package cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel(
        value = "QuestionChoiceImportDTO"
)
@JsonIgnoreProperties({"id"})
public class QuestionChoiceImportDTO {

    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    @NotEmpty(message = "{questionChoice.text.NotEmpty.message}")
    private String text;
    @ApiModelProperty(value = "It defines whether this answer is correct or not", required = true, example = "true")
    @NotNull(message = "{questionChoice.correct.NotNull.message}")
    private Boolean correct;
    @ApiModelProperty(value = "Order of question choice", required = true, example = "0")
    @NotNull(message = "{questionChoice.order.NotNull.message}")
    @Min(value = 0, message = "{questionChoice.order.Min.message}")
    private Integer order;

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
        if (!(o instanceof QuestionChoiceImportDTO)) return false;
        QuestionChoiceImportDTO that = (QuestionChoiceImportDTO) o;
        return Objects.equals(getText(), that.getText()) &&
                Objects.equals(correct, that.correct) &&
                Objects.equals(getOrder(), that.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText(), correct, getOrder());
    }

    @Override
    public String toString() {
        return "QuestionChoiceImportDTO{" +
                "text='" + text + '\'' +
                ", correct=" + correct +
                ", order=" + order +
                '}';
    }
}
