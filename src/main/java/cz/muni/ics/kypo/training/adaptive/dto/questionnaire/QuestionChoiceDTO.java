package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class QuestionChoiceDTO {

    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    @NotEmpty(message = "Task title must not be blank")
    private String text;
    @ApiModelProperty(value = "It defines whether this answer is correct or not", required = true, example = "true")
    @NotNull(message = "It must be specified whether a question choice is correct")
    private Boolean correct;
    @ApiModelProperty(value = "Order of question choice", required = true, example = "0")
    @NotNull(message = "Question choice order must be specified")
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
        if (o == null || getClass() != o.getClass()) return false;
        QuestionChoiceDTO that = (QuestionChoiceDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(correct, that.correct) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, correct, order);
    }

    @Override
    public String toString() {
        return "QuestionChoiceDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correct=" + correct +
                ", order=" + order +
                '}';
    }
}
