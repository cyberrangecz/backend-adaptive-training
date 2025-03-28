package cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions;

import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    @ElementCollection
    @CollectionTable(
            name = "question_answers",
            joinColumns = {@JoinColumn(name = "question_id"), @JoinColumn(name = "training_run_id")}
    )
    @Column(name = "answer")
    private Set<String> answers = new HashSet<>();

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

    public Set<String> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "questionAnswerId=" + this.getQuestionAnswerId().getQuestionId() +
                ", answers='" + this.getAnswers() + '\'' +
                '}';
    }
}
