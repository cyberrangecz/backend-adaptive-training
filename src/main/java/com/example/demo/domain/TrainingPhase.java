package com.example.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
public class TrainingPhase extends AbstractPhase {

    private String estimatedDuration;
    private Integer allowedCommands;
    private Integer allowedWrongFlags;

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DecisionMatrixRow> decisionMatrix;

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> subLevels) {
        this.tasks = subLevels;
    }

    public List<DecisionMatrixRow> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRow> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
