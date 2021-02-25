package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "question_answer")
public class QuestionAnswer implements Serializable {

    @EmbeddedId
    private QuestionAnswerId questionAnswerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("trainingRunId")
    @JoinColumn(name = "training_run_id")
    private TrainingRun trainingRun;
    @Column(name = "answer")
    private String answer;

    public QuestionAnswer() {
        this.questionAnswerId = new QuestionAnswerId();
    }

    public QuestionAnswer(Question question, TrainingRun trainingRun) {
        this.question = question;
        this.trainingRun = trainingRun;
        this.questionAnswerId = new QuestionAnswerId(question.getId(), trainingRun.getId());
    }

    public QuestionAnswerId getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(QuestionAnswerId questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.questionAnswerId.setQuestionId(question.getId());
        this.question = question;
    }

    public TrainingRun getTrainingRun() {
        return trainingRun;
    }

    public void setTrainingRun(TrainingRun trainingRun) {
        this.questionAnswerId.setTrainingRunId(trainingRun.getId());
        this.trainingRun = trainingRun;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "questionAnswerId=" + this.getQuestionAnswerId() +
                ", answer='" + this.getQuestionAnswerId() + '\'' +
                '}';
    }
}
