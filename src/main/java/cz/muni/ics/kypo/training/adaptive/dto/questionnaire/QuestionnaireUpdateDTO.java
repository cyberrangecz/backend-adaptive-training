package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnaireUpdateDTO that = (QuestionnaireUpdateDTO) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(phaseRelations, that.phaseRelations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, questions, phaseRelations);
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
