package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class DecisionMatrixRow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "order_in_phase", nullable = false)
    private int order;
    private double assessmentAnswered;
    private double keywordUsed;
    private double completedInTime;
    private double solutionDisplayed;
    private double wrongFlags;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public double getWrongFlags() {
        return wrongFlags;
    }

    public void setWrongFlags(double wrongFlags) {
        this.wrongFlags = wrongFlags;
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
                "id=" + id +
                ", order=" + order +
                ", assessmentAnswered=" + assessmentAnswered +
                ", keywordUsed=" + keywordUsed +
                ", completedInTime=" + completedInTime +
                ", solutionDisplayed=" + solutionDisplayed +
                ", wrongFlags=" + wrongFlags +
                '}';
    }
}
