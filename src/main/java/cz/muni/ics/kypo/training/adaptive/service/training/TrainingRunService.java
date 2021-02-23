package cz.muni.ics.kypo.training.adaptive.service.training;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.TRAcquisitionLock;
import cz.muni.ics.kypo.training.adaptive.domain.UserRef;
import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import cz.muni.ics.kypo.training.adaptive.exceptions.*;
import cz.muni.ics.kypo.training.adaptive.repository.TRAcquisitionLockRepository;
import cz.muni.ics.kypo.training.adaptive.repository.UserRefRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingRunRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.ElasticsearchServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.api.SandboxServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.audit.AuditEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The type Training run service.
 */
@Service
public class TrainingRunService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRunService.class);

    private SandboxServiceApi sandboxServiceApi;
    private TrainingRunRepository trainingRunRepository;
    private AbstractPhaseRepository abstractPhaseRepository;
    private TrainingInstanceRepository trainingInstanceRepository;
    private UserRefRepository participantRefRepository;
    private AuditEventsService auditEventsService;
    private ElasticsearchServiceApi elasticsearchServiceApi;
    private UserManagementServiceApi userManagementServiceApi;
    private TRAcquisitionLockRepository trAcquisitionLockRepository;

    /**
     * Instantiates a new Training run service.
     *
     * @param trainingRunRepository       the training run repository
     * @param abstractPhaseRepository     the abstract phase repository
     * @param trainingInstanceRepository  the training instance repository
     * @param participantRefRepository    the participant ref repository
     * @param auditEventsService          the audit events service
     * @param securityService             the security service
     * @param trAcquisitionLockRepository the tr acquisition lock repository
     */
    @Autowired
    public TrainingRunService(SandboxServiceApi sandboxServiceApi,
                              TrainingRunRepository trainingRunRepository,
                              AbstractPhaseRepository abstractPhaseRepository,
                              TrainingInstanceRepository trainingInstanceRepository,
                              UserRefRepository participantRefRepository,
                              AuditEventsService auditEventsService,
                              ElasticsearchServiceApi elasticsearchServiceApi,
                              UserManagementServiceApi userManagementServiceApi,
                              TRAcquisitionLockRepository trAcquisitionLockRepository) {
        this.sandboxServiceApi = sandboxServiceApi;
        this.trainingRunRepository = trainingRunRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.participantRefRepository = participantRefRepository;
        this.auditEventsService = auditEventsService;
        this.elasticsearchServiceApi = elasticsearchServiceApi;
        this.userManagementServiceApi = userManagementServiceApi;
        this.trAcquisitionLockRepository = trAcquisitionLockRepository;
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
    public void deleteTrainingRun(Long trainingRunId, boolean forceDelete) {
        TrainingRun trainingRun = findById(trainingRunId);
        if (!forceDelete && trainingRun.getState().equals(TRState.RUNNING)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRun.getId().getClass(), trainingRun.getId(),
                    "Cannot delete training run that is running. Consider force delete."));
        }
        elasticsearchServiceApi.deleteEventsFromTrainingRun(trainingRun.getTrainingInstance().getId(), trainingRunId);
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(trainingRun.getParticipantRef().getUserRefId(), trainingRun.getTrainingInstance().getId());
        trainingRunRepository.delete(trainingRun);
    }

    /**
     * Checks whether any trainin runs exists for particular training instance
     *
     * @param trainingInstanceId the training instance id
     * @return boolean boolean
     */
    public boolean existsAnyForTrainingInstance(Long trainingInstanceId) {
        return trainingRunRepository.existsAnyForTrainingInstance(trainingInstanceId);
    }


    /**
     * Finds all Training Runs of logged in user.
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
     * Gets next phase of given Training Run and set new current phase.
     *
     * @param runId id of Training Run whose next phase should be returned.
     * @return {@link AbstractPhase}
     * @throws EntityNotFoundException training run or phase is not found.
     */
    public AbstractPhase getNextPhase(Long runId) {
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
        int nextPhaseIndex = phases.indexOf(trainingRun.getCurrentPhase()) + 1;
        AbstractPhase nextPhase = phases.get(nextPhaseIndex);
        if (trainingRun.getCurrentPhase() instanceof InfoPhase) {
            auditEventsService.auditPhaseCompletedAction(trainingRun);
        }
        if (nextPhase instanceof TrainingPhase) {
            trainingRun.setCurrentTask(((TrainingPhase) nextPhase).getTasks().get(0));
        } else {
            trainingRun.setCurrentTask(null);
        }
        trainingRun.setCurrentPhase(nextPhase);
        trainingRun.setIncorrectAnswerCount(0);
        trainingRunRepository.save(trainingRun);
        auditEventsService.auditPhaseStartedAction(trainingRun);

        return nextPhase;
    }

    /**
     * Finds all Training Runs of specific Training Definition of logged in user.
     *
     * @param definitionId id of Training Definition
     * @param pageable     pageable parameter with information about pagination.
     * @return {@link TrainingRun}s of specific Training Definition of logged in user
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
        return trainingRunRepository.save(trainingRun);
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
        if (trainingInstance.getPoolId() == null) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", trainingInstance.getId().getClass(), trainingInstance.getId(),
                    "At first organizer must allocate sandboxes for training instance."));
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

        Optional<UserRef> userRefOpt = participantRefRepository.findUserByUserRefId(participantRefId);
        if (userRefOpt.isPresent()) {
            newTrainingRun.setParticipantRef(userRefOpt.get());
        } else {
            newTrainingRun.setParticipantRef(participantRefRepository.save(new UserRef(userManagementServiceApi.getLoggedInUserRefId())));
        }
        newTrainingRun.setQuestionnaireResponses("[]");
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
        Long sandboxInstanceRef = this.sandboxServiceApi.getAndLockSandboxForTrainingRun(poolId);
        trainingRun.setSandboxInstanceRefId(sandboxInstanceRef);
        auditEventsService.auditTrainingRunStartedAction(trainingRun);
        auditEventsService.auditPhaseStartedAction(trainingRun);
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
        if (trainingRun.getState().equals(TRState.FINISHED) || trainingRun.getState().equals(TRState.ARCHIVED)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot resume finished training run."));
        }
        if (trainingRun.getTrainingInstance().getEndTime().isBefore(LocalDateTime.now(Clock.systemUTC()))) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Cannot resume training run after end of training instance."));
        }
        if (trainingRun.getTrainingInstance().getPoolId() == null) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "The pool assignment of the appropriate training instance has been probably canceled. Please contact the organizer."));
        }

        if (trainingRun.getSandboxInstanceRefId() == null) {
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
        if (currentPhase instanceof TrainingPhase) {
            if (trainingRun.isPhaseAnswered()) {
                throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", Long.class, runId, "The answer of the current phase of training run has been already corrected."));
            }
            Task currentTask = trainingRun.getCurrentTask();
            if (currentTask.getAnswer().equals(answer)) {
                trainingRun.setPhaseAnswered(true);
                auditEventsService.auditCorrectAnswerSubmittedAction(trainingRun, answer);
                auditEventsService.auditPhaseCompletedAction(trainingRun);
                return true;
            } else if (currentTask.getIncorrectAnswerLimit() != trainingRun.getIncorrectAnswerCount()) {
                trainingRun.setIncorrectAnswerCount(trainingRun.getIncorrectAnswerCount() + 1);
            }
            auditEventsService.auditWrongAnswerSubmittedAction(trainingRun, answer);
            return false;
        } else {
            throw new BadRequestException("Current phase is not training phase and does not have answer.");
        }
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
        if (currentPhase instanceof TrainingPhase) {
            if (!trainingRun.isSolutionTaken()) {
                trainingRun.setSolutionTaken(true);
                trainingRunRepository.save(trainingRun);
                auditEventsService.auditSolutionDisplayedAction(trainingRun);
            }
            return trainingRun.getCurrentTask().getSolution();
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
        TrainingRun trainingRun = findById(trainingRunId);
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
        trAcquisitionLockRepository.deleteByParticipantRefIdAndTrainingInstanceId(trainingRun.getParticipantRef().getUserRefId(), trainingRun.getTrainingInstance().getId());
        trainingRunRepository.save(trainingRun);
    }

    /**
     * Evaluate and store responses to questionnaire.
     *
     * @param trainingRunId     id of training run to be finished.
     * @param responsesAsString response to questionnaire to be evaluated
     * @throws EntityNotFoundException training run is not found.
     */
    public void evaluateResponsesToQuestionnaire(Long trainingRunId, String responsesAsString) {
        TrainingRun trainingRun = findByIdWithPhase(trainingRunId);
        if (!(trainingRun.getCurrentPhase() instanceof QuestionnairePhase)) {
            throw new BadRequestException("Current phase is not questionnaire phase and cannot be evaluated.");
        }
        if (trainingRun.isPhaseAnswered())
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(), trainingRunId,
                    "Current phase of the training run has been already answered."));
        //TODO complete the evaluation
        auditEventsService.auditQuestionnaireAnswersAction(trainingRun, responsesAsString);
        auditEventsService.auditPhaseCompletedAction(trainingRun);
    }
}
