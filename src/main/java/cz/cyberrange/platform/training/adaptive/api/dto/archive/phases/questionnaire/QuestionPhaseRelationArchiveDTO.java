package cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

@ApiModel(
        value = "QuestionPhaseRelationArchiveDTO"
)
public class QuestionPhaseRelationArchiveDTO {

    @ApiModelProperty(value = "Question-Phase relation ID. Leave blank if a new one is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private Integer order;
    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire")
    private Set<Long> questionIds;
    @ApiModelProperty(value = "Set of orders of questions related to the specified questionnaire")
    private Set<Integer> questionOrders;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1")
    private Long phaseId;
    @ApiModelProperty(value = "Order of training phase to which the questions are related of question", required = true, example = "1")
    private Integer phaseOrder;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50")
    private int successRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(Set<Long> questionIds) {
        this.questionIds = questionIds;
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

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }


    @Override
    public String toString() {
        return "QuestionPhaseRelationArchiveDTO{" +
                "id=" + id +
                ", order=" + order +
                ", questionIds=" + questionIds +
                ", phaseId=" + phaseId +
                ", questionOrders=" + questionOrders +
                ", phaseOrder=" + phaseOrder +
                ", successRate=" + successRate +
                '}';
    }
}
