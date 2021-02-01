package com.example.demo.dto;

import com.example.demo.enums.QuestionnaireType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class QuestionnaireUpdateDto {

    @ApiModelProperty(value = "Title of questionnaire", required = true, example = "Entrance test")
    @NotEmpty(message = "Questionnaire title must not be blank")
    private String title;

    @ApiModelProperty(value = "Type of questionnaire", required = true, allowableValues = "ADAPTIVE, GENERAL", example = "ADAPTIVE")
    @NotNull(message = "Questionnaire type must be specified")
    private QuestionnaireType questionnaireType;

    @Valid
    @ApiModelProperty(value = "Questions in the questionnaire", required = true)
    private List<QuestionDto> questions;

    @Valid
    @ApiModelProperty(value = "The relation between questions in the questionnaire and phases in the training definition", required = true)
    private List<QuestionPhaseRelationDto> phaseRelations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public List<QuestionPhaseRelationDto> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationDto> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }

    @Override
    public String toString() {
        return "QuestionnaireUpdateDto{" +
                "title='" + title + '\'' +
                ", questionnaireType=" + questionnaireType +
                ", questions=" + questions +
                ", phaseRelations=" + phaseRelations +
                '}';
    }
}
