package com.example.demo.dto;

import java.util.List;

public class TrainingPhaseDto extends AbstractPhaseDto {

    private String estimatedDuration;
    private Integer allowedCommands;
    private Integer allowedWrongFlags;
    private List<TaskDto> tasks;

    private List<DecisionMatrixRowDto> decisionMatrix;

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(Integer allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public Integer getAllowedWrongFlags() {
        return allowedWrongFlags;
    }

    public void setAllowedWrongFlags(Integer allowedWrongFlags) {
        this.allowedWrongFlags = allowedWrongFlags;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowDto> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDto> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
