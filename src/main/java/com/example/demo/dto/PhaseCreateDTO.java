package com.example.demo.dto;

import com.example.demo.enums.PhaseType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class PhaseCreateDTO {

    @ApiModelProperty(value = "Type of phase.", required = true, allowableValues = "QUESTIONNAIRE, INFO, GAME", example = "GAME")
    @NotNull(message = "Phase type must be specified")
    private PhaseType phaseType;

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    @Override
    public String toString() {
        return "PhaseCreateDTO{" +
                "phaseType=" + phaseType +
                '}';
    }
}
