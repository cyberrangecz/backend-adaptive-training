package cz.muni.ics.kypo.training.adaptive.domain.phase;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_phase")
public class TrainingPhase extends AbstractPhase {

    private int estimatedDuration;
    private int allowedCommands;
    private int allowedWrongAnswers;

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OrderBy
    @OneToMany(mappedBy = "trainingPhase", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DecisionMatrixRow> decisionMatrix;

    @OrderBy
    @OneToMany(mappedBy = "relatedTrainingPhase", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
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
