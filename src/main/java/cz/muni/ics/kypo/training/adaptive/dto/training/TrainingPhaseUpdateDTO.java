package cz.muni.ics.kypo.training.adaptive.dto.training;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = "TrainingPhaseUpdateDTO",
        description = "Training phase to update."
)
public class TrainingPhaseUpdateDTO {

    @ApiModelProperty(value = "Short description of training phase", required = true, example = "Training phase title")
    @NotEmpty(message = "{trainingPhase.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10", position = 1)
    @NotNull(message = "{trainingPhase.allowedWrongAnswers.NotNull.message}")
    @Min(value = 0, message = "{trainingPhase.allowedWrongAnswers.Min.message}")
    private Integer allowedWrongAnswers;
    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10", position = 2)
    @NotNull(message = "{trainingPhase.allowedCommands.NotNull.message}")
    @Min(value = 0, message = "{trainingPhase.allowedCommands.Min.message}")
    private Integer allowedCommands;
    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20", position = 3)
    @NotNull(message = "{trainingPhase.estimatedDuration.NotNull.message}")
    @Min(value = 0, message = "{trainingPhase.estimatedDuration.Min.message}")
    private Integer estimatedDuration;
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true, position = 4)
    @Valid
    @NotEmpty(message = "{trainingPhase.decisionMatrix.NotEmpty.message}")
    private List<DecisionMatrixRowDTO> decisionMatrix;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAllowedWrongAnswers() {
        return allowedWrongAnswers;
    }

    public void setAllowedWrongAnswers(Integer allowedWrongAnswers) {
        this.allowedWrongAnswers = allowedWrongAnswers;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingPhaseUpdateDTO that = (TrainingPhaseUpdateDTO) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(allowedWrongAnswers, that.allowedWrongAnswers) &&
                Objects.equals(allowedCommands, that.allowedCommands) &&
                Objects.equals(estimatedDuration, that.estimatedDuration) &&
                Objects.equals(decisionMatrix, that.decisionMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, allowedWrongAnswers, allowedCommands, estimatedDuration, decisionMatrix);
    }

    @Override
    public String toString() {
        return "TrainingPhaseUpdateDTO{" +
                "title='" + title + '\'' +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", allowedCommands=" + allowedCommands +
                ", estimatedDuration=" + estimatedDuration +
                ", decisionMatrix=" + decisionMatrix +
                '}';
    }
}
