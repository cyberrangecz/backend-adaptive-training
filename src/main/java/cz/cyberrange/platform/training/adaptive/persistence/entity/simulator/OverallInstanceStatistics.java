package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.ImportTrainingDefinition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OverallInstanceStatistics {

    @ApiModelProperty(value = "Performance of trainees in instance")
    private List<ParticipantPerformance> participantsPerformance = new ArrayList<>();

    @ApiModelProperty(value = "Training Definition associated with exported training instance")
    private ImportTrainingDefinition trainingDefinition = new ImportTrainingDefinition();
}
