package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
@ApiModel(
        value = "QuestionImportDTO"
)
public class QuestionImportDTO {

    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    @NotEmpty(message = "{question.text.NotEmpty.message}")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ", position = 1)
    @NotNull(message = "{question.questionType.NotNull.message}")
    private QuestionType questionType;
    @ApiModelProperty(value = "Order of question", required = true, example = "0", position = 2)
    @NotNull(message = "{question.order.NotNull.message}")
    @Min(value = 0, message = "{question.order.Min.message}")
    private Integer order;
    @Valid
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true, position = 3)
    private List<QuestionChoiceImportDTO> choices;

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

    public List<QuestionChoiceImportDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceImportDTO> choices) {
        this.choices = choices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionImportDTO)) return false;
        QuestionImportDTO that = (QuestionImportDTO) o;
        return getOrder() == that.getOrder() &&
                Objects.equals(getText(), that.getText()) &&
                getQuestionType() == that.getQuestionType() &&
                Objects.equals(getChoices(), that.getChoices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getText(), getQuestionType(), getChoices());
    }

    @Override
    public String toString() {
        return "QuestionImportDTO{" +
                "order=" + order +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", choices=" + choices +
                '}';
    }
}
