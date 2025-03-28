package cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.questionnaire;

import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * Encapsulates information about c phase. Inherits from {@link AbstractPhaseArchiveDTO}
 * Used for archiving.
 */
@ApiModel(
        value = "QuestionnairePhaseArchiveDTO",
        description = "Archived questionnaire phase.",
        parent = AbstractPhaseArchiveDTO.class
)
public class QuestionnairePhaseArchiveDTO extends AbstractPhaseArchiveDTO {

    @ApiModelProperty(value = "List of questions associated with the questionnaire phase")
    private List<QuestionDTO> questions;
    @ApiModelProperty(value = "Type of questionnaire phase", allowableValues = "ADAPTIVE,GENERAL", example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;
    @ApiModelProperty(value = "List of relations between questions and a training phase")
    private List<QuestionPhaseRelationArchiveDTO> phaseRelations;

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

    public List<QuestionPhaseRelationArchiveDTO> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationArchiveDTO> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }

    @Override
    public String toString() {
        return "QuestionnairePhaseArchiveDTO{" +
                "questionnaireType=" + questionnaireType +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnairePhaseArchiveDTO)) return false;
        QuestionnairePhaseArchiveDTO that = (QuestionnairePhaseArchiveDTO) o;
        return Objects.equals(getQuestions(), that.getQuestions()) &&
                getQuestionnaireType() == that.getQuestionnaireType() &&
                Objects.equals(getPhaseRelations(), that.getPhaseRelations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestions(), getQuestionnaireType(), getPhaseRelations());
    }
}
