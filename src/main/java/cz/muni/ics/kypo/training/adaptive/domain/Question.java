package cz.muni.ics.kypo.training.adaptive.domain;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Question implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String text;

    @Column(name = "order_in_questionnaire")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionnairePhase questionnairePhase;

    @OrderBy
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionChoice> choices = new ArrayList<>();

    @ManyToMany(mappedBy = "questions", fetch = FetchType.LAZY)
    private Set<QuestionPhaseRelation> questionPhaseRelations = new HashSet<>();

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
}
