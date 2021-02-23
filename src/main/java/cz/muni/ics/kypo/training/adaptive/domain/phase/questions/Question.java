package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;

import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "question")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionGenerator")
    @SequenceGenerator(name = "questionGenerator", sequenceName = "question_seq")
    @Column(name = "question_id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String text;

    @Column(name = "order_in_questionnaire")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_phase_id")
    private QuestionnairePhase questionnairePhase;

    @OrderBy
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
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
