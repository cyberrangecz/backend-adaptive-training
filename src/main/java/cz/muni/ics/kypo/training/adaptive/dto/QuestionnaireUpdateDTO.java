package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class QuestionnaireUpdateDTO {

    @ApiModelProperty(value = "Title of questionnaire", required = true, example = "Entrance test")
    @NotEmpty(message = "Questionnaire title must not be blank")
    private String title;

    @ApiModelProperty(value = "Type of questionnaire", required = true, allowableValues = "ADAPTIVE, GENERAL", example = "ADAPTIVE")
    @NotNull(message = "Questionnaire type must be specified")
    private QuestionnaireType questionnaireType;

    @Valid
    @ApiModelProperty(value = "Questions in the questionnaire", required = true)
    private List<QuestionDTO> questions;

    @Valid
    @ApiModelProperty(value = "The relation between questions in the questionnaire and phases in the training definition", required = true)
    private List<QuestionPhaseRelationDTO> phaseRelations;

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

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public List<QuestionPhaseRelationDTO> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationDTO> phaseRelations) {
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
