package cz.cyberrange.platform.training.adaptive.service.training;

import com.querydsl.core.types.Predicate;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalWO;
import cz.cyberrange.platform.training.adaptive.persistence.entity.ParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.SolutionInfo;
import cz.cyberrange.platform.training.adaptive.persistence.entity.Submission;
import cz.cyberrange.platform.training.adaptive.persistence.entity.TRAcquisitionLock;
import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.DecisionMatrixRow;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionAnswer;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.TrainingPhaseQuestionsFulfillment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.OverallPhaseStatistics;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.AbstractPhaseImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.DecisionMatrixRowImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.QuestionPhaseRelationImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.QuestionnairePhaseImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.TrainingPhaseImport;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.api.dto.AdaptiveSmartAssistantInput;
import cz.cyberrange.platform.training.adaptive.api.dto.RelatedPhaseInfoDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.DecisionMatrixRowForAssistantDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.SubmissionType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TRState;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.BadRequestException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityConflictException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.ForbiddenException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.MicroserviceApiException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.TooManyRequestsException;
import cz.cyberrange.platform.training.adaptive.persistence.repository.ParticipantTaskAssignmentRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.QuestionAnswerRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.QuestionsPhaseRelationResultRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.SubmissionRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.TRAcquisitionLockRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.UserRefRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.AbstractPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.TrainingPhaseQuestionsFulfillmentRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingInstanceRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingRunRepository;
import cz.cyberrange.platform.training.adaptive.service.api.ElasticsearchServiceApi;
import cz.cyberrange.platform.training.adaptive.service.api.SandboxServiceApi;
import cz.cyberrange.platform.training.adaptive.service.api.SmartAssistantServiceApi;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import cz.cyberrange.platform.training.adaptive.service.audit.AuditEventsService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Training run service.
 */
@Service
public class TrainingRunService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRunService.class);
    private final SandboxServiceApi sandboxServiceApi;
    private final TrainingRunRepository trainingRunRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingPhaseQuestionsFulfillmentRepository trainingPhaseQuestionsFulfillmentRepository;
    private final UserRefRepository participantRefRepository;
    private final AuditEventsService auditEventsService;
    private final ElasticsearchServiceApi elasticsearchServiceApi;
    private final SmartAssistantServiceApi smartAssistantServiceApi;
    private final UserManagementServiceApi userManagementServiceApi;
    private final TRAcquisitionLockRepository trAcquisitionLockRepository;
    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;
    private final QuestionsPhaseRelationResultRepository questionsPhaseRelationResultRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final SubmissionRepository submissionRepository;
    @Value("${smart-assistant-service.suitable-task-delay:5}")
    private int findSuitableTaskDelay;

    /**
     * Instantiates a new Training run service.
     *
     * @param trainingRunRepository       the training run repository
     * @param abstractPhaseRepository     the abstract phase repository
     * @param trainingInstanceRepository  the training instance repository
     * @param participantRefRepository    the participant ref repository
     * @param auditEventsService          the audit events service
     * @param sandboxServiceApi           the sandbox service api
     * @param trAcquisitionLockRepository the tr acquisition lock repository
     */
    @Autowired
    public TrainingRunService(SandboxServiceApi sandboxServiceApi,
                              TrainingRunRepository trainingRunRepository,
                              AbstractPhaseRepository abstractPhaseRepository,
                              TrainingInstanceRepository trainingInstanceRepository,
                              UserRefRepository participantRefRepository,
                              TrainingPhaseQuestionsFulfillmentRepository trainingPhaseQuestionsFulfillmentRepository,
                              AuditEventsService auditEventsService,
                              ElasticsearchServiceApi elasticsearchServiceApi,
                              SmartAssistantServiceApi smartAssistantServiceApi,
                              UserManagementServiceApi userManagementServiceApi,
                              TRAcquisitionLockRepository trAcquisitionLockRepository,
                              ParticipantTaskAssignmentRepository participantTaskAssignmentRepository,
                              QuestionsPhaseRelationResultRepository questionsPhaseRelationResultRepository,
                              QuestionAnswerRepository questionAnswerRepository,
                              SubmissionRepository submissionRepository) {
        this.sandboxServiceApi = sandboxServiceApi;
        this.trainingRunRepository = trainingRunRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.participantRefRepository = participantRefRepository;
        this.trainingPhaseQuestionsFulfillmentRepository = trainingPhaseQuestionsFulfillmentRepository;
        this.auditEventsService = auditEventsService;
        this.elasticsearchServiceApi = elasticsearchServiceApi;
        this.smartAssistantServiceApi = smartAssistantServiceApi;
        this.userManagementServiceApi = userManagementServiceApi;
        this.trAcquisitionLockRepository = trAcquisitionLockRepository;
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
        this.questionsPhaseRelationResultRepository = questionsPhaseRelationResultRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.submissionRepository = submissionRepository;
    }

    /**
     * Finds specific Training Run by id.
     *
     * @param runId of a Training Run that would be returned
     * @return specific {@link TrainingRun} by id
     * @throws EntityNotFoundException training run is not found.
     */
    public TrainingRun findById(Long runId) {
        return trainingRunRepository.findById(runId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingRun.class, "id", runId.getClass(), runId)));
    }

    /**
     * /**
     * Finds specific Training Run by id including current phase.
     *
     * @param runId of a Training Run with phase that would be returned
     * @return specific {@link TrainingRun} by id
     * @throws EntityNotFoundException training run is not found.
     */
    public TrainingRun findByIdWithPhase(Long runId) {
        return trainingRunRepository.findByIdWithPhase(runId).orElseThrow(() -> new EntityNotFoundException(
                new EntityErrorDetail(TrainingRun.class, "id", runId.getClass(), runId)));
    }

    /**
     * Find all Training Runs.
     *
     * @param predicate specifies query to the database.
     * @param pageable  pageable parameter with information about pagination.
     * @return all {@link TrainingRun}s
     */
    public Page<TrainingRun> findAll(Predicate predicate, Pageable pageable) {
        return trainingRunRepository.findAll(predicate, pageable);
    }

    /**
     * Delete selected training run.
     *
     * @param trainingRunId training run to delete
     * @param forceDelete   delete training run in a force manner
     */
    public void deleteTrainingRun(Long trainingRunId, boolean forceDelete, boolean deleteDataFromElasticsearch) {
        TrainingRun trainingRun = findById(trainingRunId);
        if (!forceDelete && trainingRun.getState().equals(TRState.RUNNING)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRun.getId().getClass(), trainingRun.getId(),
                    "Cannot delete training run that is running. Consider force delete."));
        }
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(trainingRun.getParticipantRef().getUserRefId(), trainingRun.getTrainingInstance().getId());
        questionAnswerRepository.deleteAllByTrainingRunId(trainingRunId);
        submissionRepository.deleteAllByTrainingRunId(trainingRunId);
        questionsPhaseRelationResultRepository.deleteAllByTrainingRunId(trainingRunId);
        trainingPhaseQuestionsFulfillmentRepository.deleteAllByTrainingRunId(trainingRunId);
        participantTaskAssignmentRepository.deleteAllByTrainingRunId(trainingRunId);
        if(deleteDataFromElasticsearch) {
            deleteDataFromElasticsearch(trainingRun);
        }
        trainingRunRepository.delete(trainingRun);
    }

    private void deleteDataFromElasticsearch(TrainingRun trainingRun) {
        if(trainingRun.getTrainingInstance().isLocalEnvironment()) {
            String accessToken = trainingRun.getTrainingInstance().getAccessToken();
            Long userId = trainingRun.getParticipantRef().getUserRefId();
            elasticsearchServiceApi.deleteCommandsByAccessTokenAndUserId(accessToken, userId);
        } else {
            String sandboxId = trainingRun.getSandboxInstanceRefId() == null ? trainingRun.getPreviousSandboxInstanceRefId() : trainingRun.getSandboxInstanceRefId();
            elasticsearchServiceApi.deleteCommandsBySandbox(sandboxId);
        }
        elasticsearchServiceApi.deleteEventsFromTrainingRun(trainingRun.getTrainingInstance().getId(), trainingRun.getId());
    }

    /**
     * Checks whether any training runs exists for particular training instance
     *
     * @param trainingInstanceId the training instance id
     * @return boolean
     */
    public boolean existsAnyForTrainingInstance(Long trainingInstanceId) {
        return trainingRunRepository.existsAnyForTrainingInstance(trainingInstanceId);
    }


    /**
     * Finds all Training Runs of logged-in user.
     *
     * @param pageable pageable parameter with information about pagination.
     * @return {@link TrainingRun}s of logged in user.
     */
    public Page<TrainingRun> findAllByParticipantRefUserRefId(Pageable pageable) {
        return trainingRunRepository.findAllByParticipantRefId(userManagementServiceApi.getLoggedInUserRefId(), pageable);
    }

    /**
     * Finds all Training Runs of particular training instance.
     *
     * @param trainingInstanceId the training instance id
     * @return the set
     */
    public Set<TrainingRun> findAllByTrainingInstanceId(Long trainingInstanceId) {
        return trainingRunRepository.findAllByTrainingInstanceId(trainingInstanceId);
    }

    /**
     * Check if run event logging works
     *
     * @param run run to check
     * @return resulting boolean
     */
    public boolean checkRunEventLogging(TrainingRun run) {
        return !elasticsearchServiceApi.findAllEventsFromTrainingRun(run).isEmpty();
    }

    /**
     * Check if run command logging works
     *
     * @param run run to check
     * @return resulting boolean
     */
    public boolean checkRunCommandLogging(TrainingRun run) {
        List<Map<String, Object>> runCommands;
        if (run.getTrainingInstance().isLocalEnvironment()) {
            String accessToken = run.getTrainingInstance().getAccessToken();
            Long userId = run.getParticipantRef().getUserRefId();
            runCommands = elasticsearchServiceApi.findAllConsoleCommandsByAccessTokenAndUserId(accessToken, userId);
        } else {
            String sandboxId = run.getSandboxInstanceRefId() == null ? run.getPreviousSandboxInstanceRefId() : run.getSandboxInstanceRefId();
            runCommands = elasticsearchServiceApi.findAllConsoleCommandsBySandbox(sandboxId);
        }

        return !runCommands.isEmpty();
    }

    /**
     * Gets next phase of given Training Run and set new current phase.
     *
     * @param runId id of Training Run whose next phase should be returned.
     * @return {@link AbstractPhase}
     * @throws EntityNotFoundException training run or phase is not found.
     */
    public TrainingRun moveToNextPhase(Long runId) {
        TrainingRun trainingRun = findByIdWithPhase(runId);
        int currentPhaseOrder = trainingRun.getCurrentPhase().getOrder();
        int maxPhaseOrder = abstractPhaseRepository.getCurrentMaxOrder(trainingRun.getCurrentPhase().getTrainingDefinition().getId());
        if (!trainingRun.isPhaseAnswered()) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", runId.getClass(), runId,
                    "You need to answer the phase to move to the next phase."));
        }
        if (currentPhaseOrder == maxPhaseOrder) {
            throw new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "There is no next phase for current training run (ID: " + runId + ")."));
        }
        List<AbstractPhase> phases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingRun.getCurrentPhase().getTrainingDefinition().getId());
        AbstractPhase nextPhase = phases.get(currentPhaseOrder + 1);
        if (trainingRun.getCurrentPhase() instanceof InfoPhase) {
            auditEventsService.auditPhaseCompletedAction(trainingRun);
        }
        if (nextPhase instanceof TrainingPhase) {
            this.waitToPropagateEvents();
            AdaptiveSmartAssistantInput smartAssistantInput = this.gatherInputDataForSmartAssistant(trainingRun, (TrainingPhase) nextPhase, phases);
            String accessToken = trainingRun.getTrainingInstance().getAccessToken();
            Long userId = trainingRun.getParticipantRef().getUserRefId();
            // smart assistant returns order of the tasks counted from 1, and we need to decrease the number by 1, 
            // since Java order collections from 0
            int suitableTask = this.smartAssistantServiceApi.findSuitableTaskInPhase(smartAssistantInput, accessToken, userId).getSuitableTask();
            trainingRun.setCurrentTask(((TrainingPhase) nextPhase).getTasks().get(suitableTask - 1));
        } else {
            trainingRun.setCurrentTask(null);
        }
        trainingRun.setCurrentPhase(nextPhase);
        trainingRun.setIncorrectAnswerCount(0);
        trainingRunRepository.save(trainingRun);
        storeParticipantTaskAssignment(trainingRun, nextPhase);
        auditEventsService.auditPhaseStartedAction(trainingRun);

        return trainingRun;
    }

    private void waitToPropagateEvents() {
        try {
            TimeUnit.SECONDS.sleep(this.findSuitableTaskDelay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private AdaptiveSmartAssistantInput gatherInputDataForSmartAssistant(TrainingRun trainingRun, TrainingPhase nextPhase, List<AbstractPhase> phases) {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(nextPhase.getId());
        adaptiveSmartAssistantInput.setTrainingRunId(trainingRun.getId());
        adaptiveSmartAssistantInput.setPhaseXTasks(nextPhase.getTasks().size());
        adaptiveSmartAssistantInput.setPhaseIds(phases.stream()
                .map(AbstractPhase::getId)
                .collect(Collectors.toList()));
        adaptiveSmartAssistantInput.setDecisionMatrix(mapToDecisionMatrixRowForAssistantDTO(nextPhase.getDecisionMatrix(), nextPhase.getAllowedCommands(),
                nextPhase.getAllowedWrongAnswers(), phases, trainingRun.getId()));
        return adaptiveSmartAssistantInput;
    }

    public AdaptiveSmartAssistantInput gatherInputDataForSmartAssistant(Long trainingRunId, TrainingPhaseImport nextPhase, List<AbstractPhaseImport> phases,
                                                                        OverallPhaseStatistics currentPhaseStatistics, QuestionnairePhaseImport preGameQuestionnairePhase) {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = new AdaptiveSmartAssistantInput();
        adaptiveSmartAssistantInput.setPhaseX(nextPhase.getId());
        adaptiveSmartAssistantInput.setTrainingRunId(trainingRunId);
        adaptiveSmartAssistantInput.setPhaseXTasks(nextPhase.getTasks().size());
        adaptiveSmartAssistantInput.setPhaseIds(phases.stream()
                .map(AbstractPhaseImport::getId)
                .collect(Collectors.toList()));
        adaptiveSmartAssistantInput.setDecisionMatrix(mapToDecisionMatrixRowDTOForAssistant(nextPhase.getDecisionMatrix(), nextPhase.getAllowedCommands(),
                nextPhase.getAllowedWrongAnswers(), phases, currentPhaseStatistics, preGameQuestionnairePhase));
        return adaptiveSmartAssistantInput;
    }

    private List<DecisionMatrixRowForAssistantDTO> mapToDecisionMatrixRowDTOForAssistant(List<DecisionMatrixRowImport> decisionMatrixRows, int allowedCommands,
                                                                                         int allowedWrongAnswers, List<AbstractPhaseImport> phases, OverallPhaseStatistics currentPhaseStatistics, QuestionnairePhaseImport preGameQuestionnairePhase) {
        List<AbstractPhaseImport> orderedTrainingPhases = phases.stream()
                .filter(phase -> phase instanceof TrainingPhaseImport)
                .limit(decisionMatrixRows.size())
                .sorted(Comparator.comparing(AbstractPhaseImport::getOrder))
                .collect(Collectors.toList());

        return decisionMatrixRows.stream().map(row -> {
            DecisionMatrixRowForAssistantDTO decisionMatrixRowForAssistantDTO = new DecisionMatrixRowForAssistantDTO();
            decisionMatrixRowForAssistantDTO.setId(row.getId());
            decisionMatrixRowForAssistantDTO.setAllowedCommands(allowedCommands);
            decisionMatrixRowForAssistantDTO.setAllowedWrongAnswers(allowedWrongAnswers);
            decisionMatrixRowForAssistantDTO.setCompletedInTime(row.getCompletedInTime());
            decisionMatrixRowForAssistantDTO.setKeywordUsed(row.getKeywordUsed());
            decisionMatrixRowForAssistantDTO.setOrder(row.getOrder());
            decisionMatrixRowForAssistantDTO.setQuestionnaireAnswered(row.getQuestionnaireAnswered());
            decisionMatrixRowForAssistantDTO.setSolutionDisplayed(row.getSolutionDisplayed());
            decisionMatrixRowForAssistantDTO.setWrongAnswers(row.getWrongAnswers());
            decisionMatrixRowForAssistantDTO.setRelatedPhaseInfo(getPhaseInfo(orderedTrainingPhases.get(row.getOrder()), currentPhaseStatistics, preGameQuestionnairePhase));
            return decisionMatrixRowForAssistantDTO;
        }).collect(Collectors.toList());
    }

    private RelatedPhaseInfoDTO getPhaseInfo(AbstractPhaseImport abstractPhase, OverallPhaseStatistics currentPhaseStatistics, QuestionnairePhaseImport preGameQuestionnairePhase) {
        RelatedPhaseInfoDTO relatedPhaseInfoDTO = new RelatedPhaseInfoDTO();
        relatedPhaseInfoDTO.setId(abstractPhase.getId());
        if (abstractPhase instanceof TrainingPhaseImport) {
            boolean trainingPhaseQuestionsFulfillment = getTrainingPhaseQuestionsFulfillment(preGameQuestionnairePhase, abstractPhase.getOrder(), currentPhaseStatistics);
            relatedPhaseInfoDTO.setCorrectlyAnsweredRelatedQuestions(trainingPhaseQuestionsFulfillment);
            relatedPhaseInfoDTO.setEstimatedPhaseTime(((TrainingPhaseImport) abstractPhase).getEstimatedDuration());
        }
        return relatedPhaseInfoDTO;
    }

    private boolean getTrainingPhaseQuestionsFulfillment(QuestionnairePhaseImport preGameQuestionnairePhase, Integer currentPhaseOrder, OverallPhaseStatistics currentPhaseStatistics) {
        QuestionPhaseRelationImport phaseRelation = preGameQuestionnairePhase.getPhaseRelations().stream()
                .filter(relation -> relation.getPhaseOrder().equals(currentPhaseOrder))
                .findFirst()
                .orElse(null);
        if (phaseRelation == null) {
            return false;
        }
        AtomicInteger success = new AtomicInteger(0);
        phaseRelation.getQuestionOrders().forEach(question -> {
            if (question >= currentPhaseStatistics.getQuestionsAnswer().size()) {
                success.addAndGet(0);
            } else {
                success.addAndGet(currentPhaseStatistics.getQuestionsAnswer().get(question) ? 1 : 0);
            }
        });
        return ((success.get() / phaseRelation.getQuestionOrders().size()) * 100) >= phaseRelation.getSuccessRate();
    }

    private List<DecisionMatrixRowForAssistantDTO> mapToDecisionMatrixRowForAssistantDTO(List<DecisionMatrixRow> decisionMatrixRows, int allowedCommands,
                                                                                         int allowedWrongAnswers, List<AbstractPhase> phases, Long trainingRunId) {
        List<AbstractPhase> orderedTrainingPhases = phases.stream()
                .filter(phase -> phase instanceof TrainingPhase)
                .limit(decisionMatrixRows.size())
                .sorted(Comparator.comparing(AbstractPhase::getOrder))
                .collect(Collectors.toList());
        List<Long> trainingPhasesIds = orderedTrainingPhases.stream()
                .map(AbstractPhase::getId)
                .collect(Collectors.toList());
        Map<Integer, TrainingPhaseQuestionsFulfillment> questionnairesFulfillment = this.trainingPhaseQuestionsFulfillmentRepository.findByTrainingPhasesAndTrainingRun(trainingPhasesIds, trainingRunId)
                .stream()
                .collect(Collectors.toMap(fulfillment -> trainingPhasesIds.indexOf(fulfillment.getTrainingPhase().getId()), Function.identity()));

        return decisionMatrixRows.stream().map(row -> {
            DecisionMatrixRowForAssistantDTO decisionMatrixRowForAssistantDTO = new DecisionMatrixRowForAssistantDTO();
            decisionMatrixRowForAssistantDTO.setId(row.getId());
            decisionMatrixRowForAssistantDTO.setAllowedCommands(allowedCommands);
            decisionMatrixRowForAssistantDTO.setAllowedWrongAnswers(allowedWrongAnswers);
            decisionMatrixRowForAssistantDTO.setCompletedInTime(row.getCompletedInTime());
            decisionMatrixRowForAssistantDTO.setKeywordUsed(row.getKeywordUsed());
            decisionMatrixRowForAssistantDTO.setOrder(row.getOrder());
            decisionMatrixRowForAssistantDTO.setQuestionnaireAnswered(row.getQuestionnaireAnswered());
            decisionMatrixRowForAssistantDTO.setSolutionDisplayed(row.getSolutionDisplayed());
            decisionMatrixRowForAssistantDTO.setWrongAnswers(row.getWrongAnswers());
            decisionMatrixRowForAssistantDTO.setRelatedPhaseInfo(getPhaseInfo(orderedTrainingPhases.get(row.getOrder()), getFulfilled(questionnairesFulfillment, row.getOrder())));
            return decisionMatrixRowForAssistantDTO;
        }).collect(Collectors.toList());
    }

    private boolean getFulfilled(Map<Integer, TrainingPhaseQuestionsFulfillment> questionnairesFulfillment, int order) {
        TrainingPhaseQuestionsFulfillment fulfillment = questionnairesFulfillment.get(order);
        return fulfillment == null || fulfillment.isFulfilled();
    }

    private RelatedPhaseInfoDTO getPhaseInfo(AbstractPhase abstractPhase, Boolean trainingPhaseQuestionsFulfillment) {
        RelatedPhaseInfoDTO relatedPhaseInfoDTO = new RelatedPhaseInfoDTO();
        relatedPhaseInfoDTO.setId(abstractPhase.getId());
        if (abstractPhase instanceof TrainingPhase) {
            relatedPhaseInfoDTO.setCorrectlyAnsweredRelatedQuestions(trainingPhaseQuestionsFulfillment);
            relatedPhaseInfoDTO.setEstimatedPhaseTime(((TrainingPhase) abstractPhase).getEstimatedDuration());
        }
        return relatedPhaseInfoDTO;
    }

    private void storeParticipantTaskAssignment(TrainingRun trainingRun, AbstractPhase nextPhase) {
        ParticipantTaskAssignment participantTaskAssignment = new ParticipantTaskAssignment();
        participantTaskAssignment.setTrainingRun(trainingRun);
        participantTaskAssignment.setAbstractPhase(nextPhase);
        participantTaskAssignment.setTask(trainingRun.getCurrentTask());
        this.participantTaskAssignmentRepository.save(participantTaskAssignment);
    }

    /**
     * Get previous/current phase (visited) of given Training Run.
     *
     * @param runId ID of Training Run whose visited phase should be returned.
     * @param phaseId ID of the visited phase that should be returned.
     * @return {@link AbstractPhase}
     */
    public AbstractPhase getVisitedPhase(Long runId, Long phaseId) {
        TrainingRun trainingRun = findByIdWithPhase(runId);
        AbstractPhase abstractPhase = abstractPhaseRepository.findById(phaseId).orElseThrow(
                () -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, "Phase not found")));
        TrainingDefinition trainingRunDefinition = trainingRun.getTrainingInstance().getTrainingDefinition();
        if (!abstractPhase.getTrainingDefinition().getId().equals(trainingRunDefinition.getId())) {
            throw new EntityConflictException(new EntityErrorDetail("Requested phase (ID: " + phaseId + ") is not part of the training run (ID: " + runId + ")."));
        }
        if (abstractPhase.getOrder() > trainingRun.getCurrentPhase().getOrder()) {
            throw new EntityConflictException(new EntityErrorDetail("Requested phase (ID: " + phaseId + ") hasn't been visited yet"));
        }
        return abstractPhase;
    }

    public Task getVisitedTask(TrainingPhase trainingPhase, TrainingRun trainingRun) {
        return this.participantTaskAssignmentRepository.findByAbstractPhaseIdAndTrainingRunId(trainingPhase.getId(), trainingRun.getId())
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail("Task assigned to training phase (ID: " + trainingPhase.getId() + ") " +
                        "in training run (ID: " + trainingRun.getId() + ") not found.")))
                .getTask();
    }

    /**
     * Finds all Training Runs of specific Training Definition of logged-in user.
     *
     * @param definitionId id of Training Definition
     * @param pageable     pageable parameter with information about pagination.
     * @return {@link TrainingRun}s of specific Training Definition of logged-in user
     */
    public Page<TrainingRun> findAllByTrainingDefinitionAndParticipant(Long definitionId, Pageable pageable) {
        return trainingRunRepository.findAllByTrainingDefinitionIdAndParticipantUserRefId(definitionId, userManagementServiceApi.getLoggedInUserRefId(), pageable);
    }

    /**
     * Finds all Training Runs of specific training definition.
     *
     * @param definitionId id of Training Definition whose Training Runs would be returned.
     * @param pageable     pageable parameter with information about pagination.
     * @return {@link TrainingRun}s of specific Training Definition
     */
    public Page<TrainingRun> findAllByTrainingDefinition(Long definitionId, Pageable pageable) {
        return trainingRunRepository.findAllByTrainingDefinitionId(definitionId, pageable);
    }

    /**
     * Gets list of all phase in Training Definition.
     *
     * @param definitionId must be id of first phase of some Training Definition.
     * @return List of {@link AbstractPhase}s
     * @throws EntityNotFoundException one of the phase is not found.
     */
    public List<AbstractPhase> getPhases(Long definitionId) {
        return abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(definitionId);
    }

    /**
     * Access training run based on given accessToken.
     *
     * @param trainingInstance the training instance
     * @param participantRefId the participant ref id
     * @return accessed {@link TrainingRun}
     * @throws EntityNotFoundException no active training instance for given access token, no starting phase in training definition.
     * @throws EntityConflictException pool of sandboxes is not created for training instance.
     */
    public TrainingRun createTrainingRun(TrainingInstance trainingInstance, Long participantRefId) {
        AbstractPhase initialPhase = findFirstPhaseForTrainingRun(trainingInstance.getTrainingDefinition().getId());
        TrainingRun trainingRun = getNewTrainingRun(initialPhase, trainingInstance, LocalDateTime.now(Clock.systemUTC()), trainingInstance.getEndTime(), participantRefId);
        if (initialPhase instanceof TrainingPhase) {
            trainingRun.setCurrentTask(((TrainingPhase) initialPhase).getTasks().get(0));
        }
        trainingRunRepository.save(trainingRun);
        storeParticipantTaskAssignment(trainingRun, initialPhase);
        return trainingRun;
    }

    public  void auditTrainingRunStarted(TrainingRun trainingRun) {
        auditEventsService.auditTrainingRunStartedAction(trainingRun);
        auditEventsService.auditPhaseStartedAction(trainingRun);
    }

    /**
     * Find running training run of user optional.
     *
     * @param accessToken      the access token
     * @param participantRefId the participant ref id
     * @return the optional
     */
    public Optional<TrainingRun> findRunningTrainingRunOfUser(String accessToken, Long participantRefId) {
        return trainingRunRepository.findRunningTrainingRunOfUser(accessToken, participantRefId);
    }

    /**
     * Gets training instance for particular access token.
     *
     * @param accessToken the access token
     * @return the training instance for particular access token
     */
    public TrainingInstance getTrainingInstanceForParticularAccessToken(String accessToken) {
        TrainingInstance trainingInstance = trainingInstanceRepository.findByStartTimeAfterAndEndTimeBeforeAndAccessToken(LocalDateTime.now(Clock.systemUTC()), accessToken)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "accessToken", accessToken.getClass(), accessToken,
                        "There is no active training session matching access token.")));
        if (!trainingInstance.isLocalEnvironment() && trainingInstance.getPoolId() == null) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", trainingInstance.getId().getClass(), trainingInstance.getId(),
                    "At first organizer must allocate sandboxes for training instance."));
        }
        List<AbstractPhase> phases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingInstance.getTrainingDefinition().getId());
        for (var phase: phases) {
            if (phase instanceof TrainingPhase && ((TrainingPhase) phase).getTasks().isEmpty()) {
                throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", trainingInstance.getId().getClass(), trainingInstance.getId(),
                        "Training phase " + phase.getOrder() + " contains no tasks."));
            }
        }
        return trainingInstance;
    }

    /**
     * Tr acquisition lock to prevent many requests from the same user. This method is called in a new transaction that means that the existing one is suspended.
     *
     * @param participantRefId   the participant ref id
     * @param trainingInstanceId the training instance id
     * @param accessToken        the access token
     */
    @TransactionalWO(propagation = Propagation.REQUIRES_NEW)
    public void trAcquisitionLockToPreventManyRequestsFromSameUser(Long participantRefId, Long trainingInstanceId, String accessToken) {
        try {
            trAcquisitionLockRepository.saveAndFlush(new TRAcquisitionLock(participantRefId, trainingInstanceId, LocalDateTime.now(Clock.systemUTC())));
        } catch (DataIntegrityViolationException ex) {
            throw new TooManyRequestsException(new EntityErrorDetail(TrainingInstance.class, "accessToken", accessToken.getClass(), accessToken,
                    "Training run has been already accessed and cannot be created again. Please resume Training Run"));
        }
    }

    @TransactionalWO(propagation = Propagation.REQUIRES_NEW)
    public void deleteTrAcquisitionLockToPreventManyRequestsFromSameUser(Long participantRefId, Long trainingInstanceId) {
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(participantRefId, trainingInstanceId);
    }

    private AbstractPhase findFirstPhaseForTrainingRun(Long trainingDefinitionId) {
        return abstractPhaseRepository.findFirstPhaseOfTrainingDefinition(trainingDefinitionId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", Long.class, trainingDefinitionId,
                        "No starting phase available for this training definition.")));
    }

    private TrainingRun getNewTrainingRun(AbstractPhase currentPhase, TrainingInstance trainingInstance, LocalDateTime startTime, LocalDateTime endTime, Long participantRefId) {
        TrainingRun newTrainingRun = new TrainingRun();
        newTrainingRun.setCurrentPhase(currentPhase);

        User userRef = participantRefRepository.createOrGet(participantRefId);
        newTrainingRun.setParticipantRef(userRef);

        newTrainingRun.setState(TRState.RUNNING);
        newTrainingRun.setTrainingInstance(trainingInstance);
        newTrainingRun.setStartTime(startTime);
        newTrainingRun.setEndTime(endTime);
        return newTrainingRun;
    }

    /**
     * Connects available sandbox with given Training run.
     *
     * @param trainingRun that will be connected with sandbox
     * @param poolId      the pool id
     * @return Training run with assigned sandbox
     * @throws ForbiddenException       no available sandbox.
     * @throws MicroserviceApiException error calling OpenStack Sandbox Service API
     */
    public TrainingRun assignSandbox(TrainingRun trainingRun, long poolId) {
        Pair<Integer, String> sandboxRefAndId = this.sandboxServiceApi.getAndLockSandboxForTrainingRun(poolId, trainingRun.getTrainingInstance().getAccessToken());
        trainingRun.setSandboxInstanceRefId(sandboxRefAndId.getRight());
        trainingRun.setSandboxInstanceAllocationId(sandboxRefAndId.getLeft());
        return trainingRunRepository.save(trainingRun);
    }

    /**
     * Resume previously closed training run.
     *
     * @param trainingRunId id of training run to be resumed.
     * @return {@link TrainingRun}
     * @throws EntityNotFoundException training run is not found.
     */
    public TrainingRun resumeTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = findByIdWithPhase(trainingRunId);
        TrainingInstance trainingInstance = trainingRun.getTrainingInstance();
        if (trainingRun.getState().equals(TRState.FINISHED) || trainingRun.getState().equals(TRState.ARCHIVED)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot resume finished training run."));
        }
        if (trainingInstance.getEndTime().isBefore(LocalDateTime.now(Clock.systemUTC()))) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot resume training run after end of training instance."));
        }
        if (!trainingInstance.isLocalEnvironment() && trainingInstance.getPoolId() == null) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "The pool assignment of the appropriate training instance has been probably canceled. Please contact the organizer."));
        }

        if (!trainingInstance.isLocalEnvironment() && trainingRun.getSandboxInstanceRefId() == null) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Sandbox of this training run was already deleted, you have to start new training."));
        }
        auditEventsService.auditTrainingRunResumedAction(trainingRun);
        return trainingRun;
    }

    /**
     * Check given answer of given Training Run.
     *
     * @param runId  id of Training Run to check answer.
     * @param answer string which player submit.
     * @return true if answer is correct, false if answer is wrong.
     * @throws EntityNotFoundException training run is not found.
     * @throws BadRequestException     the current phase of training run is not training phase.
     */
    public boolean isCorrectAnswer(Long runId, String answer) {
        TrainingRun trainingRun = findByIdWithPhase(runId);
        AbstractPhase currentPhase = trainingRun.getCurrentPhase();
        if (currentPhase.getClass() != TrainingPhase.class) {
            throw new BadRequestException("Current phase is not training phase and does not have answer.");
        }
        if (trainingRun.isPhaseAnswered()) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", Long.class, runId, "The answer of the current phase of training run has been already corrected."));
        }
        Task currentTask = trainingRun.getCurrentTask();
        if (currentTask.getAnswer().equals(answer)) {
            trainingRun.setPhaseAnswered(true);
            auditEventsService.auditCorrectAnswerSubmittedAction(trainingRun, answer);
            auditEventsService.auditPhaseCompletedAction(trainingRun);
            auditSubmission(trainingRun, SubmissionType.CORRECT, answer);
            return true;
        } else if (currentTask.getIncorrectAnswerLimit() != trainingRun.getIncorrectAnswerCount()) {
            trainingRun.setIncorrectAnswerCount(trainingRun.getIncorrectAnswerCount() + 1);
        }
        auditEventsService.auditWrongAnswerSubmittedAction(trainingRun, answer);
        auditSubmission(trainingRun, SubmissionType.INCORRECT, answer);
        return false;
    }

    /**
     * Check given passkey of given Training Run.
     *
     * @param runId  id of Training Run to check passkey.
     * @param passkey string which player submit.
     * @return true if passkey is correct, false if passkey is wrong.
     * @throws EntityNotFoundException training run is not found.
     * @throws BadRequestException     the current phase of training run is not training phase.
     */
    public boolean isCorrectPasskey(Long runId, String passkey) {
        TrainingRun trainingRun = findByIdWithPhase(runId);
        AbstractPhase currentPhase = trainingRun.getCurrentPhase();
        if (currentPhase.getClass() != AccessPhase.class) {
            throw new BadRequestException("Current phase is not access phase and does not have passkey.");
        }
        if (trainingRun.isPhaseAnswered()) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", Long.class, runId, "The passkey of the current phase of training run has been already corrected."));
        }
        AccessPhase accessPhase = (AccessPhase) trainingRun.getCurrentPhase();
        if (accessPhase.getPasskey().equals(passkey)) {
            trainingRun.setPhaseAnswered(true);
            auditEventsService.auditCorrectPasskeySubmittedAction(trainingRun, passkey);
            auditEventsService.auditPhaseCompletedAction(trainingRun);
            return true;
        }
        auditEventsService.auditWrongPasskeySubmittedAction(trainingRun, passkey);
        return false;
    }

    /**
     * Gets remaining attempts to solve current phase of training run.
     *
     * @param trainingRunId the training run id
     * @return the remaining attempts
     */
    public int getRemainingAttempts(Long trainingRunId) {
        TrainingRun trainingRun = findByIdWithPhase(trainingRunId);
        AbstractPhase phase = trainingRun.getCurrentPhase();
        if (phase instanceof TrainingPhase) {
            if (trainingRun.isSolutionTaken()) {
                return 0;
            }
            return trainingRun.getCurrentTask().getIncorrectAnswerLimit() - trainingRun.getIncorrectAnswerCount();
        }
        throw new BadRequestException("Current phase is not training phase and does not have an answer.");
    }

    /**
     * Gets solution of current phase of given Training Run.
     *
     * @param trainingRunId id of Training Run which current phase gets solution for.
     * @return solution of current phase.
     * @throws EntityNotFoundException training run is not found.
     * @throws BadRequestException     the current phase of training run is not training phase.
     */
    public String getSolution(Long trainingRunId) {
        TrainingRun trainingRun = findByIdWithPhase(trainingRunId);
        AbstractPhase currentPhase = trainingRun.getCurrentPhase();
        if (currentPhase instanceof TrainingPhase trainingPhase) {
            if (!trainingRun.isSolutionTaken()) {
                trainingRun.setSolutionTaken(true);
                trainingRun.addSolutionInfo(
                        new SolutionInfo(
                                trainingPhase.getId(),
                                trainingRun.getCurrentTask().getId(),
                                trainingRun.getCurrentTask().getSolution()
                        ));
                trainingRunRepository.save(trainingRun);
                auditEventsService.auditSolutionDisplayedAction(trainingRun);
            }
            String solution = trainingRun.getCurrentTask().getSolution();
            if (solution.contains("${ANSWER}")) {
                solution = solution.replaceAll("\\$\\{ANSWER\\}", trainingRun.getCurrentTask().getAnswer());
            }
            return solution;
        } else {
            throw new BadRequestException("Current phase is not training phase and does not have solution.");
        }
    }

    /**
     * Gets max phase order of phase from definition.
     *
     * @param definitionId id of training definition.
     * @return max order of phase.
     */
    public int getMaxPhaseOrder(Long definitionId) {
        return abstractPhaseRepository.getCurrentMaxOrder(definitionId);
    }

    /**
     * Finish training run.
     *
     * @param trainingRunId id of training run to be finished.
     * @throws EntityNotFoundException training run is not found.
     */
    public void finishTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = findByIdWithPhase(trainingRunId);
        int maxOrder = abstractPhaseRepository.getCurrentMaxOrder(trainingRun.getCurrentPhase().getTrainingDefinition().getId());
        if (trainingRun.getCurrentPhase().getOrder() != maxOrder) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot finish training run because current phase is not last."));
        }
        if (!trainingRun.isPhaseAnswered()) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot finish training run because current phase is not answered."));
        }
        trainingRun.setState(TRState.FINISHED);
        trainingRun.setEndTime(LocalDateTime.now(Clock.systemUTC()));
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(trainingRun.getParticipantRef().getUserRefId(), trainingRun.getTrainingInstance().getId());
        if (trainingRun.getCurrentPhase() instanceof InfoPhase) {
            auditEventsService.auditPhaseCompletedAction(trainingRun);
        }
        auditEventsService.auditTrainingRunEndedAction(trainingRun);
    }

    /**
     * Finds all trainee submissions.
     *
     * @param runId the training run id
     * @param phaseId the training phase id
     * @return the list of submissions
     */
    public List<Submission> findTraineesSubmissions(Long runId, Long phaseId) {
        if (phaseId != null) {
            return submissionRepository.findByTrainingRunIdAndPhaseId(runId, phaseId);
        }
        return submissionRepository.findByTrainingRunId(runId);
    }

    /**
     * Finds all trainees commands executed in sandbox during the training run.
     *
     * @param runId the training run id
     * @return the list of commands
     */
    public List<Map<String, Object>> getCommandsByTrainingRun(Long runId) {
        TrainingRun run = findById(runId);
        if (run.getTrainingInstance().isLocalEnvironment()) {
            return elasticsearchServiceApi.findAllConsoleCommandsByAccessTokenAndUserId(
                    run.getTrainingInstance().getAccessToken(),
                    run.getParticipantRef().getUserRefId());
        }
        String sandboxId = run.getSandboxInstanceRefId() == null ? run.getPreviousSandboxInstanceRefId() : run.getSandboxInstanceRefId();
        return elasticsearchServiceApi.findAllConsoleCommandsBySandbox(sandboxId);
    }

    /**
     * Archive training run.
     *
     * @param trainingRunId id of training run to be archived.
     * @throws EntityNotFoundException training run is not found.
     */
    public void archiveTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = findById(trainingRunId);
        trainingRun.setState(TRState.ARCHIVED);
        trainingRun.setPreviousSandboxInstanceRefId(trainingRun.getSandboxInstanceRefId());
        trainingRun.setSandboxInstanceRefId(null);
        trainingRun.setSandboxInstanceAllocationId(null);
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(trainingRun.getParticipantRef().getUserRefId(), trainingRun.getTrainingInstance().getId());
        trainingRunRepository.save(trainingRun);
    }

    public List<QuestionAnswer> getQuestionAnswersByTrainingRunId(Long runId) {
        return questionAnswerRepository.getAllByTrainingRunId(runId);
    }

    private void auditSubmission(TrainingRun trainingRun, SubmissionType submissionType, String answer) {
        Submission submission = new Submission();
        submission.setDate(LocalDateTime.now(Clock.systemUTC()));
        submission.setPhase(trainingRun.getCurrentPhase());
        submission.setTask(trainingRun.getCurrentTask());
        submission.setTrainingRun(trainingRun);
        submission.setProvided(answer);
        submission.setType(submissionType);
        submission.setIpAddress(getUserIpAddress());
        submissionRepository.save(submission);
    }

    private String getUserIpAddress() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return requestAttributes == null ? "" : requestAttributes.getRequest().getRemoteAddr();
    }

}
