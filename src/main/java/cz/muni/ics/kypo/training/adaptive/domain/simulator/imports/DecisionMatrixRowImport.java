package cz.muni.ics.kypo.training.adaptive.domain.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(
        value = "DecisionMatrixRowImport"
)
@Data
public class DecisionMatrixRowImport {
    @ApiModelProperty(value = "ID of row in a decision matrix", example = "1")
    @NotNull(message = "{decisionMatrixRowImport.id.NotEmpty.message}")
    private Long id;
    @ApiModelProperty(value = "Order of row in a decision matrix", required = true, example = "1")
    @NotNull(message = "{decisionMatrixRowImport.order.NotNull.message}")
    private int order;
    @ApiModelProperty(value = "It determines how important the answers of the questions in questionnaires are", required = true, example = "0.5")
    @NotNull(message = "{decisionMatrixRowImport.importance.NotNull.message}")
    private double questionnaireAnswered;
    @ApiModelProperty(value = "It determines how important it is whether the player used the keyword", required = true, example = "0.5")
    @NotNull(message = "{decisionMatrixRowImport.importance.NotNull.message}")
    private double keywordUsed;
    @ApiModelProperty(value = "It determines how important it is whether the player completed the task in time", required = true, example = "0.5")
    @NotNull(message = "{decisionMatrixRowImport.importance.NotNull.message}")
    private double completedInTime;
    @ApiModelProperty(value = "It determines how important it is whether the player displayed the solution of the task they were solving", required = true, example = "0.5")
    @NotNull(message = "{decisionMatrixRowImport.importance.NotNull.message}")
    private double solutionDisplayed;
    @ApiModelProperty(value = "It determines how important the number of wrong answers are", required = true, example = "0.5")
    @NotNull(message = "{decisionMatrixRowImport.importance.NotNull.message}")
    private double wrongAnswers;
}
