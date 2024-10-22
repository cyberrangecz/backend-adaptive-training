package cz.cyberrange.platform.training.adaptive.api.dto.export.phases.questionnaire;

import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(
        value = "QuestionExportDTO"
)
public class QuestionExportDTO {

    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    private QuestionType questionType;
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceExportDTO> choices;

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

    public List<QuestionChoiceExportDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionChoiceExportDTO> choices) {
        this.choices = choices;
    }
}
