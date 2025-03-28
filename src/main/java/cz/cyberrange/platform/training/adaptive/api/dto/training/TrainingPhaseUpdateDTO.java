package cz.cyberrange.platform.training.adaptive.api.dto.training;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.technique.MitreTechniqueDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApiModel(
        value = "TrainingPhaseUpdateDTO",
        description = "Training phase to update."
)
public class TrainingPhaseUpdateDTO extends AbstractPhaseUpdateDTO {

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
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true, position = 5)
    @Valid
    private List<TaskUpdateDTO> tasks;
    @Valid
    @ApiModelProperty(value = "List of mitre techniques used in the training level.")
    private List<MitreTechniqueDTO> mitreTechniques;
    @ApiModelProperty(value = "Set of the expected commands to be executed during the training level.")
    private Set<String> expectedCommands;

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

    public List<TaskUpdateDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskUpdateDTO> tasks) {
        this.tasks = tasks;
    }

    public List<MitreTechniqueDTO> getMitreTechniques() {
        return mitreTechniques;
    }

    public void setMitreTechniques(List<MitreTechniqueDTO> mitreTechniques) {
        this.mitreTechniques = mitreTechniques;
    }

    public Set<String> getExpectedCommands() {
        return expectedCommands;
    }

    public void setExpectedCommands(Set<String> expectedCommands) {
        this.expectedCommands = expectedCommands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrainingPhaseUpdateDTO that = (TrainingPhaseUpdateDTO) o;
        return Objects.equals(getAllowedWrongAnswers(), that.getAllowedWrongAnswers()) && Objects.equals(getAllowedCommands(), that.getAllowedCommands()) && Objects.equals(getEstimatedDuration(), that.getEstimatedDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAllowedWrongAnswers(), getAllowedCommands(), getEstimatedDuration());
    }


    @Override
    public String toString() {
        return "TrainingPhaseUpdateDTO{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", phaseType=" + getPhaseType() +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", allowedCommands=" + allowedCommands +
                ", estimatedDuration=" + estimatedDuration +
                ", decisionMatrix=" + decisionMatrix +
                '}';
    }
}
