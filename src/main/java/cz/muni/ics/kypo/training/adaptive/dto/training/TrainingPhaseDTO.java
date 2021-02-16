package cz.muni.ics.kypo.training.adaptive.dto.training;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class TrainingPhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    private int estimatedDuration;

    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    private int allowedCommands;

    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10")
    private int allowedWrongAnswers;

    @ApiModelProperty(value = "Tasks associated with the training phase", required = true)
    private List<TaskDTO> tasks = new ArrayList<>();

    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true)
    private List<DecisionMatrixRowDTO> decisionMatrix;

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

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
