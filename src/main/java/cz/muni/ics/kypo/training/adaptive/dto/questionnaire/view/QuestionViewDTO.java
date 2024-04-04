package cz.muni.ics.kypo.training.adaptive.dto.questionnaire.view;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class QuestionViewDTO {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    private QuestionType questionType;
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceViewDTO> choices;
    @ApiModelProperty(value = "Sign if the question must be answered by the participant or not.", example = "true")
    private boolean answerRequired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType type) {
        this.questionType = type;
    }

    public List<QuestionChoiceViewDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceViewDTO> choices) {
        this.choices = choices;
    }

    public boolean isAnswerRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionViewDTO)) return false;
        QuestionViewDTO that = (QuestionViewDTO) o;
        return getOrder() == that.getOrder() &&
                getId().equals(that.getId()) &&
                getText().equals(that.getText()) &&
                getQuestionType() == that.getQuestionType() &&
                isAnswerRequired() == that.isAnswerRequired();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getText(), isAnswerRequired());
    }

    @Override
    public String toString() {
        return "QuestionViewDTO{" +
                "id=" + id +
                ", order=" + order +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", answerRequired=" + answerRequired +
                '}';
    }
}
