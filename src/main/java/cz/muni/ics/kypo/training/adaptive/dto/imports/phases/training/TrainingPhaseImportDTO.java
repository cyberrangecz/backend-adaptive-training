package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.training;

import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.AbstractPhaseImportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information about training phase. Inherits from {@link AbstractPhaseImportDTO}
 */
@ApiModel(
        value = "TrainingPhaseImportDTO",
        description = "Imported training phase.",
        parent = AbstractPhaseImportDTO.class
)
public class TrainingPhaseImportDTO extends AbstractPhaseImportDTO {

    @ApiModelProperty(value = "Estimated time it takes to finish the phase (in minutes).", example = "50")
    @Min(value = 0, message = "{trainingPhase.estimatedDuration.Size.message}")
    private int estimatedDuration;
    @ApiModelProperty(value = "Number of allowed commands that can be used to solve the task (used for data analysis).", example = "10", position = 1)
    @Min(value = 0, message = "{trainingPhase.allowedCommands.Min.message}")
    private int allowedCommands;
    @ApiModelProperty(value = "How many times player can submit incorrect answer before displaying solution.", example = "4", position = 2)
    @Min(value = 0, message = "{trainingPhase.allowedWrongAnswers.Min.message}")
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true, position = 3)
    @Valid
    private List<TaskImportDTO> tasks = new ArrayList<>();
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true, position = 4)
    @Valid
    @NotEmpty(message = "{trainingPhase.decisionMatrix.NotEmpty.message}")
    private List<DecisionMatrixRowImportDTO> decisionMatrix;

    /**
     * Gets estimated duration.
     *
     * @return the estimated duration
     */
    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Sets estimated duration.
     *
     * @param estimatedDuration the estimated duration
     */
    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Gets allowed commands.
     *
     * @return the allowed commands
     */
    public int getAllowedCommands() {
        return allowedCommands;
    }

    /**
     * Sets allowed commands.
     *
     * @param allowedCommands the allowed commands
     */
    public void setAllowedCommands(int allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    /**
     * Gets allowed wrong answers.
     *
     * @return the allowed wrong answers
     */
    public int getAllowedWrongAnswers() {
        return allowedWrongAnswers;
    }

    /**
     * Sets allowed wrong answers.
     *
     * @param allowedWrongAnswers the allowed wrong answers
     */
    public void setAllowedWrongAnswers(int allowedWrongAnswers) {
        this.allowedWrongAnswers = allowedWrongAnswers;
    }


    public List<TaskImportDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskImportDTO> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowImportDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowImportDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    @Override
    public String toString() {
        return "TrainingPhaseImportDTO{" +
                "estimatedDuration=" + estimatedDuration +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }
}
