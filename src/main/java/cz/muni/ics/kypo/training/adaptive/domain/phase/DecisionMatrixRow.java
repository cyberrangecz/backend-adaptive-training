package cz.muni.ics.kypo.training.adaptive.domain.phase;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "decision_matrix_row")
public class DecisionMatrixRow implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "decisionMatrixRowGenerator"
    )
    @SequenceGenerator(
            name = "decisionMatrixRowGenerator",
            sequenceName = "decision_matrix_row_seq"
    )
    @Column(
            name = "decision_matrix_row_id",
            nullable = false,
            unique = true
    )
    private Long id;
    @Column(
            name = "order_in_training_phase",
            nullable = false
    )
    private int order;
    @Column(name = "assessment_answered")
    private double assessmentAnswered;
    @Column(name = "keyword_used")
    private double keywordUsed;
    @Column(name = "completed_in_time")
    private double completedInTime;
    @Column(name = "solution_displayed")
    private double solutionDisplayed;
    @Column(name = "wrong_answers")
    private double wrongAnswers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_phase_id")
    private TrainingPhase trainingPhase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getAssessmentAnswered() {
        return assessmentAnswered;
    }

    public void setAssessmentAnswered(double assessmentAnswered) {
        this.assessmentAnswered = assessmentAnswered;
    }

    public double getKeywordUsed() {
        return keywordUsed;
    }

    public void setKeywordUsed(double keywordUsed) {
        this.keywordUsed = keywordUsed;
    }

    public double getCompletedInTime() {
        return completedInTime;
    }

    public void setCompletedInTime(double completedInTime) {
        this.completedInTime = completedInTime;
    }

    public double getSolutionDisplayed() {
        return solutionDisplayed;
    }

    public void setSolutionDisplayed(double solutionDisplayed) {
        this.solutionDisplayed = solutionDisplayed;
    }

    public double getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(double wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public TrainingPhase getTrainingPhase() {
        return trainingPhase;
    }

    public void setTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    @Override
    public String toString() {
        return "DecisionMatrixRow{" +
                "id=" + this.getId() +
                ", order=" + this.getOrder() +
                ", assessmentAnswered=" + this.getAssessmentAnswered() +
                ", keywordUsed=" + this.getKeywordUsed() +
                ", completedInTime=" + this.getCompletedInTime() +
                ", solutionDisplayed=" + this.getSolutionDisplayed() +
                ", wrongAnswers=" + this.getWrongAnswers() +
                '}';
    }
}
