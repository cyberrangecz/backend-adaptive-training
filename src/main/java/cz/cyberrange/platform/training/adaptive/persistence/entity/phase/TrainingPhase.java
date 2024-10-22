package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @OrderBy("order ASC")
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
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE} , fetch = FetchType.LAZY)
    @JoinTable(
            name = "training_phase_mitre_technique",
            joinColumns = { @JoinColumn(name = "training_phase_id") },
            inverseJoinColumns = { @JoinColumn(name = "mitre_technique_id")}
    )
    private Set<MitreTechnique> mitreTechniques = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "expected_commands",
            joinColumns = @JoinColumn(name = "training_phase_id")
    )
    private Set<ExpectedCommand> expectedCommands;

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

    public Set<MitreTechnique> getMitreTechniques() {
        return mitreTechniques;
    }

    public void setMitreTechniques(Set<MitreTechnique> mitreTechniques) {
        this.mitreTechniques = mitreTechniques;
    }

    public void addMitreTechnique(MitreTechnique mitreTechnique) {
        this.mitreTechniques.add(mitreTechnique);
        mitreTechnique.addTrainingPhase(this);
    }

    public void removeMitreTechnique(MitreTechnique mitreTechnique) {
        this.mitreTechniques.remove(mitreTechnique);
        mitreTechnique.addTrainingPhase(this);
    }

    public Set<ExpectedCommand> getExpectedCommands() {
        return expectedCommands;
    }

    public void setExpectedCommands(Set<ExpectedCommand> expectedCommands) {
        this.expectedCommands = expectedCommands;
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
