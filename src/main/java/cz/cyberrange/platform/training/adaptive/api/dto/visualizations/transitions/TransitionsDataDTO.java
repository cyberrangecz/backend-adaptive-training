package cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information necessary for transition graph.
 */
@ApiModel(
        value = "TrainingRunDataDTO",
        description = "Data for visualizations that displays the transitions between phases of the multiple training runs."
)
public class TransitionsDataDTO {

    @ApiModelProperty(value = "Information about all phases in training definition.")
    private List<AbstractPhaseDTO> phases;
    @ApiModelProperty(value = "List of training runs data necessary to visualize the transition graph.")
    private List<TrainingRunDataDTO> trainingRunsData = new ArrayList<>();

    public List<AbstractPhaseDTO> getPhases() {
        return phases;
    }

    public void setPhases(List<AbstractPhaseDTO> phases) {
        this.phases = phases;
    }

    public List<TrainingRunDataDTO> getTrainingRunsData() {
        return trainingRunsData;
    }

    public void setTrainingRunsData(List<TrainingRunDataDTO> trainingRunsData) {
        this.trainingRunsData = trainingRunsData;
    }
}
