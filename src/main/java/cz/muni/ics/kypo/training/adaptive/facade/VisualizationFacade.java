package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.annotations.security.IsTrainee;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.MitreTechnique;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionMitreTechniquesDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.transitions.TrainingRunDataDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.transitions.TrainingRunPathNode;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.transitions.TransitionsDataDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.mapping.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.service.VisualizationService;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingInstanceService;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingRunService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public VisualizationFacade(VisualizationService visualizationService,
                               TrainingDefinitionService trainingDefinitionService,
                               TrainingInstanceService trainingInstanceService,
                               TrainingRunService trainingRunService,
                               UserManagementServiceApi userManagementServiceApi,
                               PhaseService phaseService,
                               PhaseMapper phaseMapper) {
        this.visualizationService = visualizationService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.trainingInstanceService = trainingInstanceService;
        this.trainingRunService = trainingRunService;
        this.userManagementServiceApi = userManagementServiceApi;
        this.phaseService = phaseService;
        this.phaseMapper = phaseMapper;
    }

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingInstance(#trainingInstanceId)")
    @Transactional(readOnly = true)
    public SankeyDiagramDTO getSankeyDiagram(Long trainingInstanceId) {
        TrainingInstance trainingInstance = trainingInstanceService.findById(trainingInstanceId);
        return visualizationService.getSankeyDiagram(trainingInstance);
    }


    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingInstance(#instanceId)")
    @TransactionalRO
    public TransitionsDataDTO getTransitionsGraphForOrganizer(Long instanceId) {
        TrainingInstance trainingInstance = trainingInstanceService.findById(instanceId);
        return getTransitionsGraph(trainingInstance, () -> visualizationService.getParticipantTaskAssignmentsByTrainingInstance(instanceId));
    }

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
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

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingRun(#runId) " +
            "or @securityService.isTraineeOfGivenTrainingRun(#runId)")
    @TransactionalRO
    public List<Map<String, Object>> getAllCommandsInTrainingRun(Long runId) {
        return trainingRunService.getCommandsByTrainingRun(runId);
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
        Map<Long, UserRefDTO> trainees = userManagementServiceApi.getUserRefDTOsByUserIds(getTraineeIds(assignmentsByTrainingRun.entrySet()), PageRequest.of(0, 999), null, null)
                .getContent()
                .stream()
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

    private Set<Long> getTraineeIds(Set<Map.Entry<Long, List<ParticipantTaskAssignment>>> assignmentsEntries) {
        return assignmentsEntries.stream()
                .map(entry -> entry.getValue().get(0).getTrainingRun().getParticipantRef().getUserRefId())
                .collect(Collectors.toSet());
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
