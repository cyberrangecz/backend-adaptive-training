package cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

public class QuestionArchiveDTO {

    @ApiModelProperty(value = "Main identifier of question.", example = "1")
    protected Long id;
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    private QuestionType questionType;
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceArchiveDTO> choices;

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

    public List<QuestionChoiceArchiveDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceArchiveDTO> choices) {
        this.choices = choices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionArchiveDTO)) return false;
        QuestionArchiveDTO that = (QuestionArchiveDTO) o;
        return getOrder() == that.getOrder() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getText(), that.getText()) &&
                getQuestionType() == that.getQuestionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getText(), getQuestionType());
    }

    @Override
    public String toString() {
        return "QuestionArchiveDTO{" +
                "id=" + id +
                ", order=" + order +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                '}';
    }
}
