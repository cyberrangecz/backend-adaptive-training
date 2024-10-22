package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is taken from project elasticsearch-service.
 */
@ApiModel(value = "OverallPhaseStatistics")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class OverallPhaseStatistics {
    @JsonProperty()
    @ApiModelProperty(value = "ID of a phase", example = "1")
    private Long phaseId;
    @JsonProperty()
    @ApiModelProperty(value = "ID of a task", example = "3")
    private Long taskId;
    @JsonProperty()
    @ApiModelProperty(value = "ID of a sandbox", example = "1")
    private String sandboxId;
    @JsonProperty()
    @ApiModelProperty(value = "Order of a phase", example = "0")
    private Long phaseOrder;
    @JsonProperty()
    @ApiModelProperty(value = "ID of a task", example = "1614803536837")
    private Long phaseTime;
    @JsonProperty()
    @ApiModelProperty(value = "The number of answers (flags) that participant submitted incorrectly", example = "2")
    private List<String> wrongAnswers = new ArrayList<>();
    @JsonProperty()
    @ApiModelProperty(value = "The information if the solution was displayed", example = "true")
    private Boolean solutionDisplayed;
    @JsonProperty()
    @ApiModelProperty(value = "The number of submitted commands", example = "5")
    private int numberOfCommands;
    @JsonProperty()
    @ApiModelProperty(value = "List of trainees answers", example = "[true,false,true,true]")
    private List<Boolean> questionsAnswer = new ArrayList<>();
    @ApiModelProperty(value = "The map containing the mapping if the commands contains the right keywords")
    private Map<String, Long> keywordsInCommands = new HashMap<>();
}