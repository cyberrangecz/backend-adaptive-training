package cz.muni.ics.kypo.training.adaptive.domain.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(
        value = "InfoPhaseImport",
        description = "An imported info phase.",
        parent = AbstractPhaseImport.class
)
@Data
public class InfoPhaseImport extends AbstractPhaseImport {
    @ApiModelProperty(value = "The information and experiences that are directed towards a participant.", example = "Informational stuff")
    private String content;
}
