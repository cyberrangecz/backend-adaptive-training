package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuestionAnswerId implements Serializable {
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
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAnswerId that = (QuestionAnswerId) o;
        return Objects.equals(question, that.question) &&
                Objects.equals(trainingRunId, that.trainingRunId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, trainingRunId);
    }
}
