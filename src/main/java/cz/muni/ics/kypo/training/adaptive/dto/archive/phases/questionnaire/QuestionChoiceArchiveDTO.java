package cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(
        value = "QuestionChoiceArchiveDTO"
)
public class QuestionChoiceArchiveDTO {

    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    private String text;
    @ApiModelProperty(value = "It defines whether this answer is correct or not", required = true, example = "true")
    private Boolean correct;
    @ApiModelProperty(value = "Order of question choice", required = true, example = "0")
    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionChoiceArchiveDTO)) return false;
        QuestionChoiceArchiveDTO that = (QuestionChoiceArchiveDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getText(), that.getText()) &&
                Objects.equals(correct, that.correct) &&
                Objects.equals(getOrder(), that.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), correct, getOrder());
    }

    @Override
    public String toString() {
        return "QuestionChoiceArchiveDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correct=" + correct +
                ", order=" + order +
                '}';
    }
}
