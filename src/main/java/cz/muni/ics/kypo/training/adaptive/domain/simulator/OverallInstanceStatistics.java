package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import cz.muni.ics.kypo.training.adaptive.domain.simulator.imports.ImportTrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class OverallInstanceStatistics {

    @ApiModelProperty(value = "Performance of trainees in instance")
    private List<ParticipantPerformance> participantsPerformance = new ArrayList<>();

    @ApiModelProperty(value = "Training Definition associated with exported training instance")
    private ImportTrainingDefinition trainingDefinition = new ImportTrainingDefinition();
}
