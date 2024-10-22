package cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.preview;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class QuestionChoicePreviewDTO {

    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    private String text;
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
        QuestionChoicePreviewDTO that = (QuestionChoicePreviewDTO) o;
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
        return "QuestionChoicePreviewDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", order=" + order +
                '}';
    }
}
