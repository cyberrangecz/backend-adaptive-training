package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cyberrange.platform.training.adaptive.api.dto.AdaptiveSmartAssistantInput;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel(value = "OverallInstancePerformance")
@Data
public class OverallInstancePerformance {

    @JsonProperty()
    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    private Long traineeId;
    @JsonProperty()
    @ApiModelProperty(value = "List of input values for adaptive smart assistant for each phase together with decision matrix")
    private List<AdaptiveSmartAssistantInput> smartAssistantInput;
    @JsonProperty()
    @ApiModelProperty(value = "Performance statistics for each phase")
    private Map<Long, OverallPhaseStatistics> phasesSmartAssistantInput = new HashMap<>();

    public OverallInstancePerformance(Long traineeId, List<AdaptiveSmartAssistantInput> smartAssistantInput, Map<Long, OverallPhaseStatistics> phasesSmartAssistantInput) {
        this.traineeId = traineeId;
        this.smartAssistantInput = smartAssistantInput;
        this.phasesSmartAssistantInput = phasesSmartAssistantInput;
    }

    public OverallInstancePerformance() {
        super();
    }
}
