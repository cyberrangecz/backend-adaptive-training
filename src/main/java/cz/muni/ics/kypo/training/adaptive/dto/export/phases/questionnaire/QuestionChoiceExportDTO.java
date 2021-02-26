package cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(
        value = "QuestionChoiceExportDTO"
)
public class QuestionChoiceExportDTO {

    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    private String text;
    @ApiModelProperty(value = "It defines whether this answer is correct or not", required = true, example = "true")
    private Boolean correct;
    @ApiModelProperty(value = "Order of question choice", required = true, example = "0")
    private Integer order;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
