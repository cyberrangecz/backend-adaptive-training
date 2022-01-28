package cz.muni.ics.kypo.training.adaptive.dto.access;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class ValidatePasskeyDTO {
    @ApiModelProperty(value = "Passkey to be validated.", required = true, example = "passkey")
    @NotEmpty(message = "{validatePasskey.passkey.NotEmpty.message}")
    private String passkey;

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }
}
