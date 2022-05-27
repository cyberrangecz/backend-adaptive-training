package cz.muni.ics.kypo.training.adaptive.dto.questionnaire.preview;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class QuestionPreviewDTO {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    private QuestionType questionType;
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoicePreviewDTO> choices;
    @ApiModelProperty(value = "User answers to the question", example = "[\"An answer\"]")
    private Set<String> userAnswers;

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

    public List<QuestionChoicePreviewDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoicePreviewDTO> choices) {
        this.choices = choices;
    }

    public Set<String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(Set<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionPreviewDTO)) return false;
        QuestionPreviewDTO that = (QuestionPreviewDTO) o;
        return getOrder() == that.getOrder() &&
                getId().equals(that.getId()) &&
                getText().equals(that.getText()) &&
                getQuestionType() == that.getQuestionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getText());
    }

    @Override
    public String toString() {
        return "QuestionPreviewDTO{" +
                "id=" + id +
                ", order=" + order +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", userAnswers=" + userAnswers +
                '}';
    }
}
