package cz.cyberrange.platform.training.adaptive.api.dto.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@ApiModel(
        value = "QuestionPhaseRelationDTO"
)
public class QuestionPhaseRelationDTO {

    @ApiModelProperty(value = "Question-Phase relation ID. Leave blank if a new one is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "{questionPhaseRelation.order.NotNull.message}")
    @Min(value = 0, message = "{questionPhaseRelation.order.Min.message}")
    private Integer order;
    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire")
    @NotNull(message = "{questionPhaseRelation.questionIds.NotNull.message}")
    private Set<Long> questionIds;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1")
    @NotNull(message = "{questionPhaseRelation.phaseId.NotNull.message}")
    private Long phaseId;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50")
    @Min(value = 0, message = "{questionPhaseRelation.successRate.Min.message}")
    @Max(value = 100, message = "{questionPhaseRelation.successRate.Max.message}")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionPhaseRelationDTO that = (QuestionPhaseRelationDTO) o;
        return successRate == that.successRate &&
                Objects.equals(id, that.id) &&
                Objects.equals(order, that.order) &&
                Objects.equals(questionIds, that.questionIds) &&
                Objects.equals(phaseId, that.phaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, questionIds, phaseId, successRate);
    }

    @Override
    public String toString() {
        return "QuestionPhaseRelationDTO{" +
                "id=" + id +
                ", order=" + order +
                ", questionIds=" + questionIds +
                ", phaseId=" + phaseId +
                ", successRate=" + successRate +
                '}';
    }
}
