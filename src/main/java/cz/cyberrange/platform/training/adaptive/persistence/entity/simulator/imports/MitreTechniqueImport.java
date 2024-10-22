package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        value = "TrainingPhaseImport",
        description = "Imported training phase."
)
@Data
public class MitreTechniqueImport {
    @ApiModelProperty(value = "Mitre technique key identifying the mitre technique", example = "TA0043.T1590")
    String techniqueKey;
}
