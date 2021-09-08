package cz.muni.ics.kypo.training.adaptive.dto.training;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.AbstractQuestionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = "TrainingPhaseDTO"
)
public class TrainingPhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    private int estimatedDuration;
    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    private int allowedCommands;
    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10")
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true)
    private List<TaskDTO> tasks = new ArrayList<>();
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true)
    private List<DecisionMatrixRowDTO> decisionMatrix;
    @ApiModelProperty(value = "Questions related to the training phase.", required = true)
    private List<AbstractQuestionDTO> relatedQuestions;

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public int getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(int allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public int getAllowedWrongAnswers() {
        return allowedWrongAnswers;
    }

    public void setAllowedWrongAnswers(int allowedWrongAnswers) {
        this.allowedWrongAnswers = allowedWrongAnswers;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    public List<AbstractQuestionDTO> getRelatedQuestions() {
        return relatedQuestions;
    }

    public void setRelatedQuestions(List<AbstractQuestionDTO> relatedQuestions) {
        this.relatedQuestions = relatedQuestions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingPhaseDTO that = (TrainingPhaseDTO) o;
        return estimatedDuration == that.estimatedDuration &&
                allowedCommands == that.allowedCommands &&
                allowedWrongAnswers == that.allowedWrongAnswers &&
                Objects.equals(tasks, that.tasks) &&
                Objects.equals(decisionMatrix, that.decisionMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estimatedDuration, allowedCommands, allowedWrongAnswers, tasks, decisionMatrix);
    }

    @Override
    public String toString() {
        return "TrainingPhaseDTO{" +
                "estimatedDuration=" + estimatedDuration +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", tasks=" + tasks +
                ", decisionMatrix=" + decisionMatrix +
                ", relatedQuestions=" + relatedQuestions +
                "} " + super.toString();
    }
}
