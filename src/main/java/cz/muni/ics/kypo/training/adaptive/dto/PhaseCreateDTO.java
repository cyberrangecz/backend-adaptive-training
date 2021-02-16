package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.enums.PhaseTypeCreate;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class PhaseCreateDTO {

    @ApiModelProperty(value = "Type of phase.", required = true, allowableValues = "QUESTIONNAIRE_ADAPTIVE, QUESTIONNAIRE_GENERAL, INFO, GAME", example = "TRAINING")
    @NotNull(message = "Phase type must be specified")
    private PhaseTypeCreate phaseType;

    public PhaseTypeCreate getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(PhaseTypeCreate phaseType) {
        this.phaseType = phaseType;
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
                '}';
    }
}
