package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbstractQuestionImport {
    @ApiModelProperty(value = "Order of question", required = true, example = "0")
    private int order;
    @ApiModelProperty(value = "The question that will be displayed to a player", required = true, example = "What's the capital of Canada?")
    private String text;
    @ApiModelProperty(value = "It defines the type of the question", allowableValues = "FFQ, MCQ, RFQ", required = true, example = "MCQ")
    @NotEmpty(message = "{questionImport.questionType.NotEmpty.message}")
    private QuestionType questionType;
    @ApiModelProperty(value = "Choices that are distributed with the question", required = true)
    private List<QuestionChoiceImport> choices = new ArrayList<>();
}
