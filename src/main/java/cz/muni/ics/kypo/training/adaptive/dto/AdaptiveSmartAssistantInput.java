package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.dto.training.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.DecisionMatrixRowForAssistantDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "AdaptiveSmartAssistantInput")
public class AdaptiveSmartAssistantInput {

    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    private Long trainingRunId;
    @ApiModelProperty(value = "The id of a phase X.", example = "5")
    private Long phaseX;
    @ApiModelProperty(value = "The number of tasks in a phase X.", example = "3")
    private Integer phaseXTasks;
    @ApiModelProperty(value = "The list of phaseIds (the given phase including the given phases).", example = "[1,2,3,4,5]")
    private List<Long> phaseIds = new ArrayList<>();
    @ApiModelProperty(value = "The decision matrix with weights to compute the students' performance.")
    private List<DecisionMatrixRowForAssistantDTO> decisionMatrix = new ArrayList<>();

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public Long getPhaseX() {
        return phaseX;
    }

    public void setPhaseX(Long phaseX) {
        this.phaseX = phaseX;
    }

    public Integer getPhaseXTasks() {
        return phaseXTasks;
    }

    public void setPhaseXTasks(Integer phaseXTasks) {
        this.phaseXTasks = phaseXTasks;
    }

    public List<Long> getPhaseIds() {
        return phaseIds;
    }

    public void setPhaseIds(List<Long> phaseIds) {
        this.phaseIds = phaseIds;
    }

    public List<DecisionMatrixRowForAssistantDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowForAssistantDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdaptiveSmartAssistantInput)) return false;
        AdaptiveSmartAssistantInput that = (AdaptiveSmartAssistantInput) o;
        return Objects.equals(trainingRunId, that.trainingRunId) &&
                Objects.equals(phaseX, that.phaseX) &&
                Objects.equals(phaseXTasks, that.phaseXTasks) &&
                Objects.equals(phaseIds, that.phaseIds) &&
                Objects.equals(decisionMatrix, that.decisionMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingRunId, phaseX, phaseXTasks, phaseIds, decisionMatrix);
    }

    @Override
    public String toString() {
        return "AdaptiveSmartAssistantInput{" +
                "trainingRunId=" + trainingRunId +
                ", phaseX=" + phaseX +
                ", phaseXTasks=" + phaseXTasks +
                ", phaseIds=" + phaseIds +
                ", decisionMatrix=" + decisionMatrix +
                '}';
    }
}
