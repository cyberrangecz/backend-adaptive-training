package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "decision_matrix_row")
public class DecisionMatrixRow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "decisionMatrixRowGenerator")
    @SequenceGenerator(name = "decisionMatrixRowGenerator")
    @Column(name = "decision_matrix_row_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "order_in_training_phase", nullable = false)
    private int order;
    @Column(name = "questionnaire_answered")
    private double questionnaireAnswered;
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

    public void incrementOrder(int incrementNumber) {
        this.order += incrementNumber;
    }

    public void decrementOrder(int decrementNumber) {
        this.order -= decrementNumber;
    }

    public double getQuestionnaireAnswered() {
        return questionnaireAnswered;
    }

    public void setQuestionnaireAnswered(double questionnaireAnswered) {
        this.questionnaireAnswered = questionnaireAnswered;
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
                ", questionnaireAnswered=" + this.getQuestionnaireAnswered() +
                ", keywordUsed=" + this.getKeywordUsed() +
                ", completedInTime=" + this.getCompletedInTime() +
                ", solutionDisplayed=" + this.getSolutionDisplayed() +
                ", wrongAnswers=" + this.getWrongAnswers() +
                '}';
    }
}
