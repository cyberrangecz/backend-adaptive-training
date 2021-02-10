package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingPhase extends AbstractPhase {

    private int estimatedDuration;
    private int allowedCommands;
    private int allowedWrongAnswers;

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DecisionMatrixRow> decisionMatrix;

    @OrderBy
    @OneToMany(mappedBy = "relatedTrainingPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionPhaseRelation> questionPhaseRelations = new ArrayList<>();

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRow> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRow> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    public List<QuestionPhaseRelation> getQuestionPhaseRelations() {
        return questionPhaseRelations;
    }

    public void setQuestionPhaseRelations(List<QuestionPhaseRelation> questionPhaseRelations) {
        this.questionPhaseRelations = questionPhaseRelations;
    }
}
