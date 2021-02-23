package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.BadRequestException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.*;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

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

    public QuestionnairePhase createDefaultQuestionnairePhase(Long trainingDefinitionId, QuestionnaireType questionnaireType) {
        TrainingDefinition trainingDefinition = findDefinitionById(trainingDefinitionId);
        checkIfCanBeUpdated(trainingDefinition);

        QuestionnairePhase questionnairePhase = new QuestionnairePhase();
        questionnairePhase.setTitle("Title of questionnaire phase");
        questionnairePhase.setTrainingDefinition(trainingDefinition);
        questionnairePhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);
        questionnairePhase.setQuestionnaireType(questionnaireType);
        QuestionnairePhase persistedEntity = questionnairePhaseRepository.save(questionnairePhase);
        trainingDefinition.setLastEdited(getCurrentTimeInUTC());
        return persistedEntity;
    }

    public QuestionnairePhase updateQuestionnairePhase(Long phaseId, QuestionnairePhase questionnairePhaseToUpdate) {
        QuestionnairePhase persistedQuestionnairePhase = findQuestionnairePhaseById(phaseId);
        checkIfCanBeUpdated(persistedQuestionnairePhase.getTrainingDefinition());

        questionnairePhaseToUpdate.setId(phaseId);
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

    private QuestionnairePhase findQuestionnairePhaseById(Long phaseId) {
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

    private LocalDateTime getCurrentTimeInUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

}
