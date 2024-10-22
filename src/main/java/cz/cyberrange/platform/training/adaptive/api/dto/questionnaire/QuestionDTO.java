package cz.cyberrange.platform.training.adaptive.api.dto.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(
        value = "QuestionDTO"
)
public class QuestionDTO extends AbstractQuestionDTO {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                "} " + super.toString();
    }
}
