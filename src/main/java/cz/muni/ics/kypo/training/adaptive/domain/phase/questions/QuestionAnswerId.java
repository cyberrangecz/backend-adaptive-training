package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;


import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuestionAnswerId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
    private Long trainingRunId;

    public QuestionAnswerId() {
    }

    public QuestionAnswerId(Question question, Long trainingRunId) {
        this.question = question;
        this.trainingRunId = trainingRunId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionAnswerId))
            return false;
        QuestionAnswerId that = (QuestionAnswerId) o;
        return getQuestion().equals(that.getQuestion()) &&
                getTrainingRunId().equals(that.getTrainingRunId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, trainingRunId);
    }
}
