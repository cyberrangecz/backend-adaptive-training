package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

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
}
