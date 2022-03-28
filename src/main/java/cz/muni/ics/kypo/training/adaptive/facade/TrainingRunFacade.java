package cz.muni.ics.kypo.training.adaptive.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsOrganizerOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsTrainee;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsTraineeOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.*;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseViewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseAnswersDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessedTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.enums.Actions;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.mapping.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.SubmissionMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.TrainingRunMapper;
import cz.muni.ics.kypo.training.adaptive.service.QuestionnaireEvaluationService;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingRunService;
import cz.muni.ics.kypo.training.adaptive.utils.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Training run facade.
 */
@Service
public class TrainingRunFacade {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRunFacade.class);

    @Value("${central.syslog.ip:127.0.0.1}")
    private String centralSyslogIp;

    private final TrainingRunService trainingRunService;
    private final UserManagementServiceApi userManagementServiceApi;
    private final TrainingRunMapper trainingRunMapper;
    private final PhaseMapper phaseMapper;
    private final SubmissionMapper submissionMapper;
    private final QuestionnaireEvaluationService questionnaireEvaluationService;

    /**
     * Instantiates a new Training run facade.
     *
     * @param trainingRunService the training run service
     * @param trainingRunMapper  the training run mapper
     * @param phaseMapper        the phase mapper
     */
    @Autowired
    public TrainingRunFacade(TrainingRunService trainingRunService,
                             QuestionnaireEvaluationService questionnaireEvaluationService,
                             UserManagementServiceApi userManagementServiceApi,
                             TrainingRunMapper trainingRunMapper,
                             PhaseMapper phaseMapper,
                             SubmissionMapper submissionMapper) {
        this.trainingRunService = trainingRunService;
        this.questionnaireEvaluationService = questionnaireEvaluationService;
        this.userManagementServiceApi = userManagementServiceApi;
        this.trainingRunMapper = trainingRunMapper;
        this.phaseMapper = phaseMapper;
        this.submissionMapper = submissionMapper;
    }

    /**
     * Finds specific Training Run by id
     *
     * @param id of a Training Run that would be returned
     * @return specific {@link TrainingRunByIdDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#id)")
    @TransactionalRO
    public TrainingRunByIdDTO findById(Long id) {
        TrainingRun trainingRun = trainingRunService.findById(id);
        TrainingRunByIdDTO trainingRunByIdDTO = trainingRunMapper.mapToFindByIdDTO(trainingRun);
        trainingRunByIdDTO.setDefinitionId(trainingRun.getTrainingInstance().getTrainingDefinition().getId());
        trainingRunByIdDTO.setInstanceId(trainingRun.getTrainingInstance().getId());
        trainingRunByIdDTO.setParticipantRef(userManagementServiceApi.getUserRefDTOByUserRefId(trainingRunByIdDTO.getParticipantRef().getUserRefId()));
        return trainingRunByIdDTO;
    }

    /**
     * Find all Training Runs.
     *
     * @param predicate specifies query to the database.
     * @param pageable  pageable parameter with information about pagination.
     * @return page of all {@link TrainingRunDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)")
    @TransactionalRO
    public PageResultResource<TrainingRunDTO> findAll(Predicate predicate, Pageable pageable) {
        PageResultResource<TrainingRunDTO> trainingRunDTOPageResultResource = trainingRunMapper.mapToPageResultResource(trainingRunService.findAll(predicate, pageable));
        addParticipantsToTrainingRunDTOs(trainingRunDTOPageResultResource.getContent());
        return trainingRunDTOPageResultResource;
    }

    /**
     * Delete selected training runs.
     *
     * @param trainingRunIds training runs to delete
     * @param forceDelete    indicates if this training run should be force deleted.
     */
    @IsOrganizerOrAdmin
    @TransactionalWO
    public void deleteTrainingRuns(List<Long> trainingRunIds, boolean forceDelete) {
        trainingRunIds.forEach(trainingRunId -> trainingRunService.deleteTrainingRun(trainingRunId, forceDelete, true));
    }

    /**
     * Delete selected training run.
     *
     * @param trainingRunId training run to delete
     * @param forceDelete   indicates if this training run should be force deleted.
     */
    @IsOrganizerOrAdmin
    @TransactionalWO
    public void deleteTrainingRun(Long trainingRunId, boolean forceDelete) {
        trainingRunService.deleteTrainingRun(trainingRunId, forceDelete, true);
    }

    /**
     * Finds all Training Runs of logged in user.
     *
     * @param pageable    pageable parameter with information about pagination.
     * @param sortByTitle optional parameter. "asc" for ascending sort, "desc" for descending and null if sort is not wanted
     * @return Page of all {@link AccessedTrainingRunDTO} of logged in user.
     */
    @IsTraineeOrAdmin
    @TransactionalRO
    public PageResultResource<AccessedTrainingRunDTO> findAllAccessedTrainingRuns(Pageable pageable, String sortByTitle) {
        Page<TrainingRun> trainingRuns = trainingRunService.findAllByParticipantRefUserRefId(pageable);
        return convertToAccessedRunDTO(trainingRuns, sortByTitle);
    }

    /**
     * Resume given training run.
     *
     * @param trainingRunId id of Training Run to be resumed.
     * @return {@link AccessTrainingRunDTO} response
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public AccessTrainingRunDTO resumeTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = trainingRunService.resumeTrainingRun(trainingRunId);
        return convertToAccessTrainingRunDTO(trainingRun);
    }

    /**
     * Access Training Run by logged in user based on given accessToken.
     *
     * @param accessToken of one training instance
     * @return {@link AccessTrainingRunDTO} response
     */
    @IsTraineeOrAdmin
    @Transactional
    public AccessTrainingRunDTO accessTrainingRun(String accessToken) {
        TrainingInstance trainingInstance = trainingRunService.getTrainingInstanceForParticularAccessToken(accessToken);
        // checking if the user is not accessing to his existing training run (resume action)
        Long participantRefId = userManagementServiceApi.getLoggedInUserRefId();
        Optional<TrainingRun> accessedTrainingRun = trainingRunService.findRunningTrainingRunOfUser(accessToken, participantRefId);
        if (accessedTrainingRun.isPresent()) {
            return convertToAccessTrainingRunDTO(trainingRunService.resumeTrainingRun(accessedTrainingRun.get().getId()));
        }
        // Check if the user already clicked access training run, in that case, it returns an exception (it prevents concurrent accesses).
        trainingRunService.trAcquisitionLockToPreventManyRequestsFromSameUser(participantRefId, trainingInstance.getId(), accessToken);
        try {
            // During this action we create a new TrainingRun and lock and get sandbox from OpenStack Sandbox API
            TrainingRun trainingRun = trainingRunService.createTrainingRun(trainingInstance, participantRefId);
            if (!trainingInstance.isLocalEnvironment()) {
                trainingRunService.assignSandbox(trainingRun, trainingInstance.getPoolId());
            }
            trainingRunService.auditTrainingRunStarted(trainingRun);
            return convertToAccessTrainingRunDTO(trainingRun);
        } catch (Exception e) {
            // delete/rollback acquisition lock when no training run either sandbox is assigned
            trainingRunService.deleteTrAcquisitionLockToPreventManyRequestsFromSameUser(participantRefId, trainingInstance.getId());
            throw e;
        }
    }

    private AccessTrainingRunDTO convertToAccessTrainingRunDTO(TrainingRun trainingRun) {
        AccessTrainingRunDTO accessTrainingRunDTO = new AccessTrainingRunDTO();
        accessTrainingRunDTO.setTrainingRunID(trainingRun.getId());
        accessTrainingRunDTO.setCurrentPhase(getCorrectAbstractPhaseDTO(trainingRun.getCurrentPhase(), trainingRun.getCurrentTask()));
        accessTrainingRunDTO.setShowStepperBar(trainingRun.getTrainingInstance().getTrainingDefinition().isShowStepperBar());
        accessTrainingRunDTO.setInfoAboutPhases(getInfoAboutPhases(trainingRun.getCurrentPhase().getTrainingDefinition().getId()));
        accessTrainingRunDTO.setSandboxInstanceRefId(trainingRun.getSandboxInstanceRefId());
        accessTrainingRunDTO.setInstanceId(trainingRun.getTrainingInstance().getId());
        accessTrainingRunDTO.setStartTime(trainingRun.getStartTime());
        accessTrainingRunDTO.setLocalEnvironment(trainingRun.getTrainingInstance().isLocalEnvironment());
        accessTrainingRunDTO.setSandboxDefinitionId(trainingRun.getTrainingInstance().getSandboxDefinitionId());
        if (trainingRun.getCurrentPhase() instanceof TrainingPhase && trainingRun.isSolutionTaken()) {
            accessTrainingRunDTO.setTakenSolution(trainingRun.getCurrentTask().getSolution());
        }
        if(trainingRun.getCurrentPhase().getClass() == AccessPhase.class) {
            replacePlaceholders(
                    (AccessPhaseViewDTO) accessTrainingRunDTO.getCurrentPhase(),
                    trainingRun.getTrainingInstance().getAccessToken(),
                    trainingRun.getParticipantRef().getUserRefId()
            );
        }
        return accessTrainingRunDTO;
    }

    private void replacePlaceholders(AccessPhaseViewDTO accessPhaseViewDTO, String accessToken, Long userId) {
        String localContent = accessPhaseViewDTO.getLocalContent();
        localContent = localContent.replaceAll("\\$\\{ACCESS_TOKEN\\}", accessToken);
        localContent = localContent.replaceAll("\\$\\{USER_ID\\}", userId.toString());
        localContent = localContent.replaceAll("\\$\\{CENTRAL_SYSLOG_IP\\}", centralSyslogIp);
        accessPhaseViewDTO.setLocalContent(localContent);
    }

    private List<BasicPhaseInfoDTO> getInfoAboutPhases(Long definitionId) {
        List<BasicPhaseInfoDTO> infoAboutPhases = new ArrayList<>();
        List<AbstractPhase> phases = trainingRunService.getPhases(definitionId);
        for (AbstractPhase abstractPhase : phases) {
            if (abstractPhase instanceof QuestionnairePhase) {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.QUESTIONNAIRE, abstractPhase.getOrder()));
            } else if (abstractPhase instanceof TrainingPhase) {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.TRAINING, abstractPhase.getOrder()));
            } else if (abstractPhase instanceof AccessPhase) {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.ACCESS, abstractPhase.getOrder()));
            } else {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.INFO, abstractPhase.getOrder()));
            }
        }
        return infoAboutPhases;
    }

    /**
     * Finds all Training Runs by specific Training Definition and logged in user.
     *
     * @param trainingDefinitionId id of Training Definition
     * @param pageable             pageable parameter with information about pagination.
     * @return Page of all {@link AccessedTrainingRunDTO} of logged in user and given definition.
     */
    @IsTrainee
    @TransactionalRO
    public PageResultResource<TrainingRunDTO> findAllByTrainingDefinitionAndParticipant(Long trainingDefinitionId, Pageable pageable) {
        Page<TrainingRun> trainingRuns = trainingRunService.findAllByTrainingDefinitionAndParticipant(trainingDefinitionId, pageable);
        PageResultResource<TrainingRunDTO> trainingRunDTOPageResultResource = trainingRunMapper.mapToPageResultResource(trainingRuns);
        addParticipantsToTrainingRunDTOs(trainingRunDTOPageResultResource.getContent());
        return trainingRunDTOPageResultResource;
    }

    /**
     * Finds all Training Runs of specific training definition.
     *
     * @param trainingDefinitionId id of Training Definition whose Training Runs would be returned.
     * @param pageable             pageable parameter with information about pagination.
     * @return Page of all {@link AccessedTrainingRunDTO} of given definition.
     */
    @IsTrainee
    @TransactionalRO
    public PageResultResource<TrainingRunDTO> findAllByTrainingDefinition(Long trainingDefinitionId, Pageable pageable) {
        Page<TrainingRun> trainingRuns = trainingRunService.findAllByTrainingDefinition(trainingDefinitionId, pageable);
        PageResultResource<TrainingRunDTO> trainingRunDTOPageResultResource = trainingRunMapper.mapToPageResultResource(trainingRuns);
        addParticipantsToTrainingRunDTOs(trainingRunDTOPageResultResource.getContent());
        return trainingRunDTOPageResultResource;
    }

    /**
     * Gets next phase of given Training Run and set new current phase.
     *
     * @param trainingRunId id of Training Run whose next phase should be returned.
     * @return {@link AbstractPhaseDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public AbstractPhaseDTO getNextPhase(Long trainingRunId) {
        TrainingRun trainingRun = trainingRunService.moveToNextPhase(trainingRunId);
        AbstractPhaseDTO abstractPhaseDTO = getCorrectAbstractPhaseDTO(trainingRun.getCurrentPhase(), trainingRun.getCurrentTask());
        if (abstractPhaseDTO.getClass() == AccessPhaseViewDTO.class) {
            replacePlaceholders(
                    (AccessPhaseViewDTO) abstractPhaseDTO,
                    trainingRun.getTrainingInstance().getAccessToken(),
                    trainingRun.getParticipantRef().getUserRefId()
            );
        }
        return abstractPhaseDTO;
    }

    /**
     * Gets solution of current phase of given Training Run.
     *
     * @param trainingRunId id of Training Run which current phase gets solution for.
     * @return solution of current phase.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public String getSolution(Long trainingRunId) {
        return trainingRunService.getSolution(trainingRunId);
    }

    /**
     * Check given answer of given Training Run.
     *
     * @param trainingRunId id of Training Run to check answer.
     * @param answer        string which player submit.
     * @return true if answer is correct, false if answer is wrong.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public IsCorrectAnswerDTO isCorrectAnswer(Long trainingRunId, String answer) {
        IsCorrectAnswerDTO correctAnswerDTO = new IsCorrectAnswerDTO();
        correctAnswerDTO.setCorrect(trainingRunService.isCorrectAnswer(trainingRunId, answer));
        correctAnswerDTO.setRemainingAttempts(trainingRunService.getRemainingAttempts(trainingRunId));
        if (correctAnswerDTO.getRemainingAttempts() == 0) {
            correctAnswerDTO.setSolution(getSolution(trainingRunId));
        }
        return correctAnswerDTO;
    }

    /**
     * Check given passkey of given Training Run.
     *
     * @param trainingRunId id of Training Run to check passkey.
     * @param passkey        string which player submit.
     * @return true if passkey is correct, false if passkey is wrong.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public Boolean isCorrectPasskey(Long trainingRunId, String passkey) {
        return trainingRunService.isCorrectPasskey(trainingRunId, passkey);
    }

    /**
     * Finish training run.
     *
     * @param trainingRunId id of Training Run to be finished.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public void finishTrainingRun(Long trainingRunId) {
        trainingRunService.finishTrainingRun(trainingRunId);
    }

    /**
     * Archive training run.
     *
     * @param trainingRunId id of Training Run to be archived.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public void archiveTrainingRun(Long trainingRunId) {
        trainingRunService.archiveTrainingRun(trainingRunId);
    }

    /**
     * Evaluate and store answers to questionnaire.
     *
     * @param trainingRunId                id of the Training Run.
     * @param questionnairePhaseAnswersDTO answers to questionnaire
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalWO
    public void evaluateAnswersToQuestionnaire(Long trainingRunId, QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO) {
        Map<Long, Set<String>> questionsAnswersMapping = questionnairePhaseAnswersDTO.getAnswers().stream()
                .collect(Collectors.toMap(QuestionAnswerDTO::getQuestionId, QuestionAnswerDTO::getAnswers));
        questionnaireEvaluationService.saveAndEvaluateAnswersToQuestionnaire(trainingRunId, questionsAnswersMapping);
    }

    /**
     * Retrieve info about the participant of the given training run.
     *
     * @param trainingRunId id of the training run whose participant you want to get.
     * @return returns participant of the given training run.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#trainingRunId)")
    @TransactionalRO
    public UserRefDTO getParticipant(Long trainingRunId) {
        TrainingRun trainingRun = trainingRunService.findById(trainingRunId);
        return userManagementServiceApi.getUserRefDTOByUserRefId(trainingRun.getParticipantRef().getUserRefId());
    }

    /**
     * Retrieve trainees submissions.
     *
     * @param runId ID of the training run.
     * @param phaseId ID of the phase to specify subset of submissions.
     * @return returns submissions of the given training run.
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfGivenTrainingRun(#runId) or @securityService.isOrganizerOfGivenTrainingRun(#runId)")
    @TransactionalRO
    public List<SubmissionDTO> getTraineesSubmissions(Long runId, Long phaseId) {
        return submissionMapper.mapToSubmissionListDTO(trainingRunService.findTraineesSubmissions(runId, phaseId));
    }

    private void addParticipantsToTrainingRunDTOs(List<TrainingRunDTO> trainingRunDTOS) {
        trainingRunDTOS.forEach(trainingRunDTO ->
                trainingRunDTO.setParticipantRef(userManagementServiceApi.getUserRefDTOByUserRefId(trainingRunDTO.getParticipantRef().getUserRefId())));
    }

    private PageResultResource<AccessedTrainingRunDTO> convertToAccessedRunDTO(Page<TrainingRun> trainingRuns, String sortByTitle) {
        List<AccessedTrainingRunDTO> accessedTrainingRunDTOS = new ArrayList<>();
        for (TrainingRun trainingRun : trainingRuns) {
            AccessedTrainingRunDTO accessedTrainingRunDTO = generateAccessedTrainingRunDTO(trainingRun);
            accessedTrainingRunDTOS.add(accessedTrainingRunDTO);
        }
        return new PageResultResource<>(sortByTitle(accessedTrainingRunDTOS, sortByTitle), trainingRunMapper.createPagination(trainingRuns));
    }

    private List<AccessedTrainingRunDTO> sortByTitle(List<AccessedTrainingRunDTO> runs, String sortByTitle) {
        if (sortByTitle != null && !sortByTitle.isBlank() && !runs.isEmpty()) {
            if (sortByTitle.equals(Sort.ASC)) {
                runs.sort(Comparator.comparing(AccessedTrainingRunDTO::getTitle));
            } else if (sortByTitle.equals(Sort.DESC)) {
                runs.sort(Comparator.comparing(AccessedTrainingRunDTO::getTitle).reversed());
            }
        }
        return runs;
    }

    private AccessedTrainingRunDTO generateAccessedTrainingRunDTO(TrainingRun trainingRun) {
        AccessedTrainingRunDTO accessedTrainingRunDTO = new AccessedTrainingRunDTO();
        accessedTrainingRunDTO.setId(trainingRun.getId());
        accessedTrainingRunDTO.setTitle(trainingRun.getTrainingInstance().getTitle());
        accessedTrainingRunDTO.setTrainingInstanceStartDate(trainingRun.getTrainingInstance().getStartTime());
        accessedTrainingRunDTO.setTrainingInstanceEndDate(trainingRun.getTrainingInstance().getEndTime());
        accessedTrainingRunDTO.setInstanceId(trainingRun.getTrainingInstance().getId());
        accessedTrainingRunDTO.setNumberOfPhases(trainingRunService.getMaxPhaseOrder(trainingRun.getTrainingInstance().getTrainingDefinition().getId()) + 1);
        accessedTrainingRunDTO.setCurrentPhaseOrder(trainingRun.getCurrentPhase().getOrder() + 1);
        accessedTrainingRunDTO.setPossibleAction(resolvePossibleActions(accessedTrainingRunDTO, trainingRun.isPhaseAnswered()));
        return accessedTrainingRunDTO;
    }

    private Actions resolvePossibleActions(AccessedTrainingRunDTO trainingRunDTO, boolean isCurrentPhaseAnswered) {
        boolean isTrainingRunFinished = isCurrentPhaseAnswered && trainingRunDTO.getCurrentPhaseOrder() == trainingRunDTO.getNumberOfPhases();
        boolean isTrainingInstanceRunning = LocalDateTime.now(Clock.systemUTC()).isBefore(trainingRunDTO.getTrainingInstanceEndDate());
        if (isTrainingRunFinished || !isTrainingInstanceRunning) {
            return Actions.RESULTS;
        } else {
            return Actions.RESUME;
        }
    }

    private AbstractPhaseDTO getCorrectAbstractPhaseDTO(AbstractPhase abstractPhase, Task task) {
        if (abstractPhase instanceof QuestionnairePhase) {
            return phaseMapper.mapToQuestionnairePhaseViewDTO((QuestionnairePhase) abstractPhase);
        } else if (abstractPhase instanceof TrainingPhase) {
            return phaseMapper.mapToTrainingPhaseViewDTO((TrainingPhase) abstractPhase, task);
        } else if (abstractPhase instanceof AccessPhase) {
            return phaseMapper.mapToAccessPhaseViewDTO((AccessPhase) abstractPhase);
        } else {
            return phaseMapper.mapToInfoPhaseDTO((InfoPhase) abstractPhase);
        }
    }
}
