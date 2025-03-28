package cz.cyberrange.platform.training.adaptive.api.dto.training.technique;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;


@ApiModel(value = "MitreTechniqueDTO", description = "Represent 'how' an trainee achieves a tactical goal of the training level by performing an action.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MitreTechniqueDTO {

    @ApiModelProperty(value = "Main identifier of Mitre technique.", required = true, example = "1")
    private Long id;
    @ApiModelProperty(example = "T1548.001")
    @NotEmpty(message = "{mitreTechnique.techniqueKey.NotEmpty.message}")
    private String techniqueKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTechniqueKey() {
        return techniqueKey;
    }

    public void setTechniqueKey(String techniqueKey) {
        this.techniqueKey = techniqueKey;
    }
}
