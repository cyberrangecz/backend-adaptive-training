package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.*;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Export import service.
 */
@Service
public class ExportImportService {

    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final QuestionPhaseRelationRepository questionPhaseRelationRepository;
    private final QuestionRepository questionRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingRunRepository trainingRunRepository;

    /**
     * Instantiates a new Export import service.
     *
     * @param trainingDefinitionRepository    the training definition repository
     * @param abstractPhaseRepository         the abstract phase repository
     * @param questionPhaseRelationRepository the questionnaire phase repository
     * @param infoPhaseRepository             the info phase repository
     * @param trainingPhaseRepository         the training phase repository
     * @param trainingInstanceRepository      the training instance repository
     * @param trainingRunRepository           the training run repository
     */
    @Autowired
    public ExportImportService(TrainingDefinitionRepository trainingDefinitionRepository,
                               AbstractPhaseRepository abstractPhaseRepository,
                               QuestionPhaseRelationRepository questionPhaseRelationRepository,
                               QuestionRepository questionRepository,
                               InfoPhaseRepository infoPhaseRepository,
                               TrainingPhaseRepository trainingPhaseRepository,
                               TrainingInstanceRepository trainingInstanceRepository,
                               TrainingRunRepository trainingRunRepository) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.questionPhaseRelationRepository = questionPhaseRelationRepository;
        this.questionRepository = questionRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.trainingRunRepository = trainingRunRepository;
    }

    /**
     * Finds training definition with given id.
     *
     * @param trainingDefinitionId the id of definition to be found.
     * @return the {@link TrainingDefinition} with the given id.
     * @throws EntityNotFoundException if training definition was not found.
     */
    public TrainingDefinition findById(Long trainingDefinitionId) {
        return trainingDefinitionRepository.findById(trainingDefinitionId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinitionId.getClass(),
                        trainingDefinitionId)));
    }

    /**
     * Creates a phase and connects it with training definition.
     *
     * @param phase      the {@link AbstractPhase} to be created.
     * @param definition the {@link TrainingDefinition} to associate phase with.
     */
    public TrainingPhase createTrainingPhase(TrainingPhase phase, TrainingDefinition definition) {
        //phase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(definition.getId()) + 1);
        phase.setTrainingDefinition(definition);
        phase.getTasks().forEach(task -> task.setTrainingPhase(phase));
        phase.getDecisionMatrix().forEach(decisionMatrixRow -> decisionMatrixRow.setTrainingPhase(phase));
        return abstractPhaseRepository.save(phase);
    }

    public QuestionnairePhase createQuestionnairePhase(QuestionnairePhase phase, TrainingDefinition definition) {
        //phase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(definition.getId()) + 1);
        phase.setTrainingDefinition(definition);
        phase.getQuestions().forEach(question -> {
            question.setQuestionnairePhase(phase);
            question.getChoices().forEach(questionChoice -> questionChoice.setQuestion(question));
        });
        return abstractPhaseRepository.save(phase);
    }

    public void createQuestionPhaseRelationPhase(QuestionPhaseRelation questionPhaseRelation,
                                                 QuestionnairePhase questionnairePhase,
                                                 Integer relatedTrainingPhaseOrder,
                                                 Set<Integer> questionOrders) {
        questionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        questionPhaseRelation.setRelatedTrainingPhase(findTrainingPhaseByDefinitionIdAndOrder(questionnairePhase.getTrainingDefinition().getId(), relatedTrainingPhaseOrder));
        questionPhaseRelation.setQuestions(questionnairePhase.getQuestions().stream()
                .filter(question -> questionOrders.contains(question.getOrder()))
                .collect(Collectors.toSet()));
        questionPhaseRelationRepository.save(questionPhaseRelation);
    }

    public InfoPhase createInfoPhase(InfoPhase phase, TrainingDefinition definition) {
        //phase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(definition.getId()) + 1);
        phase.setTrainingDefinition(definition);
        return abstractPhaseRepository.save(phase);
    }

    /**
     * Finds training instance with given id.
     *
     * @param trainingInstanceId the id of instance to be found.
     * @return the {@link TrainingInstance} with the given id.
     * @throws EntityNotFoundException if training instance was not found.
     */
    public TrainingInstance findInstanceById(Long trainingInstanceId) {
        return trainingInstanceRepository.findById(trainingInstanceId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "id", trainingInstanceId.getClass(),
                        trainingInstanceId)));
    }

    /**
     * Finds training phase with given id.
     *
     * @param trainingPhaseId the id of the training phase to be found.
     * @return the {@link TrainingPhase} with the given id.
     * @throws EntityNotFoundException if training instance was not found.
     */
    public TrainingPhase findTrainingPhaseById(Long trainingPhaseId) {
        return trainingPhaseRepository.findById(trainingPhaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingPhase.class, "id", trainingPhaseId.getClass(),
                        trainingPhaseId)));
    }

    /**
     * Finds training phase with given id.
     *
     * @param trainingDefinitionId the id of the training definition that is associated with training phase.
     * @param trainingPhaseOrder the order of the training phase.
     * @return the {@link TrainingPhase} with the given id.
     * @throws EntityNotFoundException if training instance was not found.
     */
    public TrainingPhase findTrainingPhaseByDefinitionIdAndOrder(Long trainingDefinitionId, Integer trainingPhaseOrder) {
        return trainingPhaseRepository.findByTrainingDefinitionIdAndOrder(trainingDefinitionId, trainingPhaseOrder)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingPhase.class, "order", trainingPhaseOrder.getClass(),
                        trainingPhaseOrder, "Training phase (order: " + trainingPhaseOrder + ") not found in training definition (ID: " + trainingDefinitionId + ").")));
    }

    /**
     * Finds training runs associated with training instance with given id.
     *
     * @param trainingInstanceId the id of instance which runs are to be found.
     * @return the set off all {@link TrainingRun}
     */
    public Set<TrainingRun> findRunsByInstanceId(Long trainingInstanceId) {
        return trainingRunRepository.findAllByTrainingInstanceId(trainingInstanceId);
    }
}
