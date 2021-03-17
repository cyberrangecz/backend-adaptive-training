package cz.muni.ics.kypo.training.adaptive.service;

import com.google.common.collect.Lists;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.LinkDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.NodeDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.PreProcessLink;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.SankeyGraphDTO;
import cz.muni.ics.kypo.training.adaptive.repository.ParticipantTaskAssignmentRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TaskRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TrainingPhaseRepository;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisualizationService {

    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;
    private final TaskRepository taskRepository;

    public VisualizationService(ParticipantTaskAssignmentRepository participantTaskAssignmentRepository,
                                TaskRepository taskRepository) {
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
        this.taskRepository = taskRepository;
    }

    public SankeyGraphDTO getSankeyGraph(TrainingInstance trainingInstance) {
        SankeyGraphDTO sankeyGraphDTO = new SankeyGraphDTO();
        List<NodeDTO> nodes = participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        NodeDTO lastNodeDTO = nodes.get(nodes.size() -1);

        List<PreProcessLink> preProcessedLinks = participantTaskAssignmentRepository.findAllTaskTransitions(trainingInstance.getTrainingDefinition().getId(), trainingInstance.getId());
        List<LinkDTO> resultLinks = new ArrayList<>();

        this.addLinksFromStartNode(preProcessedLinks, resultLinks, nodes.get(0).getPhaseId(), nodes.get(0).getPhaseOrder());
        this.addLinksToFinishNode(preProcessedLinks, resultLinks, lastNodeDTO, nodes.size());

        resultLinks.addAll(preProcessedLinks.stream()
                .map(link -> new LinkDTO(link.getSource(), link.getTarget(), link.getValue()))
                .collect(Collectors.toList()));

        this.addStartAndFinishNode(nodes);

        sankeyGraphDTO.setNodes(nodes);
        sankeyGraphDTO.setLinks(resultLinks);
        return sankeyGraphDTO;
    }

    private void addStartAndFinishNode(List<NodeDTO> nodes) {
        nodes.add(0, new NodeDTO(null, null, null, null, -1, null));
        nodes.add(new NodeDTO(null, null, null, null, -2, null));
    }

    private void addLinksFromStartNode(List<PreProcessLink> preProcessedLinks,
                                       List<LinkDTO> resultLinks,
                                       Long firstPhaseId,
                                       Integer firstPhaseOrder) {
        Map<Long, Long> numberOfParticipantsInTaskOfFirstPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(firstPhaseId);
        for (PreProcessLink preProcessedLink: preProcessedLinks) {
            if (!preProcessedLink.getSourcePhaseOrder().equals(firstPhaseOrder)) {
                break;
            }
            Long participantsSolvingTask = Optional.ofNullable(numberOfParticipantsInTaskOfFirstPhase.get(preProcessedLink.getSourceTaskId())).orElse(0L);
            resultLinks.add(new LinkDTO(0, preProcessedLink.getSource(), preProcessedLink.getValue() + participantsSolvingTask));
        }
    }

    private void addLinksToFinishNode(List<PreProcessLink> preProcessedLinks,
                                      List<LinkDTO> resultLinks,
                                      NodeDTO lastNode,
                                      Integer numberOfNodes) {
        Map<Long, Long> numberOfParticipantsInTaskOfLastPhase = participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(lastNode.getPhaseId());
        Map<Long, LinkDTO> linksFromLastPhaseToFinish = new HashMap<>();
        for (PreProcessLink preProcessedLink: Lists.reverse(preProcessedLinks)) {
            if (!preProcessedLink.getTargetPhaseOrder().equals(lastNode.getPhaseOrder())) {
                break;
            }
            linksFromLastPhaseToFinish.merge(preProcessedLink.getTargetTaskId(),
                    new LinkDTO(preProcessedLink.getTarget(), numberOfNodes + 1, preProcessedLink.getValue()), (this::mergeLinkValues));
        }
        numberOfParticipantsInTaskOfLastPhase.forEach((k, v) -> linksFromLastPhaseToFinish.get(k).setValue(linksFromLastPhaseToFinish.get(k).getValue() - v));
        resultLinks.addAll(linksFromLastPhaseToFinish.values());
    }

    private LinkDTO mergeLinkValues(LinkDTO link1, LinkDTO link2) {
        link1.setValue(link1.getValue() + link2.getValue());
        return link1;
    }
}
