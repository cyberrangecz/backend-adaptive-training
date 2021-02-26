package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;


import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "adaptive_questions_fulfillment")
public class TrainingPhaseQuestionsFulfillment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adaptiveQuestionsFulfillmentGenerator")
    @SequenceGenerator(name = "adaptiveQuestionsFulfillmentGenerator", sequenceName = "adaptive_questions_fulfillment_seq")
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
