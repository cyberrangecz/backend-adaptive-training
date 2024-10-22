package cz.cyberrange.platform.training.adaptive.service.phases;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.BadRequestException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityConflictException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.AbstractPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionPhaseRelationRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionnairePhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.TrainingPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingDefinitionRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cz.cyberrange.platform.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

@Service
public class QuestionnairePhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnairePhaseService.class);

    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final QuestionnairePhaseRepository questionnairePhaseRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final QuestionRepository questionRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;
    private final QuestionPhaseRelationRepository questionPhaseRelationRepository;

    @Autowired
    public QuestionnairePhaseService(TrainingDefinitionRepository trainingDefinitionRepository,
                                     TrainingInstanceRepository trainingInstanceRepository,
                                     QuestionnairePhaseRepository questionnairePhaseRepository,
                                     AbstractPhaseRepository abstractPhaseRepository,
                                     QuestionRepository questionRepository,
                                     TrainingPhaseRepository trainingPhaseRepository,
                                     QuestionPhaseRelationRepository questionPhaseRelationRepository) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.questionnairePhaseRepository = questionnairePhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.questionRepository = questionRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
        this.questionPhaseRelationRepository = questionPhaseRelationRepository;
    }

    public QuestionnairePhase createQuestionnairePhase(TrainingDefinition trainingDefinition, QuestionnaireType questionnaireType) {
        checkIfCanBeUpdated(trainingDefinition);

        QuestionnairePhase questionnairePhase = new QuestionnairePhase();
        questionnairePhase.setTitle("Title of questionnaire phase");
        questionnairePhase.setTrainingDefinition(trainingDefinition);
        questionnairePhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinition.getId()) + 1);
        questionnairePhase.setQuestionnaireType(questionnaireType);
        return questionnairePhaseRepository.save(questionnairePhase);
    }

    public QuestionnairePhase updateQuestionnairePhase(QuestionnairePhase persistedQuestionnairePhase, QuestionnairePhase questionnairePhaseToUpdate) {
        this.checkOrderOfPhaseRelations(questionnairePhaseToUpdate.getQuestionPhaseRelations());

        questionnairePhaseToUpdate.setId(persistedQuestionnairePhase.getId());
        questionnairePhaseToUpdate.setTrainingDefinition(persistedQuestionnairePhase.getTrainingDefinition());
        questionnairePhaseToUpdate.setOrder(persistedQuestionnairePhase.getOrder());
        questionnairePhaseToUpdate.setQuestionnaireType(persistedQuestionnairePhase.getQuestionnaireType());
        questionnairePhaseToUpdate.setQuestionPhaseRelations(persistedQuestionnairePhase.getQuestionPhaseRelations());

        if (!CollectionUtils.isEmpty(questionnairePhaseToUpdate.getQuestions())) {
            questionnairePhaseToUpdate.getQuestions().forEach(question -> {
                question.setQuestionnairePhase(questionnairePhaseToUpdate);
                question.getChoices().forEach(questionChoice -> questionChoice.setQuestion(question));
            });
        }
        return questionnairePhaseRepository.save(questionnairePhaseToUpdate);
    }

    public QuestionPhaseRelation updateQuestionPhaseRelation(QuestionnairePhase questionnairePhase,
                                                             QuestionPhaseRelation questionPhaseRelationToUpdate,
                                                             Set<Long> questionIds,
                                                             Long relatedTrainingPhaseId) {
        Set<Question> questionsInPhaseRelation = new HashSet<>(Set.copyOf(questionRepository.findAllById(questionIds)));

        QuestionPhaseRelation questionPhaseRelation;
        if (Objects.isNull(questionPhaseRelationToUpdate.getId())) {
            questionPhaseRelation = new QuestionPhaseRelation();
        } else {
            questionPhaseRelation = this.findQuestionPhaseRelationById(questionPhaseRelationToUpdate.getId());
            if (!questionPhaseRelation.getQuestionnairePhase().getId().equals(questionnairePhase.getId())) {
                throw new BadRequestException("The question phase relation (ID: " + questionPhaseRelationToUpdate.getId() + ") is not associated with the" +
                        " questionnaire phase (ID: " + questionnairePhase.getId() + ".");
            }
        }
        TrainingPhase trainingPhase = this.findTrainingPhaseById(relatedTrainingPhaseId);
        questionPhaseRelation.setQuestions(questionsInPhaseRelation);
        questionPhaseRelation.setOrder(questionPhaseRelationToUpdate.getOrder());
        questionPhaseRelation.setSuccessRate(questionPhaseRelationToUpdate.getSuccessRate());
        questionPhaseRelation.setRelatedTrainingPhase(trainingPhase);
        questionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        return questionPhaseRelationRepository.save(questionPhaseRelation);
    }

    private QuestionPhaseRelation findQuestionPhaseRelationById(Long questionPhaseRelation) {
        return questionPhaseRelationRepository.findById(questionPhaseRelation)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(QuestionPhaseRelation.class, "id", questionPhaseRelation.getClass(), questionPhaseRelation, "Question phase relation not found.")));
    }

    public QuestionnairePhase findQuestionnairePhaseById(Long phaseId) {
        return questionnairePhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(QuestionnairePhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    private TrainingPhase findTrainingPhaseById(Long phaseId) {
        return trainingPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    private TrainingDefinition findDefinitionById(Long id) {
        return trainingDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", Long.class, id)));
    }

    private void checkIfCanBeUpdated(TrainingDefinition trainingDefinition) {
        if (!trainingDefinition.getState().equals(TDState.UNRELEASED)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    ARCHIVED_OR_RELEASED));
        }
        if (trainingInstanceRepository.existsAnyForTrainingDefinition(trainingDefinition.getId())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    "Cannot update training definition with already created training instance. " +
                            "Remove training instance/s before updating training definition."));
        }
    }

    private void checkOrderOfPhaseRelations(List<QuestionPhaseRelation> questionPhaseRelations) {
        questionPhaseRelations.sort(Comparator.comparing(QuestionPhaseRelation::getOrder));
        Integer order = 0;
        for (QuestionPhaseRelation questionPhaseRelation : questionPhaseRelations) {
            if (!questionPhaseRelation.getOrder().equals(order)) {
                throw new BadRequestException("The question phase relation has wrong order. The current order is " + questionPhaseRelation.getOrder() +
                        " but should be " + order);
            }
            order++;
        }
    }
}
