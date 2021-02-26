package cz.muni.ics.kypo.training.adaptive.dto.questionnaire.view;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class QuestionChoiceViewDTO {

    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    @NotEmpty(message = "Task title must not be blank")
    private String text;
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
        QuestionChoiceViewDTO that = (QuestionChoiceViewDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, order);
    }

    @Override
    public String toString() {
        return "QuestionChoiceDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", order=" + order +
                '}';
    }
}
