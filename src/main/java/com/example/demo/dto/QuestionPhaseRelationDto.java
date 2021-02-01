package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class QuestionPhaseRelationDto {

    @ApiModelProperty(value = "Question-Phase relation ID. Leave blank if a new one is added", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "Question order must be specified")
    private Integer order;

    @Valid
    @ApiModelProperty(value = "Set of questions related to the specified questionnaire")
    private Set<QuestionRequiredIdDto> questions;

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

    public Set<QuestionRequiredIdDto> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionRequiredIdDto> questions) {
        this.questions = questions;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }
}
