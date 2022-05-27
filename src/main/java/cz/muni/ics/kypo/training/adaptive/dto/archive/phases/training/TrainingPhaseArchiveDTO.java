package cz.muni.ics.kypo.training.adaptive.dto.archive.phases.training;

import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.technique.MitreTechniqueDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates information about training phase. Inherits from {@link AbstractPhaseArchiveDTO}
 * Used for archiving.
 */
@ApiModel(
        value = "TrainingPhaseArchiveDTO",
        description = "Archived training phase.",
        parent = AbstractPhaseArchiveDTO.class
)
public class TrainingPhaseArchiveDTO extends AbstractPhaseArchiveDTO {

    @ApiModelProperty(value = "Estimated time (minutes) taken by the player to solve the training phase", example = "20")
    private int estimatedDuration;
    @ApiModelProperty(value = "Maximal number of allowed commands provided by played", required = true, example = "10")
    private int allowedCommands;
    @ApiModelProperty(value = "Maximal number of allowed wrong answers provided by played", required = true, example = "10")
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true)
    private List<TaskDTO> tasks = new ArrayList<>();
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true)
    private List<DecisionMatrixRowArchiveDTO> decisionMatrix;
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

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<DecisionMatrixRowArchiveDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowArchiveDTO> decisionMatrix) {
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
    public String toString() {
        return "TrainingPhaseArchiveDTO{" +
                "estimatedDuration=" + estimatedDuration +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongAnswers=" + allowedWrongAnswers +
                ", tasks=" + tasks +
                ", decisionMatrix=" + decisionMatrix +
                '}';
    }
}
