package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class QuestionAnswerDTO {

    @ApiModelProperty(value = "ID of answered question", example = "1")
    @NotNull(message = "ID of the answered question must not be null")
    private Long questionId;
    @ApiModelProperty(value = "Answer to the question", example = "An answer")
    private String answer;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public String toString() {
        return "QuestionAnswerDTO{" +
                "questionId=" + questionId +
                ", answer='" + answer + '\'' +
                '}';
    }
}
