package cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.preview;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

public class QuestionnairePhasePreviewDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "List of questions associated with the questionnaire phase")
    private List<QuestionPreviewDTO> questions;
    @ApiModelProperty(value = "Type of questionnaire phase", allowableValues = "ADAPTIVE,GENERAL", example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;

    public List<QuestionPreviewDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionPreviewDTO> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnairePhasePreviewDTO that = (QuestionnairePhasePreviewDTO) o;
        return Objects.equals(questions, that.questions) &&
                questionnaireType == that.questionnaireType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions, questionnaireType);
    }

    @Override
    public String toString() {
        return "QuestionnairePhasePreviewDTO{" +
                "questions=" + questions +
                ", questionnaireType=" + questionnaireType +
                "} " + super.toString();
    }
}
