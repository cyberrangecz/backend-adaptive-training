package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = "AbstractQuestionDTO"
)
public abstract class AbstractQuestionDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @Min(value = 0, message = "{question.order.Min.message}")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    @NotEmpty(message = "{question.text.NotEmpty.message}")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    @NotNull(message = "{question.questionType.NotNull.message}")
    private QuestionType questionType;
    @Valid
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceDTO> choices = new ArrayList<>();
    @ApiModelProperty(value = "Sign if the question must be answered by the participant or not.", example = "true")
    @NotNull(message = "{question.answerRequired.NotNull.message}")
    private boolean answerRequired;

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

    public List<QuestionChoiceDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceDTO> choices) {
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
        if (o == null || getClass() != o.getClass()) return false;
        AbstractQuestionDTO that = (AbstractQuestionDTO) o;
        return order == that.order &&
                answerRequired == that.answerRequired &&
                Objects.equals(text, that.text) &&
                questionType == that.questionType &&
                Objects.equals(choices, that.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, text, questionType, choices, answerRequired);
    }

    @Override
    public String toString() {
        return "AbstractQuestionDTO{" +
                "order=" + order +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", choices=" + choices +
                '}';
    }
}
