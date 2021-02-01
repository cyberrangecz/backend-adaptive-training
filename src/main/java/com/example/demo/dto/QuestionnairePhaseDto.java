package com.example.demo.dto;

import com.example.demo.enums.QuestionnaireType;

import java.util.List;

public class QuestionnairePhaseDto extends AbstractPhaseDto {

    private List<QuestionDto> questions;
    private QuestionnaireType questionnaireType;
    private List<QuestionPhaseRelationDto> phaseRelations;

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionPhaseRelationDto> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationDto> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }
}
