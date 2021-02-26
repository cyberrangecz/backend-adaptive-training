package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
@ApiModel(
        value = "QuestionPhaseRelationImportDTO"
)
public class QuestionPhaseRelationImportDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0"
    )
    @NotNull(message = "{questionnairePhaseRelation.order.NotNull.message}")
    @Min(value = 0, message = "{questionnairePhaseRelation.order.Min.message}")
    private Integer order;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1", position = 1)
    @NotNull(message = "{questionnairePhaseRelation.phaseId.NotNull.message}")
    @Min(value = 0, message = "{questionnairePhaseRelation.phaseId.Min.message}")
    private Long phaseId;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50", position = 2)
    @Min(value = 0, message = "{questionnairePhaseRelation.successRate.Min.message}")
    @Max(value = 100, message = "{questionnairePhaseRelation.successRate.Max.message}")
    private int successRate;
    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire", position = 3)
    @NotNull(message = "{questionnairePhaseRelation.questionIds.NotNull.message}")
    @Size(min = 1, message = "{questionnairePhaseRelation.questionIds.Size.message}")
    private Set<Long> questionIds;

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
        if (!(o instanceof QuestionPhaseRelationImportDTO)) return false;
        QuestionPhaseRelationImportDTO that = (QuestionPhaseRelationImportDTO) o;
        return getSuccessRate() == that.getSuccessRate() &&
                getOrder().equals(that.getOrder()) &&
                getQuestionIds().equals(that.getQuestionIds()) &&
                getPhaseId().equals(that.getPhaseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getQuestionIds(), getPhaseId(), getSuccessRate());
    }

    @Override
    public String toString() {
        return "QuestionPhaseRelationImportDTO{" +
                "order=" + order +
                ", questionIds=" + questionIds +
                ", phaseId=" + phaseId +
                ", successRate=" + successRate +
                '}';
    }
}
