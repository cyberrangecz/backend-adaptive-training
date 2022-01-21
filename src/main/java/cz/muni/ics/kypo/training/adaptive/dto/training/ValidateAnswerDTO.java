package cz.muni.ics.kypo.training.adaptive.dto.training;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@ApiModel(
        value = "ValidateAnswerDTO",
        description = "Encapsulation of the answer submitted in training phase."
)
public class ValidateAnswerDTO {
    @ApiModelProperty(value = "Answer to be validated.", required = true, example = "answer")
    @NotEmpty(message = "{validateAnswer.answer.NotEmpty.message}")
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidateAnswerDTO)) return false;
        ValidateAnswerDTO that = (ValidateAnswerDTO) o;
        return Objects.equals(getAnswer(), that.getAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswer());
    }

    @Override
    public String toString() {
        return "ValidateAnswerDTO{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
