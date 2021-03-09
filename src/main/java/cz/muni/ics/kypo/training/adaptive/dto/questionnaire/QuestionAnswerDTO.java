package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Set;

@ApiModel(
        value = "QuestionAnswerDTO"
)
public class QuestionAnswerDTO {

    @ApiModelProperty(value = "ID of answered question", example = "1")
    @NotNull(message = "ID of the answered question must not be null")
    private Long questionId;
    @ApiModelProperty(value = "Answer to the question", example = "[\"An answer\"]")
    private Set<String> answers;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Set<String> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuestionAnswerDTO{" +
                "questionId=" + questionId +
                ", answers='" + answers + '\'' +
                '}';
    }
}
