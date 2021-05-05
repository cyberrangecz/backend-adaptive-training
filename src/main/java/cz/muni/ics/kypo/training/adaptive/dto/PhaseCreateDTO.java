package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.annotations.validation.NotNullQuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel(
        value = "PhaseCreateDTO",
        description = "Specification of the phase to create."
)
@NotNullQuestionnaireType
public class PhaseCreateDTO {

    @ApiModelProperty(value = "Type of phase.", required = true, allowableValues = "QUESTIONNAIRE, INFO, TRAINING", example = "TRAINING")
    @NotNull(message = "{phase.phaseType.NotNull.message}")
    private PhaseType phaseType;

    @ApiModelProperty(value = "Type of questionnaire.", allowableValues = "ADAPTIVE, GENERAL", example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
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
        PhaseCreateDTO that = (PhaseCreateDTO) o;
        return phaseType == that.phaseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phaseType);
    }

    @Override
    public String toString() {
        return "PhaseCreateDTO{" +
                "phaseType=" + phaseType +
                ", questionnaireType=" + questionnaireType +
                '}';
    }
}
