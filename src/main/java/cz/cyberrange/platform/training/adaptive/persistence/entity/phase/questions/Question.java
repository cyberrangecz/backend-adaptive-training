package cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "question")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionGenerator")
    @SequenceGenerator(name = "questionGenerator", sequenceName = "question_seq")
    @Column(name = "question_id", nullable = false, unique = true)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;
    @Column(name = "text")
    private String text;
    @Column(name = "order_in_questionnaire")
    private int order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_phase_id")
    private QuestionnairePhase questionnairePhase;
    @OrderBy
    @OneToMany(
            mappedBy = "question",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<QuestionChoice> choices = new ArrayList<>();
    @ManyToMany(
            mappedBy = "questions",
            fetch = FetchType.LAZY
    )
    private Set<QuestionPhaseRelation> questionPhaseRelations = new HashSet<>();

    @Column(name = "answer_required")
    private boolean answerRequired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public QuestionnairePhase getQuestionnairePhase() {
        return questionnairePhase;
    }

    public void setQuestionnairePhase(QuestionnairePhase questionnairePhase) {
        this.questionnairePhase = questionnairePhase;
    }

    public List<QuestionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoice> choices) {
        this.choices = choices;
    }

    public Set<QuestionPhaseRelation> getQuestionPhaseRelations() {
        return Collections.unmodifiableSet(questionPhaseRelations);
    }

    public void setQuestionPhaseRelations(Set<QuestionPhaseRelation> questionPhaseRelations) {
        this.questionPhaseRelations = questionPhaseRelations;
    }

    public void addQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation) {
        this.questionPhaseRelations.add(questionPhaseRelation);
    }

    public boolean isAnswerRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return getOrder() == question.getOrder() &&
                getQuestionType() == question.getQuestionType() &&
                Objects.equals(getText(), question.getText()) &&
                Objects.equals(isAnswerRequired(), question.isAnswerRequired());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionType(), getText(), getOrder(), isAnswerRequired());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + this.getId() +
                ", questionType=" + this.getQuestionType() +
                ", text='" + this.getText() + '\'' +
                ", order=" + this.getOrder() +
                ", answerRequired=" + this.answerRequired +
                '}';
    }
}
