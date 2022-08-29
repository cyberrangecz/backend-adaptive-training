package cz.muni.ics.kypo.training.adaptive.domain.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        value = "QuestionChoiceImport"
)
@Data
public class QuestionChoiceImport {
    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of question choice", required = true, example = "An answer")
    private String text;
    @ApiModelProperty(value = "It defines whether this answer is correct or not", required = true, example = "true")
    private Boolean correct;
    @ApiModelProperty(value = "Order of question choice", required = true, example = "0")
    private Integer order;
}
