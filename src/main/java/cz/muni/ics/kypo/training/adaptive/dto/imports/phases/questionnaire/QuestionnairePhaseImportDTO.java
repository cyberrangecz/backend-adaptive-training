package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire;

import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.AbstractPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Encapsulates information about questionnaire phase. Inherits from {@link QuestionnairePhaseImportDTO}
 */
@ApiModel(
        value = "QuestionnairePhaseImportDTO",
        description = "Imported questionnaire phase.",
        parent = QuestionnairePhaseImportDTO.class
)
public class QuestionnairePhaseImportDTO extends AbstractPhaseImportDTO {

    @ApiModelProperty(value = "The type of the questionnaire", required = true, example = "ADAPTIVE")
    @NotNull(message = "{questionnairePhase.questionnaireType.NotNull.message}")
    private QuestionnaireType questionnaireType;
    @Valid
    @ApiModelProperty(value = "Questions in the questionnaire", required = true, position = 1)
    private List<QuestionImportDTO> questions;
    @Valid
    @ApiModelProperty(value = "The relation between questions in the questionnaire and phase in the training definition", required = true, position = 2)
    private List<QuestionPhaseRelationImportDTO> phaseRelations;

    /**
     * Gets questions.
     *
     * @return the questions
     */
    public List<QuestionImportDTO> getQuestions() {
        return questions;
    }

    /**
     * Sets questions.
     *
     * @param questions the questions
     */
    public void setQuestions(List<QuestionImportDTO> questions) {
        this.questions = questions;
    }

    /**
     * Gets phase relations.
     *
     * @return the relations
     */
    public List<QuestionPhaseRelationImportDTO> getPhaseRelations() {
        return phaseRelations;
    }

    /**
     * Sets phase relations.
     *
     * @param phaseRelations the relations
     */
    public void setPhaseRelations(List<QuestionPhaseRelationImportDTO> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    @Override
    public String toString() {
        return "QuestionnairePhaseImportDTO{" +
                "questions=" + questions +
                ", phaseRelations=" + phaseRelations +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }
}
