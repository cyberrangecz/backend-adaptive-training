package cz.cyberrange.platform.training.adaptive.api.dto.questionnaire;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

@ApiModel(
        value = "QuestionnairePhaseDTO"
)
public class QuestionnairePhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "List of questions associated with the questionnaire phase")
    private List<QuestionDTO> questions;
    @ApiModelProperty(value = "Type of questionnaire phase", allowableValues = "ADAPTIVE,GENERAL", example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;
    @ApiModelProperty(value = "List of relations between questions and a training phase")
    private List<QuestionPhaseRelationDTO> phaseRelations;

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
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
        QuestionnairePhaseDTO that = (QuestionnairePhaseDTO) o;
        return Objects.equals(questions, that.questions) &&
                questionnaireType == that.questionnaireType &&
                Objects.equals(phaseRelations, that.phaseRelations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions, questionnaireType, phaseRelations);
    }

    @Override
    public String toString() {
        return "QuestionnairePhaseDTO{" +
                "questions=" + questions +
                ", questionnaireType=" + questionnaireType +
                ", phaseRelations=" + phaseRelations +
                "} " + super.toString();
    }
}
