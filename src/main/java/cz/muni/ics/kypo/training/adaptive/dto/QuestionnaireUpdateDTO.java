package cz.muni.ics.kypo.training.adaptive.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class QuestionnaireUpdateDTO {

    @ApiModelProperty(value = "Title of questionnaire", required = true, example = "Entrance test")
    @NotEmpty(message = "Questionnaire title must not be blank")
    private String title;

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
                ", questions=" + questions +
                ", phaseRelations=" + phaseRelations +
                '}';
    }
}
