package cz.muni.ics.kypo.training.adaptive.dto.export.phases.training;

import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates information about training phase. Inherits from {@link AbstractPhaseExportDTO}
 */
@ApiModel(value = "TrainingPhaseExportDTO", description = "Exported training phase.", parent = AbstractPhaseExportDTO.class)
public class TrainingPhaseExportDTO extends AbstractPhaseExportDTO {

    private int estimatedDuration;
    private int allowedCommands;
    private int allowedWrongAnswers;
    private List<TaskExportDTO> tasks = new ArrayList<>();
    private List<DecisionMatrixRowExportDTO> decisionMatrix;

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

    public List<TaskExportDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskExportDTO> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowExportDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowExportDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingPhaseExportDTO)) return false;
        if (!super.equals(o)) return false;
        TrainingPhaseExportDTO that = (TrainingPhaseExportDTO) o;
        return getEstimatedDuration() == that.getEstimatedDuration() &&
                getAllowedCommands() == that.getAllowedCommands() &&
                getAllowedWrongAnswers() == that.getAllowedWrongAnswers();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEstimatedDuration(), getAllowedCommands(), getAllowedWrongAnswers());
    }

    @Override
    public String toString() {
        return "TrainingPhaseExportDTO{" +
                "estimatedDuration=" + estimatedDuration +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }
}
