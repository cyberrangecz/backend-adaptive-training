package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class QuestionRequiredIdDto extends AbstractQuestionDto {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    @NotNull(message = "Question ID must be specified")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
