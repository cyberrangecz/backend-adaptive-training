package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;

import cz.muni.ics.kypo.training.adaptive.domain.UserRef;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_answer")
public class QuestionAnswer implements Serializable {

    @EmbeddedId
    private QuestionAnswerId questionAnswerId;
    private String answer;

    public QuestionAnswerId getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(QuestionAnswerId questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionAnswer)) return false;
        QuestionAnswer that = (QuestionAnswer) o;
        return getQuestionAnswerId().equals(that.getQuestionAnswerId()) &&
                getAnswer().equals(that.getAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionAnswerId(), getAnswer());
    }
}
