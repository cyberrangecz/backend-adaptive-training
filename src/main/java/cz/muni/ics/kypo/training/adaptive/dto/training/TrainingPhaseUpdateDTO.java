package cz.muni.ics.kypo.training.adaptive.dto.training;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TrainingPhaseUpdateDTO {

    @ApiModelProperty(value = "Short description of training phase", required = true, example = "Training phase title")
    @NotEmpty(message = "Training phase title must not be blank")
    private String title;

    @ApiModelProperty(value = "Maximal number of allowed wrong flags provided by played", required = true, example = "10")
    @NotNull(message = "Maximal number of allowed wrong flags must be set")
    private Integer allowedWrongFlags;

    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    @NotNull(message = "Maximal number of allowed commands must be set")
    private Integer allowedCommands;

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    @NotNull(message = "Estimated duration of phase task must be set")
    private Integer estimatedDuration;

    private List<DecisionMatrixRowDTO> decisionMatrix;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAllowedWrongFlags() {
        return allowedWrongFlags;
    }

    public void setAllowedWrongFlags(Integer allowedWrongFlags) {
        this.allowedWrongFlags = allowedWrongFlags;
    }

    public Integer getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(Integer allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public List<DecisionMatrixRowDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
