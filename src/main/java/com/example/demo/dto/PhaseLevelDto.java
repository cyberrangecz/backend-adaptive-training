package com.example.demo.dto;

import java.util.List;

public class PhaseLevelDto extends BaseLevelDto {

    private List<TaskDto> subLevels;

    private List<DecisionMatrixRowDto> decisionMatrix;

    public List<TaskDto> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<TaskDto> subLevels) {
        this.subLevels = subLevels;
    }

    public List<DecisionMatrixRowDto> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDto> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
