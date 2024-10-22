package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(
        value = "TaskImport",
        description = "Imported task of the training phase."
)
@Data
public class TaskImport {
    @ApiModelProperty(value = "Question choice ID. Leave blank if new choice is added", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training phase title")
    protected String title;
    @ApiModelProperty(value = "Order of phase, starts with 0", example = "2", position = 1)
    @NotNull(message = "{taskImport.order.NotNull.message}")
    protected Integer order;
    protected Integer globalOrder;
    @ApiModelProperty(value = "The information and experiences that are directed towards a participant.", example = "Play me", position = 2)
    private String content;
    @ApiModelProperty(value = "Keyword found in training, used for access next phase.", example = "secretFlag", position = 3)
    @NotEmpty(message = "{taskImport.answer.NotNull.message}")
    private String answer;
    @ApiModelProperty(value = "Instruction how to get answer in training.", example = "This is how you do it", position = 4)
    private String solution;
    @ApiModelProperty(value = "How many times player can submit incorrect answer before displaying solution.", example = "5", position = 5)
    @NotNull(message = "{taskImport.incorrectAnswerLimit.NotNull.message}")
    private int incorrectAnswerLimit;
    @ApiModelProperty(value = "Sign if sandbox should be modified if the task is picked.", example = "true", position = 6)
    private boolean modifySandbox;
    @ApiModelProperty(value = "Expected duration of the sandbox change when the task is picked (in seconds).", example = "30", position = 7)
    private int sandboxChangeExpectedDuration;
}
