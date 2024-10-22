package cz.cyberrange.platform.training.adaptive.rest.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security.IsTrainee;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalRO;
import cz.cyberrange.platform.training.adaptive.persistence.entity.ParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.MitreTechnique;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.CommandDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnairePhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.PageResultResource;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionMitreTechniquesDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions.TrainingRunDataDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions.TrainingRunPathNode;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions.TransitionsDataDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import cz.cyberrange.platform.training.adaptive.api.mapping.PhaseMapper;
import cz.cyberrange.platform.training.adaptive.service.VisualizationService;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import cz.cyberrange.platform.training.adaptive.service.phases.PhaseService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingInstanceService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingRunService;
import cz.cyberrange.platform.training.adaptive.utils.AbstractCommandPrefixes;
import cz.cyberrange.platform.training.adaptive.utils.ElasticSearchCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional
public class VisualizationFacade {

    private final VisualizationService visualizationService;
    private final TrainingDefinitionService trainingDefinitionService;
    private final TrainingInstanceService trainingInstanceService;
    private final TrainingRunService trainingRunService;
    private final UserManagementServiceApi userManagementServiceApi;
    private final PhaseService phaseService;
    private final PhaseMapper phaseMapper;
    private final ObjectMapper objectMapper;

    public VisualizationFacade(VisualizationService visualizationService,
                               TrainingDefinitionService trainingDefinitionService,
                               TrainingInstanceService trainingInstanceService,
                               TrainingRunService trainingRunService,
                               UserManagementServiceApi userManagementServiceApi,
                               PhaseService phaseService,
                               PhaseMapper phaseMapper, ObjectMapper objectMapper) {
        this.visualizationService = visualizationService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.trainingInstanceService = trainingInstanceService;
        this.trainingRunService = trainingRunService;
        this.userManagementServiceApi = userManagementServiceApi;
        this.phaseService = phaseService;
        this.phaseMapper = phaseMapper;
        this.objectMapper = objectMapper;
    }

    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingInstance(#trainingInstanceId)")
    @Transactional(readOnly = true)
    public SankeyDiagramDTO getSankeyDiagram(Long trainingInstanceId) {
        TrainingInstance trainingInstance = trainingInstanceService.findById(trainingInstanceId);
        return visualizationService.getSankeyDiagram(trainingInstance);
    }


    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingInstance(#instanceId)")
    @TransactionalRO
    public TransitionsDataDTO getTransitionsGraphForOrganizer(Long instanceId) {
        TrainingInstance trainingInstance = trainingInstanceService.findById(instanceId);
        return getTransitionsGraph(trainingInstance, () -> visualizationService.getParticipantTaskAssignmentsByTrainingInstance(instanceId));
    }

    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isTraineeOfFinishedTrainingRun(#runId)")
    @TransactionalRO
    public TransitionsDataDTO getTransitionsGraphForTrainee(Long runId) {
        TrainingInstance trainingInstance = trainingRunService.findById(runId).getTrainingInstance();
        TransitionsDataDTO transitionsData = getTransitionsGraph(trainingInstance, () -> Map.of(runId, visualizationService.getParticipantTaskAssignmentsByTrainingRun(runId)));
        removeCorrectnessFromTransitionsDataOfTrainee(transitionsData);
        return transitionsData;
    }

    /**
     * Gather all summarized data about mitre techniques used in training definitions.
     *
     * @return training definitions with mitre techniques
     */
    @IsTrainee
    @TransactionalRO
    public List<TrainingDefinitionMitreTechniquesDTO> getTrainingDefinitionsWithMitreTechniques() {
        List<TrainingDefinition> trainingDefinitions = trainingDefinitionService.findAllByState(TDState.RELEASED, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        List<TrainingPhase> trainingPhases = visualizationService.getAllTrainingPhases();
        UserRefDTO userRefDTO = userManagementServiceApi.getUserRefDTO();
        Set<Long> playedDefinitionIds = trainingDefinitionService.findAllPlayedByUser(userRefDTO.getUserRefId()).stream()
                .map(TrainingDefinition::getId)
                .collect(Collectors.toSet());

        List<TrainingDefinitionMitreTechniquesDTO> result = new ArrayList<>();

        for (TrainingDefinition trainingDefinition: trainingDefinitions) {
            List<TrainingPhase> trainingPhasesOfDefinition = visualizationService.getTrainingPhasesByTrainingDefinitionId(trainingDefinition.getId());
            TrainingDefinitionMitreTechniquesDTO definitionMitreTechniquesDTO = new TrainingDefinitionMitreTechniquesDTO();
            definitionMitreTechniquesDTO.setId(trainingDefinition.getId());
            definitionMitreTechniquesDTO.setTitle(trainingDefinition.getTitle());
            Set<String> techniques = trainingPhasesOfDefinition.stream()
                    .flatMap(trainingLevel -> trainingLevel.getMitreTechniques().stream().map(MitreTechnique::getTechniqueKey))
                    .collect(Collectors.toSet());
            definitionMitreTechniquesDTO.setMitreTechniques(techniques);
            definitionMitreTechniquesDTO.setPlayed(playedDefinitionIds.contains(trainingDefinition.getId()));
            result.add(definitionMitreTechniquesDTO);
        }
        return result;
    }

    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingRun(#runId) " +
            "or @securityService.isTraineeOfGivenTrainingRun(#runId)")
    @TransactionalRO
    public List<CommandDTO> getAllCommandsInTrainingRun(Long runId) {
        TrainingRun trainingRun = trainingRunService.findById(runId);
        List<Map<String, Object>> runCommands = trainingRunService.getCommandsByTrainingRun(runId);
        return elasticCommandsToCommandDTOlist(runCommands, trainingRun.getStartTime());
    }

    private List<CommandDTO> elasticCommandsToCommandDTOlist(List<Map<String, Object>> elasticCommands, LocalDateTime runStartTime) {
        List<CommandDTO> commandDTOS = new ArrayList<>(elasticCommands.size());
        elasticCommands.stream()
                .map(elasticCommand -> objectMapper.convertValue(elasticCommand, ElasticSearchCommand.class))
                .forEach(elasticCommand -> commandDTOS.add(elasticCommandToCommandDTO(elasticCommand, runStartTime)));
        return commandDTOS;
    }

    private CommandDTO elasticCommandToCommandDTO(ElasticSearchCommand elasticSearchCommand, LocalDateTime runStartTime) {
        String[] commandSplit =  elasticSearchCommand.getCmd().split(" ", 2);
        String command = commandSplit[0];
        if (AbstractCommandPrefixes.isPrefix(command)) {
            commandSplit = commandSplit[1].split(" ", 2);
            command += " " + commandSplit[0];
        }

        // if there were no options, the option string should be null
        String options = null;
        if (commandSplit.length == 2) {
            options = commandSplit[1];
        }
        String timestampString = elasticSearchCommand.getTimestampStr();
        LocalDateTime commandTimestamp = ZonedDateTime.parse(timestampString).toLocalDateTime();

        return CommandDTO.builder()
                .commandType(elasticSearchCommand.getCmdType())
                .cmd(command)
                .timestamp(commandTimestamp)
                .trainingTime(Duration.between(runStartTime, commandTimestamp))
                .fromHostIp(elasticSearchCommand.getIp())
                .options(options)
                .build();
    }




    private void removeCorrectnessFromTransitionsDataOfTrainee(TransitionsDataDTO transitionsDataDTO) {
        Map<Long, Long> visitedTasksByPhaseId = transitionsDataDTO.getTrainingRunsData().get(0).getTrainingRunPathNodes().stream()
                .collect(Collectors.toMap(TrainingRunPathNode::getPhaseId, TrainingRunPathNode::getTaskId));
        for (AbstractPhaseDTO phase : transitionsDataDTO.getPhases()) {
            if (phase.getPhaseType() == PhaseType.QUESTIONNAIRE) {
                ((QuestionnairePhaseDTO) phase).getQuestions().forEach(question -> question.getChoices().forEach(choice -> choice.setCorrect(null)));
            } else if (phase.getPhaseType() == PhaseType.TRAINING) {
                ((TrainingPhaseDTO) phase).getTasks().stream()
                        .filter(task -> !task.getId().equals(visitedTasksByPhaseId.get(phase.getId())))
                        .forEach(task -> {
                            task.setSolution(null);
                            task.setAnswer(null);
                        });
            }
        }
    }

    private TransitionsDataDTO getTransitionsGraph(TrainingInstance trainingInstance,
                                                   Supplier<Map<Long, List<ParticipantTaskAssignment>>> getTaskAssignmentFunction) {
        List<AbstractPhase> abstractPhases = phaseService.getPhases(trainingInstance.getTrainingDefinition().getId());
        Map<Long, List<ParticipantTaskAssignment>> assignmentsByTrainingRun = getTaskAssignmentFunction.get();
        Map<Long, UserRefDTO> trainees = getAllUsersRefsByGivenUsersIds(getTraineeIds(assignmentsByTrainingRun)).stream()
                .collect(Collectors.toMap(UserRefDTO::getUserRefId, Function.identity()));

        TransitionsDataDTO resultData = new TransitionsDataDTO();
        resultData.setTrainingRunsData(getTrainingRunsData(assignmentsByTrainingRun.entrySet(), trainees, trainingInstance.isLocalEnvironment()));
        resultData.setPhases(getAbstractPhasesDTO(abstractPhases));
        return resultData;
    }

    private List<TrainingRunDataDTO> getTrainingRunsData(Set<Map.Entry<Long, List<ParticipantTaskAssignment>>> assignmentsEntries,
                                                         Map<Long, UserRefDTO> trainees, boolean localEnvironment) {
        return assignmentsEntries.stream().map(entry -> {
            TrainingRunDataDTO trainingRunDataDTO = new TrainingRunDataDTO();
            trainingRunDataDTO.setTrainingRunId(entry.getKey());
            trainingRunDataDTO.setTrainee(trainees.get(entry.getValue().get(0).getTrainingRun().getParticipantRef().getUserRefId()));
            trainingRunDataDTO.setTrainingRunPathNodes(getPathNodes(entry.getValue()));
            trainingRunDataDTO.setLocalEnvironment(localEnvironment);
            return trainingRunDataDTO;
        }).collect(Collectors.toList());
    }

    private List<AbstractPhaseDTO> getAbstractPhasesDTO(List<AbstractPhase> abstractPhases) {
        return abstractPhases.stream().map(abstractPhase -> {
            AbstractPhaseDTO abstractPhaseDTO = phaseMapper.mapToDTO(abstractPhase);
            removeUnnecessaryFields(abstractPhaseDTO);
            return abstractPhaseDTO;
        }).collect(Collectors.toList());
    }

    private List<UserRefDTO> getAllUsersRefsByGivenUsersIds(List<Long> participantsRefIds) {
        List<UserRefDTO> users = new ArrayList<>();
        PageResultResource<UserRefDTO> usersPageResultResource;
        int page = 0;
        do {
            usersPageResultResource = userManagementServiceApi.getUserRefDTOsByUserIds(participantsRefIds, PageRequest.of(page, 999), null, null);
            users.addAll(usersPageResultResource.getContent());
            page++;
        }
        while (page < usersPageResultResource.getPagination().getTotalPages());
        return users;
    }

    private List<Long> getTraineeIds(Map<Long, List<ParticipantTaskAssignment>> assignmentsByTrainingRun) {
        return assignmentsByTrainingRun.values().stream()
                .map(taskAssignments -> taskAssignments.get(0).getTrainingRun().getParticipantRef().getUserRefId())
                .toList();
    }

    private void removeUnnecessaryFields(AbstractPhaseDTO abstractPhaseDTO) {
        if (abstractPhaseDTO.getClass() == TrainingPhaseDTO.class) {
            ((TrainingPhaseDTO) abstractPhaseDTO).setDecisionMatrix(null);
            ((TrainingPhaseDTO) abstractPhaseDTO).setRelatedQuestions(null);
        } else if (abstractPhaseDTO.getClass() == QuestionnairePhaseDTO.class) {
            ((QuestionnairePhaseDTO) abstractPhaseDTO).setPhaseRelations(null);
        }
    }

    private List<TrainingRunPathNode> getPathNodes(List<ParticipantTaskAssignment> participantTaskAssignments) {
        return participantTaskAssignments.stream()
                .map(assignment -> {
                    TrainingRunPathNode trainingRunPathNode = new TrainingRunPathNode();
                    trainingRunPathNode.setPhaseId(assignment.getAbstractPhase().getId());
                    trainingRunPathNode.setPhaseOrder(assignment.getAbstractPhase().getOrder());
                    trainingRunPathNode.setTaskId(assignment.getTask() == null ? 0L : assignment.getTask().getId());
                    trainingRunPathNode.setTaskOrder(assignment.getTask() == null ? 0 : assignment.getTask().getOrder());
                    return trainingRunPathNode;
                }).sorted(Comparator.comparing(TrainingRunPathNode::getPhaseOrder))
                .collect(Collectors.toList());
    }
}
