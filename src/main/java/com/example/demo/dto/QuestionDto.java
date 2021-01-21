package com.example.demo.dto;

import com.example.demo.enums.QuestionType;

import java.io.Serializable;
import java.util.List;

public class QuestionDto implements Serializable {

    private Long id;

    private QuestionType questionType;
    private String text;
    private Integer points;
    private Integer penalty;
    private boolean required;
    private Integer order;
    private List<QuestionChoiceDto> choices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<QuestionChoiceDto> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceDto> choices) {
        this.choices = choices;
    }
}
