package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@ApiModel(
        value = "QuestionPhaseRelationImport"
)
@Data
public class QuestionPhaseRelationImport {
    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "{questionPhaseRelationImport.order.NotNull.message}")
    private Integer order;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1", position = 1)
    @NotNull(message = "{questionPhaseRelationImport.phaseOrder.NotNull.message}")
    private Integer phaseOrder;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50", position = 2)
    @NotNull(message = "{questionPhaseRelationImport.successRate.NotNull.message}")
    private int successRate;
    @ApiModelProperty(value = "Set of orders of questions related to the specified questionnaire", position = 3)
    @NotNull(message = "{questionPhaseRelationImport.questionOrders.NotNull.message}")
    private Set<Integer> questionOrders;
    @ApiModelProperty(value = "Set of ids of questions related to the specified questionnaire", position = 3)
    private Set<Integer> questionIds;
    @ApiModelProperty(value = "Id of phase to which the question is relate", position = 3)
    @NotNull(message = "{questionPhaseRelationImport.phaseId.NotNull.message}")
    private Long phaseId;
}
