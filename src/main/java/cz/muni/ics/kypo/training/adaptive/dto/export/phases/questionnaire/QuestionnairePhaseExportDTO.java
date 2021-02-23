package cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire;

import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;

import java.util.List;
import java.util.Objects;

/**
 * Encapsulates information about questionnaire phase. Inherits from {@link AbstractPhaseExportDTO}
 */
@ApiModel(value = "QuestionnairePhaseExportDTO", description = "Exported questionnaire phase.", parent = AbstractPhaseExportDTO.class)
public class QuestionnairePhaseExportDTO extends AbstractPhaseExportDTO {

    private List<QuestionExportDTO> questions;
    private QuestionnaireType questionnaireType;
    private List<QuestionPhaseRelationExportDTO> phaseRelations;

    public List<QuestionExportDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionExportDTO> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionPhaseRelationExportDTO> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationExportDTO> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionnairePhaseExportDTO)) return false;
        if (!super.equals(o)) return false;
        QuestionnairePhaseExportDTO that = (QuestionnairePhaseExportDTO) o;
        return getQuestionnaireType() == that.getQuestionnaireType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getQuestionnaireType());
    }
}
