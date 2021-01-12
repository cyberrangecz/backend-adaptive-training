package com.example.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
public class PhaseLevel extends BaseLevel {

    @OrderBy
    @OneToMany(mappedBy = "phaseLevel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> subLevels;

    @OrderBy
    @OneToMany(mappedBy = "phaseLevel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DecisionMatrixRow> decisionMatrix;

    public List<Task> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<Task> subLevels) {
        this.subLevels = subLevels;
    }

    public List<DecisionMatrixRow> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRow> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }
}
