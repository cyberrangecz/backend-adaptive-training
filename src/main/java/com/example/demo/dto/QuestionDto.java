package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class QuestionDto extends AbstractQuestionDto implements Serializable {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
