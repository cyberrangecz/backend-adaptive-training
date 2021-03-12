package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.Objects;

public class NodeDTO {
    private Long taskId;
    private Integer taskOrder;
    private String taskTitle;
    private Long phaseId;
    private Integer phaseOrder;
    private String phaseTitle;

    public NodeDTO() {
    }

    public NodeDTO(Long taskId, Integer taskOrder, String taskTitle, Long phaseId, Integer phaseOrder, String phaseTitle) {
        this.taskId = taskId;
        this.taskOrder = taskOrder;
        this.taskTitle = taskTitle;
        this.phaseId = phaseId;
        this.phaseOrder = phaseOrder;
        this.phaseTitle = phaseTitle;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Integer getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(Integer taskOrder) {
        this.taskOrder = taskOrder;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Integer getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(Integer phaseOrder) {
        this.phaseOrder = phaseOrder;
    }

    public String getPhaseTitle() {
        return phaseTitle;
    }

    public void setPhaseTitle(String phaseTitle) {
        this.phaseTitle = phaseTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeDTO)) return false;
        NodeDTO nodeDTO = (NodeDTO) o;
        return Objects.equals(getTaskId(), nodeDTO.getTaskId()) &&
                Objects.equals(getTaskOrder(), nodeDTO.getTaskOrder()) &&
                Objects.equals(getPhaseId(), nodeDTO.getPhaseId()) &&
                Objects.equals(getPhaseOrder(), nodeDTO.getPhaseOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTaskId(), getPhaseId());
    }

    @Override
    public String toString() {
        return "NodeDTO{" +
                "taskId=" + taskId +
                ", taskOrder=" + taskOrder +
                ", taskTitle='" + taskTitle + '\'' +
                ", phaseId=" + phaseId +
                ", phaseOrder=" + phaseOrder +
                ", phaseTitle='" + phaseTitle + '\'' +
                '}';
    }
}
