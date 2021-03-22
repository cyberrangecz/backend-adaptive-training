package cz.muni.ics.kypo.training.adaptive.service;

import com.google.common.collect.Lists;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.LinkDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.NodeDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.PreProcessLink;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.SankeyGraphDTO;
import cz.muni.ics.kypo.training.adaptive.repository.ParticipantTaskAssignmentRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TaskRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TrainingPhaseRepository;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisualizationService {

    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;
    private final TaskRepository taskRepository;

    public VisualizationService(ParticipantTaskAssignmentRepository participantTaskAssignmentRepository,
                                TrainingPhaseRepository trainingPhaseRepository,
                                TaskRepository taskRepository) {
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
        this.taskRepository = taskRepository;
    }

    public SankeyGraphDTO getSankeyGraph(TrainingInstance trainingInstance) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingInstance.getTrainingDefinition().getId());
        if (trainingPhases.isEmpty()) {
            return new SankeyGraphDTO();
        }
        List<NodeDTO> nodes = participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        List<PreProcessLink> preProcessedLinks = participantTaskAssignmentRepository.findAllTaskTransitions(trainingInstance.getTrainingDefinition().getId(), trainingInstance.getId());
        List<LinkDTO> resultLinks = new ArrayList<>();

        this.addLinksFromStartNode(preProcessedLinks, resultLinks, nodes);
        this.addLinksToFinishNode(preProcessedLinks, resultLinks, nodes, trainingPhases);

        resultLinks.addAll(preProcessedLinks.stream()
                .map(link -> new LinkDTO(link.getSource(), link.getTarget(), link.getValue()))
                .collect(Collectors.toList()));

        this.addStartAndFinishNode(nodes);
        return new SankeyGraphDTO(nodes, resultLinks);
    }

    private void addLinksFromStartNode(List<PreProcessLink> preProcessedLinks,
                                       List<LinkDTO> resultLinks,
                                       List<NodeDTO> visitedNodes) {
        Long firstPhaseId = visitedNodes.get(0).getPhaseId();
        Integer firstPhaseOrder = visitedNodes.get(0).getPhaseOrder();
        Map<Long, Long> numberOfParticipantsInTaskOfFirstPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(firstPhaseId);
        int nodeIndex = 1;
        for (NodeDTO visitedNode : visitedNodes) {
            if (!visitedNode.getPhaseOrder().equals(firstPhaseOrder)) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTaskOfFirstPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsFinishedTask = preProcessedLinks.stream()
                    .filter(link -> link.getSourceTaskId().equals(visitedNode.getTaskId()))
                    .map(PreProcessLink::getValue)
                    .findFirst().orElseGet(() -> 0L);
            resultLinks.add(new LinkDTO(0, nodeIndex, participantsSolvingTask + participantsFinishedTask));
            nodeIndex++;
        }
    }

    private void addLinksToFinishNode(List<PreProcessLink> preProcessedLinks,
                                      List<LinkDTO> resultLinks,
                                      List<NodeDTO> visitedNodes,
                                      List<TrainingPhase> trainingPhases) {
        int nodeIndex = visitedNodes.size();
        Map<Long, Long> numberOfParticipantsInTaskOfLastPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(trainingPhases.get(trainingPhases.size() - 1).getId());
        for (NodeDTO visitedNode: Lists.reverse(visitedNodes)) {
            if (!visitedNode.getPhaseId().equals(trainingPhases.get(trainingPhases.size() - 1).getId())) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTaskOfLastPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsVisitedTask = preProcessedLinks.stream()
                    .filter(link -> link.getTargetTaskId().equals(visitedNode.getTaskId()))
                    .mapToLong(PreProcessLink::getValue)
                    .sum();
            resultLinks.add(new LinkDTO(nodeIndex, visitedNodes.size() + 1, participantsVisitedTask - participantsSolvingTask));
            nodeIndex--;
        }
        resultLinks.add(new LinkDTO(visitedNodes.size(), visitedNodes.size() + 1, 0L));
    }

    private void addStartAndFinishNode(List<NodeDTO> nodes) {
        nodes.add(0, new NodeDTO(null, null, null, null, -1, null));
        nodes.add(new NodeDTO(null, null, null, null, -2, null));
    }
}
