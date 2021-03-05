package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.NodeDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.SankeyGraphDTO;
import cz.muni.ics.kypo.training.adaptive.repository.ParticipantTaskAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisualizationService {

    private final ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;

    public VisualizationService(ParticipantTaskAssignmentRepository participantTaskAssignmentRepository) {
        this.participantTaskAssignmentRepository = participantTaskAssignmentRepository;
    }

    public SankeyGraphDTO getSankeyGraph(TrainingInstance trainingInstance) {
        SankeyGraphDTO sankeyGraphDTO = new SankeyGraphDTO();
        List<NodeDTO> nodes = participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        sankeyGraphDTO.setNodes(nodes);
        return sankeyGraphDTO;
    }
}
