package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.dto.sankeydiagram.SankeyDiagramDTO;
import cz.muni.ics.kypo.training.adaptive.service.VisualizationService;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingInstanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisualizationFacade {

    private final VisualizationService visualizationService;
    private final TrainingInstanceService trainingInstanceService;

    public VisualizationFacade(VisualizationService visualizationService,
                               TrainingInstanceService trainingInstanceService) {
        this.visualizationService = visualizationService;
        this.trainingInstanceService = trainingInstanceService;
    }

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isOrganizerOfGivenTrainingInstance(#trainingInstanceId)")
    @Transactional(readOnly = true)
    public SankeyDiagramDTO getSankeyDiagram(Long trainingInstanceId) {
        TrainingInstance trainingInstance = trainingInstanceService.findById(trainingInstanceId);
        return visualizationService.getSankeyDiagram(trainingInstance);
    }
}
