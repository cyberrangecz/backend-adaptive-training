package cz.muni.ics.kypo.training.adaptive.dto.visualizations.simulator;

import cz.muni.ics.kypo.training.adaptive.domain.simulator.imports.ImportTrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.SankeyDiagramDTO;
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
