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

@Service
public class VisualizationService {

    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;

    public VisualizationService(ParticipantTaskAssignmentRepository participantTaskAssignmentRepository,
                                TrainingPhaseRepository trainingPhaseRepository) {
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
    }

    public SankeyGraphDTO getSankeyGraph(TrainingInstance trainingInstance) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingInstance.getTrainingDefinition().getId());
        if (trainingPhases.isEmpty()) {
            return new SankeyGraphDTO();
        }
        List<NodeDTO> nodes = participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        List<PreProcessLink> preProcessedLinks = this.getAllTasksTransitions(trainingInstance, trainingPhases);
        this.addSourcesAndTargetsToLinks(preProcessedLinks, nodes);
        this.addLinksFromStartNode(preProcessedLinks, nodes);
        this.addLinksToNextNotVisitedPhase(preProcessedLinks, nodes, trainingPhases);

        List<LinkDTO> resultLinks = preProcessedLinks.stream()
                .map(link -> new LinkDTO(link.getSource(), link.getTarget(), link.getValue()))
                .collect(Collectors.toList());

        this.addEmptyLinksFromInnerNodes(resultLinks, preProcessedLinks);
        this.addStartNodeAndNextNotVisitedPhaseNode(nodes, trainingPhases);
        return new SankeyGraphDTO(nodes, resultLinks);
    }

    private void addLinksFromStartNode(List<PreProcessLink> preProcessedLinks, List<NodeDTO> visitedNodes) {
        Long firstPhaseId = visitedNodes.get(0).getPhaseId();
        Integer firstPhaseOrder = visitedNodes.get(0).getPhaseOrder();
        Map<Long, Long> numberOfParticipantsInTaskOfFirstPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(firstPhaseId);
        List<PreProcessLink> linksToAdd = new ArrayList<>();
        int nodeIndex = 1;
        for (NodeDTO visitedNode : visitedNodes) {
            if (!visitedNode.getPhaseOrder().equals(firstPhaseOrder)) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTaskOfFirstPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsFinishedTask = preProcessedLinks.stream()
                    .filter(link -> link.getSourceTaskId().equals(visitedNode.getTaskId()))
                    .map(PreProcessLink::getValue)
                    .reduce(0L, Long::sum);
            linksToAdd.add(new PreProcessLink(0, nodeIndex, participantsSolvingTask + participantsFinishedTask));
            nodeIndex++;
        }
        preProcessedLinks.addAll(linksToAdd);
    }

    private void addLinksToNextNotVisitedPhase(List<PreProcessLink> preProcessedLinks,
                                      List<NodeDTO> visitedNodes,
                                      List<TrainingPhase> trainingPhases) {
        int nodeIndex = visitedNodes.size();
        Map<Long, Long> numberOfParticipantsInTasksOfLastPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(trainingPhases.get(trainingPhases.size() - 1).getId());
        List<PreProcessLink> linksToAdd = new ArrayList<>();
        for (NodeDTO visitedNode: Lists.reverse(visitedNodes)) {
            if (!visitedNode.getPhaseId().equals(trainingPhases.get(trainingPhases.size() - 1).getId())) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTasksOfLastPhase.get(visitedNode.getTaskId())).orElse(0L);
            Long participantsVisitedTask = preProcessedLinks.stream()
                    .filter(link -> link.getTargetTaskId() != null && link.getTargetTaskId().equals(visitedNode.getTaskId()))
                    .mapToLong(PreProcessLink::getValue)
                    .sum();
            linksToAdd.add(new PreProcessLink(nodeIndex, visitedNodes.size() + 1, participantsVisitedTask - participantsSolvingTask));
            nodeIndex--;
        }
        preProcessedLinks.add(new PreProcessLink(visitedNodes.size(), visitedNodes.size() + 1, 0L));
        preProcessedLinks.addAll(linksToAdd);
    }

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

    private List<PreProcessLink> getAllTasksTransitions(TrainingInstance trainingInstance, List<TrainingPhase> trainingPhases) {
        Long definitionId = trainingInstance.getTrainingDefinition().getId();
        List<PreProcessLink> preProcessedLinks = new ArrayList<>();
        for (int i = 1; i < trainingPhases.size(); i++) {
            preProcessedLinks.addAll(participantTaskAssignmentRepository.findTaskTransitionsBetweenTwoPhases(definitionId, trainingInstance.getId(),
                    trainingPhases.get(i-1).getId(), trainingPhases.get(i).getId()));
            if (preProcessedLinks.isEmpty()) {
                break;
            }
        }
        return preProcessedLinks;
    }

    private void addSourcesAndTargetsToLinks(List<PreProcessLink> preProcessedLinks, List<NodeDTO> nodes) {
        AtomicInteger index = new AtomicInteger();
        Map<Long, Integer> orderedVisitedTasksIds = nodes.stream()
                .collect(Collectors.toMap(NodeDTO::getTaskId, tuple -> index.getAndIncrement()));
        preProcessedLinks.forEach(link -> {
            link.setSource(orderedVisitedTasksIds.get(link.getSourceTaskId()) + 1);
            link.setTarget(orderedVisitedTasksIds.get(link.getTargetTaskId()) + 1);
        });
    }

    private void addEmptyLinksFromInnerNodes(List<LinkDTO> resultLinks, List<PreProcessLink> preProcessedLinks) {
        // key = phase order, value = first task overall order in the next phase
        Map<Integer, Integer> phaseToNextPhaseTaskOrder = new HashMap<>();
        preProcessedLinks.forEach(link -> phaseToNextPhaseTaskOrder.put(link.getSourcePhaseOrder(), link.getTarget()));
        // key = overall visited task order, value = phase order
        Map<Integer, Integer> sources = new HashMap<>();
        Map<Integer, Integer> targets = new HashMap<>();
        preProcessedLinks.forEach(link -> {
            sources.putIfAbsent(link.getSource(), link.getSourcePhaseOrder());
            targets.putIfAbsent(link.getTarget(), link.getTargetPhaseOrder());

        });
        targets.forEach((targetTaskOrder, targetPhaseOrder) -> {
            if (!sources.containsKey(targetTaskOrder) && targetPhaseOrder != null && phaseToNextPhaseTaskOrder.get(targetPhaseOrder) != null) {
                resultLinks.add(new LinkDTO(targetTaskOrder, phaseToNextPhaseTaskOrder.get(targetPhaseOrder), 0L));
            }
        });
    }
}
