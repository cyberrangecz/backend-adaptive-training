package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;


import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "questions_phase_relation_result")
public class QuestionsPhaseRelationResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionsPhaseRelationResultGenerator")
    @SequenceGenerator(name = "questionsPhaseRelationResultGenerator", sequenceName = "questions_phase_relation_result_seq")
    @Column(name = "questions_phase_relation_result_id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_run_id")
    private TrainingRun trainingRun;
    @Column(name = "achieved_result")
    private double achievedResult;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_phase_relation_id")
    private QuestionPhaseRelation questionPhaseRelation;

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

    public double getAchievedResult() {
        return achievedResult;
    }

    public void setAchievedResult(double achievedResult) {
        this.achievedResult = achievedResult;
    }

    public QuestionPhaseRelation getQuestionPhaseRelation() {
        return questionPhaseRelation;
    }

    public void setQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation) {
        this.questionPhaseRelation = questionPhaseRelation;
    }

    @Override
    public String toString() {
        return "QuestionPhaseResult{" +
                "id=" + this.getId() +
                ", achievedResult=" + this.getAchievedResult() +
                '}';
    }
}
