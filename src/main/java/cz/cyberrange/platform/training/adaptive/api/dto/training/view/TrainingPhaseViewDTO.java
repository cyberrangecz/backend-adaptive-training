package cz.cyberrange.platform.training.adaptive.api.dto.training.view;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class TrainingPhaseViewDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    private int estimatedDuration;
    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    private int allowedCommands;
    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10")
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Task associated with the training phase", required = true)
    private TaskViewDTO task;

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public int getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(int allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public int getAllowedWrongAnswers() {
        return allowedWrongAnswers;
    }

    public void setAllowedWrongAnswers(int allowedWrongAnswers) {
        this.allowedWrongAnswers = allowedWrongAnswers;
    }

    public TaskViewDTO getTask() {
        return task;
    }

    public void setTask(TaskViewDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingPhaseViewDTO that = (TrainingPhaseViewDTO) o;
        return estimatedDuration == that.estimatedDuration &&
                allowedCommands == that.allowedCommands &&
                allowedWrongAnswers == that.allowedWrongAnswers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(estimatedDuration, allowedCommands, allowedWrongAnswers, task);
    }

    @Override
    public String toString() {
        return "TrainingPhaseDTO{" +
                "estimatedDuration=" + estimatedDuration +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", task=" + task +
                "} " + super.toString();
    }
}
