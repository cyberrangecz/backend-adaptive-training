package com.example.demo.dto;

import java.util.List;

public class PhaseLevelUpdateDto {

    private Long id;
    private String title;
    private Integer order;
    private Integer allowedWrongFlags;
    private Integer allowedCommands;
    private Integer estimatedDuration;
    private Long maxScore;
    private List<DecisionMatrixRowDto> decisionMatrix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    public Long getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Long maxScore) {
        this.maxScore = maxScore;
    }

    public List<DecisionMatrixRowDto> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDto> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
