package cz.muni.ics.kypo.training.adaptive.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class QuestionPhaseRelationDTO {

    @ApiModelProperty(value = "Question-Phase relation ID. Leave blank if a new one is added", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "Question order must be specified")
    private Integer order;

    @ApiModelProperty(value = "Set of IDs of questions related to the specified questionnaire")
    private Set<Long> questionIds;

    @ApiModelProperty(value = "ID of training phase to which the questions are related of question", required = true, example = "1")
    @NotNull(message = "Phase ID in question-phase relations must not be null")
    private Long phaseId;

    @ApiModelProperty(value = "Percentage that defines whether a player was successful or not ", required = true, example = "50")
    @Min(value = 0, message = "Success rate must not be lower than 0 %")
    @Max(value = 100, message = "Success rate must not be higher than 100 %")
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
}
