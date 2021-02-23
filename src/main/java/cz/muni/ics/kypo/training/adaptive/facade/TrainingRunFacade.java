package cz.muni.ics.kypo.training.adaptive.facade;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsOrganizerOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsTrainee;
import cz.muni.ics.kypo.training.adaptive.annotations.security.IsTraineeOrAdmin;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.IsCorrectAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionChoiceDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessedTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.enums.Actions;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.mapping.mapstruct.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.mapstruct.TrainingRunMapper;
import cz.muni.ics.kypo.training.adaptive.service.SecurityService;
import cz.muni.ics.kypo.training.adaptive.service.UserService;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The type Training run facade.
 */
@Service
public class TrainingRunFacade {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingRunFacade.class);

    private TrainingRunService trainingRunService;
    private UserService userService;
    private UserManagementServiceApi userManagementServiceApi;
    private TrainingRunMapper trainingRunMapper;
    private PhaseMapper phaseMapper;


    /**
     * Instantiates a new Training run facade.
     *
     * @param trainingRunService the training run service
     * @param userService        the user service
     * @param trainingRunMapper  the training run mapper
     * @param phaseMapper        the phase mapper
     */
    @Autowired
    public TrainingRunFacade(TrainingRunService trainingRunService,
                             UserService userService,
                             UserManagementServiceApi userManagementServiceApi,
                             TrainingRunMapper trainingRunMapper,
                             PhaseMapper phaseMapper) {
        this.trainingRunService = trainingRunService;
        this.userService = userService;
        this.userManagementServiceApi = userManagementServiceApi;
        this.trainingRunMapper = trainingRunMapper;
        this.phaseMapper = phaseMapper;
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
        trainingRunIds.forEach(trainingRunId -> trainingRunService.deleteTrainingRun(trainingRunId, forceDelete));
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
        trainingRunService.deleteTrainingRun(trainingRunId, forceDelete);
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
            trainingRunService.assignSandbox(trainingRun, trainingInstance.getPoolId());
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
        accessTrainingRunDTO.setAbstractPhaseDTO(getCorrectAbstractPhaseDTO(trainingRun.getCurrentPhase()));
        accessTrainingRunDTO.setShowStepperBar(trainingRun.getTrainingInstance().getTrainingDefinition().isShowStepperBar());
        accessTrainingRunDTO.setInfoAboutPhases(getInfoAboutPhases(trainingRun.getCurrentPhase().getTrainingDefinition().getId()));
        accessTrainingRunDTO.setSandboxInstanceRefId(trainingRun.getSandboxInstanceRefId());
        accessTrainingRunDTO.setInstanceId(trainingRun.getTrainingInstance().getId());
        accessTrainingRunDTO.setStartTime(trainingRun.getStartTime());
        if (trainingRun.getCurrentPhase() instanceof TrainingPhase && trainingRun.isSolutionTaken()) {
            accessTrainingRunDTO.setTakenSolution(trainingRun.getCurrentTask().getSolution());
        }
        return accessTrainingRunDTO;
    }

    private List<BasicPhaseInfoDTO> getInfoAboutPhases(Long definitionId) {
        List<BasicPhaseInfoDTO> infoAboutPhases = new ArrayList<>();
        List<AbstractPhase> phases = trainingRunService.getPhases(definitionId);
        for (AbstractPhase abstractPhase : phases) {
            if (abstractPhase instanceof QuestionnairePhase) {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.QUESTIONNAIRE, abstractPhase.getOrder()));
            } else if (abstractPhase instanceof TrainingPhase) {
                infoAboutPhases.add(new BasicPhaseInfoDTO(abstractPhase.getId(), abstractPhase.getTitle(), PhaseType.TRAINING, abstractPhase.getOrder()));
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
        AbstractPhase abstractPhase;
        abstractPhase = trainingRunService.getNextPhase(trainingRunId);
        return getCorrectAbstractPhaseDTO(abstractPhase);
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
     * Evaluate and store responses to questionnaire.
     *
     * @param trainingRunId     id of Training Run to be finish.
     * @param responsesAsString responses to questionnaire
     */
    @IsTrainee
    @TransactionalWO
    public void evaluateResponsesToQuestionnaire(Long trainingRunId, String responsesAsString) {
        trainingRunService.evaluateResponsesToQuestionnaire(trainingRunId, responsesAsString);
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
        if (sortByTitle != null && !sortByTitle.isBlank()) {
            if (!runs.isEmpty()) {
                if (sortByTitle.equals("asc")) {
                    runs.sort(Comparator.comparing(AccessedTrainingRunDTO::getTitle));
                } else if (sortByTitle.equals("desc")) {
                    runs.sort(Comparator.comparing(AccessedTrainingRunDTO::getTitle).reversed());
                }
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

    private AbstractPhaseDTO getCorrectAbstractPhaseDTO(AbstractPhase abstractPhase) {
        AbstractPhaseDTO abstractPhaseDTO;
        if (abstractPhase instanceof QuestionnairePhase) {
            QuestionnairePhase questionnairePhase = (QuestionnairePhase) abstractPhase;
            abstractPhaseDTO = phaseMapper.mapToDTO(questionnairePhase);
            abstractPhaseDTO.setPhaseType(PhaseType.QUESTIONNAIRE);
            deleteInfoAboutCorrectnessFromQuestions((QuestionnairePhaseDTO) abstractPhaseDTO);
        } else if (abstractPhase instanceof TrainingPhase) {
            TrainingPhase trainingPhase = (TrainingPhase) abstractPhase;
            abstractPhaseDTO = phaseMapper.mapToDTO(trainingPhase);
            //TODO change the DTO with one task and remove the correct answer
            abstractPhaseDTO.setPhaseType(PhaseType.TRAINING);
        } else {
            InfoPhase infoPhase = (InfoPhase) abstractPhase;
            abstractPhaseDTO = phaseMapper.mapToDTO(infoPhase);
            abstractPhaseDTO.setPhaseType(PhaseType.INFO);
        }
        return abstractPhaseDTO;
    }

    private void deleteInfoAboutCorrectnessFromQuestions(QuestionnairePhaseDTO questionnairePhaseDTO) {
        for (QuestionDTO questionDTO : questionnairePhaseDTO.getQuestions()) {
            for (QuestionChoiceDTO questionChoiceDTO : questionDTO.getChoices()) {
                questionChoiceDTO.setCorrect(null);
            }
        }
    }
}
