package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "question_phase_result")
public class QuestionPhaseResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionPhaseResultGenerator")
    @SequenceGenerator(name = "questionPhaseResultGenerator", sequenceName = "question_phase_result_seq")
    @Column(name = "question_phase_result_id", nullable = false, unique = true)
    private Long id;

    private Long trainingRunId;
    private int achievedResult;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionPhaseRelation questionPhaseRelation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public int getAchievedResult() {
        return achievedResult;
    }

    public void setAchievedResult(int achievedResult) {
        this.achievedResult = achievedResult;
    }

    public QuestionPhaseRelation getQuestionPhaseRelation() {
        return questionPhaseRelation;
    }

    public void setQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation) {
        this.questionPhaseRelation = questionPhaseRelation;
    }
}
