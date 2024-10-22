package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireActions {
    @ApiModelProperty(value = "Question associated with the questionnaire phase")
    @NotEmpty(message = "{questionnaireActions.question.NotEmpty.message}")
    private String question;
    @ApiModelProperty(value = "List of answers associated with the question")
    @NotNull(message = "{questionnaireActions.answer.NotEmpty.message}")
    private List<String> answer = new ArrayList<>();
}
