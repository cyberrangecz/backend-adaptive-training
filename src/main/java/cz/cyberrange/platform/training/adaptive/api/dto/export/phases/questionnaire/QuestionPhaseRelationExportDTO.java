package cz.cyberrange.platform.training.adaptive.api.dto.export.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.Set;

@ApiModel(
        value = "QuestionPhaseRelationExportDTO"
)
public class QuestionPhaseRelationExportDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private Integer order;
    @ApiModelProperty(value = "Set of orders of questions related to the specified questionnaire")
    private Set<Integer> questionOrders;
    @ApiModelProperty(value = "Order of training phase to which the questions are related of question", required = true, example = "1")
    private Integer phaseOrder;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50")
    private int successRate;

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
        if (!(o instanceof QuestionPhaseRelationExportDTO)) return false;
        QuestionPhaseRelationExportDTO that = (QuestionPhaseRelationExportDTO) o;
        return getSuccessRate() == that.getSuccessRate() &&
                Objects.equals(getOrder(), that.getOrder()) &&
                Objects.equals(getQuestionOrders(), that.getQuestionOrders()) &&
                Objects.equals(getPhaseOrder(), that.getPhaseOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getQuestionOrders(), getPhaseOrder(), getSuccessRate());
    }

    @Override
    public String toString() {
        return "QuestionPhaseRelationExportDTO{" +
                "order=" + order +
                ", questionOrders=" + questionOrders +
                ", phaseOrder=" + phaseOrder +
                ", successRate=" + successRate +
                '}';
    }
}
