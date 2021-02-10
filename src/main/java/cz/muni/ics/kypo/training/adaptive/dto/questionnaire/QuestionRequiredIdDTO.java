package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class QuestionRequiredIdDTO extends AbstractQuestionDTO {

    @ApiModelProperty(value = "Question ID. Leave blank if a new question is added", required = true, example = "1")
    @NotNull(message = "Question ID must be specified")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
