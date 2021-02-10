package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import cz.muni.ics.kypo.training.adaptive.enums.QuestionType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class AbstractQuestionDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    @NotNull(message = "Question order must be specified")
    private int order;

    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    @NotEmpty(message = "Text of question must not be blank")
    private String text;

    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    @NotNull(message = "Question type must be specified")
    private QuestionType questionType;

    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceDTO> choices;

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
}
