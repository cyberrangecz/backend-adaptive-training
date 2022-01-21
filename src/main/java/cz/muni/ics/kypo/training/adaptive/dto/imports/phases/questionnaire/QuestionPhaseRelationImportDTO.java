package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@ApiModel(
        value = "QuestionPhaseRelationImportDTO"
)
public class QuestionPhaseRelationImportDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "{questionPhaseRelation.order.NotNull.message}")
    @Min(value = 0, message = "{questionPhaseRelation.order.Min.message}")
    private Integer order;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1", position = 1)
    @NotNull(message = "{questionPhaseRelation.phaseOrder.NotNull.message}")
    @Min(value = 0, message = "{questionPhaseRelation.phaseOrder.Min.message}")
    private Integer phaseOrder;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50", position = 2)
    @Min(value = 0, message = "{questionPhaseRelation.successRate.Min.message}")
    @Max(value = 100, message = "{questionPhaseRelation.successRate.Max.message}")
    private int successRate;
    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire", position = 3)
    @NotNull(message = "{questionPhaseRelation.questionOrders.NotNull.message}")
    private Set<Integer> questionOrders;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<Integer> getQuestionOrders() {
        return questionOrders;
    }

    public void setQuestionOrders(Set<Integer> questionOrders) {
        this.questionOrders = questionOrders;
    }

    public Integer getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(Integer phaseOrder) {
        this.phaseOrder = phaseOrder;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionPhaseRelationImportDTO)) return false;
        QuestionPhaseRelationImportDTO that = (QuestionPhaseRelationImportDTO) o;
        return getSuccessRate() == that.getSuccessRate() &&
                getOrder().equals(that.getOrder()) &&
                getQuestionOrders().equals(that.getQuestionOrders()) &&
                getPhaseOrder().equals(that.getPhaseOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getQuestionOrders(), getPhaseOrder(), getSuccessRate());
    }

    @Override
    public String toString() {
        return "QuestionPhaseRelationImportDTO{" +
                "order=" + order +
                ", questionOrders=" + questionOrders +
                ", phaseOrder=" + phaseOrder +
                ", successRate=" + successRate +
                '}';
    }
}
