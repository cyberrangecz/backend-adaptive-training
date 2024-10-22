package cz.cyberrange.platform.training.adaptive.api.dto.visualizations.simulator;

import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.ImportTrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.SankeyDiagramDTO;
import lombok.Data;

@Data
public class InstanceSimulatorDTO {
    private SankeyDiagramDTO sankeyDiagram = new SankeyDiagramDTO();
    private ImportTrainingDefinition trainingDefinition = new ImportTrainingDefinition();
    private String cacheKey;

    public InstanceSimulatorDTO(SankeyDiagramDTO sankeyDiagram, ImportTrainingDefinition trainingDefinition, String cacheKey) {
        this.sankeyDiagram = sankeyDiagram;
        this.trainingDefinition = trainingDefinition;
        this.cacheKey = cacheKey;
    }
}
