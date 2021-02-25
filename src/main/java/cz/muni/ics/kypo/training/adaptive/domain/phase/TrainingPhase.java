package cz.muni.ics.kypo.training.adaptive.domain.phase;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_phase")
public class TrainingPhase extends AbstractPhase {

    @Column(name = "estimated_duration")
    private int estimatedDuration;
    @Column(name = "allowed_commands")
    private int allowedCommands;
    @Column(name = "allowed_wrong_answers")
    private int allowedWrongAnswers;
    @OrderBy
    @OneToMany(
            mappedBy = "trainingPhase",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Task> tasks = new ArrayList<>();
    @OrderBy
    @OneToMany(
            mappedBy = "trainingPhase",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DecisionMatrixRow> decisionMatrix = new ArrayList<>();
    @OrderBy
    @OneToMany(
            mappedBy = "relatedTrainingPhase",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
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


    @Override
    public String toString() {
        return "TrainingPhase{" +
                "estimatedDuration=" + this.getEstimatedDuration() +
                ", allowedCommands=" + this.getAllowedCommands() +
                ", allowedWrongAnswers=" + this.getAllowedWrongAnswers() +
                ", title='" + super.getTitle() + '\'' +
                ", order=" + super.getOrder() +
                ", id=" + super.getId() +
                '}';
    }
}
