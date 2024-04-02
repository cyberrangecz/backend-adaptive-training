package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel(
        value = "QuestionDTO"
)
public class QuestionDTO extends AbstractQuestionDTO {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Sign if the question must be answered by the participant or not.", example = "true")
    @NotNull(message = "{question.answerRequired.NotNull.message}")
    private boolean answerRequired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAnswerRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(answerRequired, that.answerRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, answerRequired);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                "answerRequired=" + answerRequired +
                "} " + super.toString();
    }
}
