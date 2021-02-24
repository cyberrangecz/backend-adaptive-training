package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;


import javax.persistence.*;
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
    @JoinColumn(name = "question_phase_relation_id")
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
