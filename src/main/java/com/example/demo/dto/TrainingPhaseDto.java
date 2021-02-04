package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainingPhaseDto extends AbstractPhaseDto {

    private String estimatedDuration;
    private int allowedCommands;
    private int allowedWrongFlags;
    private List<TaskDto> tasks = new ArrayList<>();

    private List<DecisionMatrixRowDto> decisionMatrix;

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public int getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(int allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public int getAllowedWrongFlags() {
        return allowedWrongFlags;
    }

    public void setAllowedWrongFlags(int allowedWrongFlags) {
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
