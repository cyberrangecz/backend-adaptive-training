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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * The Visualization service for adaptive training.
 */
@Service
public class VisualizationService {

    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;

    /**
     * Instantiates a new Visualization service.
     *
     * @param participantTaskAssignmentRepository the participant task assignment repository
     * @param trainingPhaseRepository             the training phase repository
     */
    public VisualizationService(ParticipantTaskAssignmentRepository participantTaskAssignmentRepository,
                                TrainingPhaseRepository trainingPhaseRepository) {
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
    }


    /**
     * Gets sankey graph. To visualize sankey graph, the list of nodes and links must be defined.
     *
     * @param trainingInstance the training instance
     * @return the sankey graph data
     */
    public SankeyGraphDTO getSankeyGraph(TrainingInstance trainingInstance) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingInstance.getTrainingDefinition().getId());
        List<NodeDTO> nodes = participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        if (trainingPhases.isEmpty() || nodes.isEmpty()) {
            return new SankeyGraphDTO(List.of( new NodeDTO(null, null, null, null, -1, null)), new ArrayList<>());
        }
        List<PreProcessLink> preProcessedLinks = this.getAllTasksTransitions(trainingInstance, trainingPhases, nodes);
        this.addLinksFromStartNode(preProcessedLinks, nodes);
        if (nodes.get(nodes.size() - 1).getPhaseOrder().equals(trainingPhases.get(trainingPhases.size() - 1).getOrder())) {
            this.addLinksToFinishNode(preProcessedLinks, nodes);
        } else {
            this.addLinksToNextNotVisitedPhase(preProcessedLinks, nodes);
        }
        this.addEmptyLinksFromInnerNodes(preProcessedLinks);
        this.addStartNodeAndNextNotVisitedPhaseNode(nodes, trainingPhases);
        List<LinkDTO> resultLinks = preProcessedLinks.stream()
                .map(link -> new LinkDTO(link.getSource(), link.getTarget(), link.getValue()))
                .collect(Collectors.toList());
        return new SankeyGraphDTO(nodes, resultLinks);
    }

    /**
     * Obtain all task transitions between specified training phases.
     *
     * @param trainingInstance the training instance
     * @param trainingPhases training phases between which transitions are to be obtained
     * @param nodes list of ordered visited tasks
     * @return the list of links with complement info
     */
    private List<PreProcessLink> getAllTasksTransitions(TrainingInstance trainingInstance, List<TrainingPhase> trainingPhases, List<NodeDTO> nodes) {
        Long definitionId = trainingInstance.getTrainingDefinition().getId();
        List<PreProcessLink> preProcessedLinks = new ArrayList<>();
        for (int i = 1; i < trainingPhases.size(); i++) {
            preProcessedLinks.addAll(participantTaskAssignmentRepository.findTaskTransitionsBetweenTwoPhases(definitionId, trainingInstance.getId(),
                    trainingPhases.get(i-1).getId(), trainingPhases.get(i).getId()));
            if (preProcessedLinks.isEmpty()) {
                break;
            }
        }
        this.addSourcesAndTargetsToLinks(preProcessedLinks, nodes);
        return preProcessedLinks;
    }

    /**
     * Set source and target attributes of the obtained links. Source/target is the order of the task in list of nodes.
     *
     * @param preProcessedLinks obtained links between training phases
     * @param nodes list of ordered visited tasks
     */
    private void addSourcesAndTargetsToLinks(List<PreProcessLink> preProcessedLinks, List<NodeDTO> nodes) {
        AtomicInteger index = new AtomicInteger();
        Map<Long, Integer> orderedVisitedTasksIds = nodes.stream()
                .collect(Collectors.toMap(NodeDTO::getTaskId, tuple -> index.getAndIncrement()));
        preProcessedLinks.forEach(link -> {
            link.setSource(orderedVisitedTasksIds.get(link.getSourceTaskId()) + 1);
            link.setTarget(orderedVisitedTasksIds.get(link.getTargetTaskId()) + 1);
        });
    }

    /**
     * Add links from the start node to other tasks in the first training phase.
     *
     * @param preProcessedLinks obtained links between training phases
     * @param visitedNodes list of ordered visited tasks
     */
    private void addLinksFromStartNode(List<PreProcessLink> preProcessedLinks, List<NodeDTO> visitedNodes) {
        Long firstPhaseId = visitedNodes.get(0).getPhaseId();
        Map<Long, Long> numberOfParticipantsInTaskOfFirstPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(firstPhaseId);
        List<PreProcessLink> linksToAdd = new ArrayList<>();
        int nodeIndex = 1;
        for (NodeDTO visitedNode : visitedNodes) {
            if (!visitedNode.getPhaseId().equals(firstPhaseId)) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTaskOfFirstPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsFinishedTask = preProcessedLinks.stream()
                    .filter(link -> link.getSourceTaskId().equals(visitedNode.getTaskId()))
                    .mapToLong(PreProcessLink::getValue)
                    .sum();
            linksToAdd.add(new PreProcessLink(0, nodeIndex, visitedNode.getTaskId(), visitedNode.getPhaseOrder(), participantsSolvingTask + participantsFinishedTask));
            nodeIndex++;
        }
        preProcessedLinks.addAll(linksToAdd);
    }

    /**
     * Add links from the tasks in the last training phase to the finish node.
     *
     * @param preProcessedLinks obtained links between training phases
     * @param visitedNodes list of ordered visited tasks
     */
    private void addLinksToFinishNode(List<PreProcessLink> preProcessedLinks, List<NodeDTO> visitedNodes) {
        int nodeIndex = visitedNodes.size();
        Long lastPhaseId = visitedNodes.get(visitedNodes.size() - 1).getPhaseId();
        Map<Long, Long> numberOfParticipantsInTasksOfLastPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(lastPhaseId);
        List<PreProcessLink> linksToAdd = new ArrayList<>();
        for (NodeDTO visitedNode: Lists.reverse(visitedNodes)) {
            if (!visitedNode.getPhaseId().equals(lastPhaseId)) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTasksOfLastPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsVisitedTask = preProcessedLinks.stream()
                    .filter(link -> link.getTargetTaskId() != null && link.getTargetTaskId().equals(visitedNode.getTaskId()))
                    .mapToLong(PreProcessLink::getValue)
                    .sum();
            linksToAdd.add(new PreProcessLink(nodeIndex, visitedNode.getTaskId(), visitedNode.getPhaseOrder(), visitedNodes.size() + 1, participantsVisitedTask - participantsSolvingTask));
            nodeIndex--;
        }
        preProcessedLinks.addAll(linksToAdd);
    }

    /**
     * Add empty links from the tasks in the last visited training phase to the next not visited phase.
     *
     * @param preProcessedLinks obtained links between training phases
     * @param visitedNodes list of ordered visited tasks
     */
    private void addLinksToNextNotVisitedPhase(List<PreProcessLink> preProcessedLinks, List<NodeDTO> visitedNodes) {
        int nodeIndex = visitedNodes.size();
        Integer lastPhaseOrder = visitedNodes.get(visitedNodes.size() - 1).getPhaseOrder();
        for (NodeDTO visitedNode: Lists.reverse(visitedNodes)) {
            if (!visitedNode.getPhaseOrder().equals(lastPhaseOrder)) {
                break;
            }
            preProcessedLinks.add(new PreProcessLink(nodeIndex, visitedNode.getTaskId(), visitedNode.getPhaseOrder(), visitedNodes.size() + 1, 0L));
            nodeIndex--;
        }
    }

    /**
     * Add empty links from tasks that haven't been completed by anyone to the random task in the next phase.
     * @param preProcessedLinks obtained links between training phases
     */
    private void addEmptyLinksFromInnerNodes(List<PreProcessLink> preProcessedLinks) {
        // key = phase order, value = overall order of the task in the next phase
        Map<Integer, Integer> phaseToNextPhaseTaskOrder = new HashMap<>();
        preProcessedLinks.forEach(link -> phaseToNextPhaseTaskOrder.put(link.getSourcePhaseOrder(), link.getTarget()));
        Set<Integer> sources = new HashSet<>();
        // key = overall order of visited task, value = phase order
        Map<Integer, Integer> targets = new HashMap<>();
        preProcessedLinks.forEach(link -> {
            sources.add(link.getSource());
            targets.putIfAbsent(link.getTarget(), link.getTargetPhaseOrder());

        });
        targets.forEach((targetTaskOrder, targetPhaseOrder) -> {
            if (!sources.contains(targetTaskOrder) && targetPhaseOrder != null && phaseToNextPhaseTaskOrder.get(targetPhaseOrder) != null) {
                preProcessedLinks.add(new PreProcessLink(targetTaskOrder, phaseToNextPhaseTaskOrder.get(targetPhaseOrder), 0L));
            }
        });
    }

    /**
     * Add start and finish to the list of nodes. If the training have been finished by no one, add next phase node instead of finish node.
     * @param nodes list of visited phases
     * @param trainingPhases all training phase in the training
     */
    private void addStartNodeAndNextNotVisitedPhaseNode(List<NodeDTO> nodes, List<TrainingPhase> trainingPhases) {
        nodes.add(0, new NodeDTO(null, null, null, null, -1, null));
        NodeDTO lastNode = nodes.get(nodes.size() -1);
        TrainingPhase lastTrainingPhase =  trainingPhases.get(trainingPhases.size() - 1);
        if (lastNode.getPhaseOrder().equals(lastTrainingPhase.getOrder())) {
            //FINISH NODE
            nodes.add(new NodeDTO(null, null, null, null, -2, null));
        } else {
            List<Integer> uniqueTrainingPhaseOrders = nodes.stream().map(NodeDTO::getPhaseOrder)
                    .sorted(Integer::compareTo)
                    .distinct()
                    .collect(Collectors.toList());
            TrainingPhase nextNotVisitedPhase = trainingPhases.get(uniqueTrainingPhaseOrders.size()-1);
            nodes.add(new NodeDTO(null, null, null, nextNotVisitedPhase.getId(), nextNotVisitedPhase.getOrder(), nextNotVisitedPhase.getTitle()));
        }
    }
}
