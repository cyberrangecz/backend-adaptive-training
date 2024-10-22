package cz.cyberrange.platform.training.adaptive.api.dto.export.phases.training;

import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.AbstractPhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.technique.MitreTechniqueDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates information about training phase. Inherits from {@link AbstractPhaseExportDTO}
 */
@ApiModel(
        value = "TrainingPhaseExportDTO",
        description = "Exported training phase.",
        parent = AbstractPhaseExportDTO.class
)
public class TrainingPhaseExportDTO extends AbstractPhaseExportDTO {

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    private int estimatedDuration;
    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    private int allowedCommands;
    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10")
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true)
    private List<TaskExportDTO> tasks = new ArrayList<>();
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true)
    private List<DecisionMatrixRowExportDTO> decisionMatrix;
    @ApiModelProperty(value = "List of mitre techniques used in the training phase.")
    private Set<MitreTechniqueDTO> mitreTechniques;
    @ApiModelProperty(value = "Set of the expected commands to be executed during the training level.")
    private Set<String> expectedCommands;

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

    public Set<MitreTechniqueDTO> getMitreTechniques() {
        return mitreTechniques;
    }

    public void setMitreTechniques(Set<MitreTechniqueDTO> mitreTechniques) {
        this.mitreTechniques = mitreTechniques;
    }

    public Set<String> getExpectedCommands() {
        return expectedCommands;
    }

    public void setExpectedCommands(Set<String> expectedCommands) {
        this.expectedCommands = expectedCommands;
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
