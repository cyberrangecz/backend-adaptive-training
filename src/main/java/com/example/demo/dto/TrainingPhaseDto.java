package com.example.demo.dto;

import java.util.List;

public class TrainingPhaseDto extends AbstractPhaseDto {

    private List<TaskDto> tasks;

    private List<DecisionMatrixRowDto> decisionMatrix;

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
