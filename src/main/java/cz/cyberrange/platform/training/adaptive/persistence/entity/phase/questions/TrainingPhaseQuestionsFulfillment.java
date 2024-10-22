package cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions;


import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;

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
@Table(name = "adaptive_questions_fulfillment")
public class TrainingPhaseQuestionsFulfillment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adaptiveQuestionsFulfillmentGenerator")
    @SequenceGenerator(name = "adaptiveQuestionsFulfillmentGenerator")
    @Column(name = "adaptive_questions_fulfillment_id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_run_id")
    private TrainingRun trainingRun;
    @Column(name = "fulfilled")
    private boolean fulfilled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_phase_id")
    private TrainingPhase trainingPhase;

    public TrainingPhaseQuestionsFulfillment() {
    }

    public TrainingPhaseQuestionsFulfillment(TrainingRun trainingRun, TrainingPhase trainingPhase, boolean fulfilled) {
        this.trainingRun = trainingRun;
        this.trainingPhase = trainingPhase;
        this.fulfilled = fulfilled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingRun getTrainingRun() {
        return trainingRun;
    }

    public void setTrainingRun(TrainingRun trainingRun) {
        this.trainingRun = trainingRun;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public TrainingPhase getTrainingPhase() {
        return trainingPhase;
    }

    public void setTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    @Override
    public String toString() {
        return "AdaptiveQuestionsFulfillment{" +
                "id=" + id +
                ", fulfilled=" + fulfilled +
                '}';
    }
}
