package com.example.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

@Entity
public class QuestionnairePhaseRelation {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "order_in_questionnaire", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionnaireLevel relatedPhase;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "questionnaire_phase_relation_question",
            joinColumns = @JoinColumn(name = "question_phase_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions;

    private int successRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public QuestionnaireLevel getRelatedPhase() {
        return relatedPhase;
    }

    public void setRelatedPhase(QuestionnaireLevel relatedPhase) {
        this.relatedPhase = relatedPhase;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }
}
