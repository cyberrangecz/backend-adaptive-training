package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(
        value = "AccessPhaseImport",
        description = "Imported access phase.",
        parent = AbstractPhaseImport.class
)
@Data
public class AccessPhaseImport extends AbstractPhaseImport {
    @ApiModelProperty(value = "Keyword used for access next level.", example = "secretAnswer")
    private String passkey;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in cloud environment.", example = "Connect using SSH config.")
    private String cloudContent;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in local (non-cloud) environment.", example = "Use vagrant SSH connection.")
    private String localContent;
}
