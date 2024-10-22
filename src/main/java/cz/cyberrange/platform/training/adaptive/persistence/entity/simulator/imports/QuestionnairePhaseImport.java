package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(
        value = "QuestionnairePhaseImport",
        description = "Imported questionnaire phase.",
        parent = QuestionnairePhaseImport.class
)
@Data
public class QuestionnairePhaseImport extends AbstractPhaseImport {
    @ApiModelProperty(value = "The type of the questionnaire", required = true, example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;
    @ApiModelProperty(value = "Questions in the questionnaire", required = true, position = 1)
    private List<QuestionImport> questions;
    @ApiModelProperty(value = "The relation between questions in the questionnaire and phase in the training definition", required = true, position = 2)
    private List<QuestionPhaseRelationImport> phaseRelations;
}
