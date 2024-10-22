package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "task")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskGenerator")
    @SequenceGenerator(name = "taskGenerator")
    @Column(name = "task_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "answer")
    private String answer;
    @Column(name = "solution")
    private String solution;
    @Column(name = "incorrect_answer_limit")
    private int incorrectAnswerLimit;
    @Column(name = "modify_sandbox")
    private boolean modifySandbox;
    @Column(name = "sandbox_change_expected_duration")
    private int sandboxChangeExpectedDuration;
    @Column(name = "order_in_training_phase", nullable = false)
    private Integer order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_phase_id")
    private TrainingPhase trainingPhase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getIncorrectAnswerLimit() {
        return incorrectAnswerLimit;
    }

    public void setIncorrectAnswerLimit(int incorrectAnswerLimit) {
        this.incorrectAnswerLimit = incorrectAnswerLimit;
    }

    public TrainingPhase getTrainingPhase() {
        return trainingPhase;
    }

    public void setTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    public boolean isModifySandbox() {
        return modifySandbox;
    }

    public void setModifySandbox(boolean modifySandbox) {
        this.modifySandbox = modifySandbox;
    }

    public int getSandboxChangeExpectedDuration() {
        return sandboxChangeExpectedDuration;
    }

    public void setSandboxChangeExpectedDuration(int sandboxChangeExpectedDuration) {
        this.sandboxChangeExpectedDuration = sandboxChangeExpectedDuration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", content='" + this.getContent() + '\'' +
                ", answer='" + this.getAnswer() + '\'' +
                ", solution='" + this.getSolution() + '\'' +
                ", incorrectAnswerLimit=" + this.getIncorrectAnswerLimit() +
                ", modifySandbox=" + this.isModifySandbox() +
                ", sandboxChangeExpectedDuration=" + this.getSandboxChangeExpectedDuration() +
                ", order=" + this.getOrder() +
                '}';
    }
}
