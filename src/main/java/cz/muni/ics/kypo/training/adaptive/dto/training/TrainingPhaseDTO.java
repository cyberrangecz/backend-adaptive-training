package cz.muni.ics.kypo.training.adaptive.dto.training;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;

import java.util.ArrayList;
import java.util.List;

public class TrainingPhaseDTO extends AbstractPhaseDTO {

    private int estimatedDuration;
    private int allowedCommands;
    private int allowedWrongAnswers;
    private List<TaskDTO> tasks = new ArrayList<>();

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
