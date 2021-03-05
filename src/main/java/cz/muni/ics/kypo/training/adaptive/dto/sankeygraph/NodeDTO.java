package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

public class NodeDTO {
    private Long taskId;
    private Long phaseId;

    public NodeDTO() {
    }

    public NodeDTO(Long taskId, Long phaseId) {
        this.taskId = taskId;
        this.phaseId = phaseId;
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

    @Override
    public String toString() {
        return "NodeDTO{" +
                "taskId=" + taskId +
                ", phaseId=" + phaseId +
                '}';
    }
}
