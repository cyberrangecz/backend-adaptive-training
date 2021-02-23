package cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.Set;

public class QuestionPhaseRelationExportDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private Integer order;
    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire")
    private Set<Long> questionIds;
    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1")
    private Long phaseId;
    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50")
    private int successRate;

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
        if (!(o instanceof QuestionPhaseRelationExportDTO)) return false;
        QuestionPhaseRelationExportDTO that = (QuestionPhaseRelationExportDTO) o;
        return getSuccessRate() == that.getSuccessRate() &&
                Objects.equals(getOrder(), that.getOrder()) &&
                Objects.equals(getQuestionIds(), that.getQuestionIds()) &&
                Objects.equals(getPhaseId(), that.getPhaseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getQuestionIds(), getPhaseId(), getSuccessRate());
    }

    @Override
    public String toString() {
        return "QuestionPhaseRelationExportDTO{" +
                "order=" + order +
                ", questionIds=" + questionIds +
                ", phaseId=" + phaseId +
                ", successRate=" + successRate +
                '}';
    }
}
