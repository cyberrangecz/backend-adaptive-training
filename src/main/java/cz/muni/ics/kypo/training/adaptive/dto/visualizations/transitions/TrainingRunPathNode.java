package cz.muni.ics.kypo.training.adaptive.dto.visualizations.transitions;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Encapsulates information about one node in the transition graph.
 */
@ApiModel(
        value = "TrainingRunPathNode",
        description = "Represents visited phase and task if it is training phase."
)
public class TrainingRunPathNode {
    @ApiModelProperty(value = "Main identifier of phase.", example = "4")
    private Long phaseId;
    @ApiModelProperty(value = "Order of the phase in a training definition.", example = "4")
    private Integer phaseOrder;
    @ApiModelProperty(value = "Main identifier of task.", example = "8")
    private Long taskId;
    @ApiModelProperty(value = "Order of the task in a training phase.", example = "0")
    private Integer taskOrder;

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Integer getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(Integer phaseOrder) {
        this.phaseOrder = phaseOrder;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(Integer taskOrder) {
        this.taskOrder = taskOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingRunPathNode that = (TrainingRunPathNode) o;
        return getPhaseId().equals(that.getPhaseId()) && getPhaseOrder().equals(that.getPhaseOrder()) && Objects.equals(getTaskId(), that.getTaskId()) && Objects.equals(getTaskOrder(), that.getTaskOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhaseId(), getPhaseOrder(), getTaskId(), getTaskOrder());
    }

    @Override
    public String toString() {
        return "TrainingRunPathNode{" +
                "phaseId=" + phaseId +
                ", phaseOrder=" + phaseOrder +
                ", taskId=" + taskId +
                ", taskOrder=" + taskOrder +
                '}';
    }
}
