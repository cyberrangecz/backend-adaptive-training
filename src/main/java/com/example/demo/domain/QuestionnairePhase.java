package com.example.demo.domain;

import com.example.demo.enums.QuestionnaireType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
public class QuestionnairePhase extends AbstractPhase {

    @Enumerated(EnumType.STRING)
    private QuestionnaireType questionnaireType;

    @OrderBy
    @OneToMany(mappedBy = "questionnairePhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions;

    @OrderBy
    @OneToMany(mappedBy = "relatedPhase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionnairePhaseRelation> questionnairePhaseRelations;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionnairePhaseRelation> getQuestionnairePhaseRelations() {
        return questionnairePhaseRelations;
    }

    public void setQuestionnairePhaseRelations(List<QuestionnairePhaseRelation> questionnairePhaseRelations) {
        this.questionnairePhaseRelations = questionnairePhaseRelations;
    }
}
