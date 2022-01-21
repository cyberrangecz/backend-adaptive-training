package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.List;

@ApiModel(
        value = "QuestionnairePhaseAnswersDTO"
)
public class QuestionnairePhaseAnswersDTO {

    @Valid
    @ApiModelProperty(value = "Answers to the questionnaire provided by user", required = true)
    private List<QuestionAnswerDTO> answers;

    public List<QuestionAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerDTO> answers) {
        this.answers = answers;
    }
}
