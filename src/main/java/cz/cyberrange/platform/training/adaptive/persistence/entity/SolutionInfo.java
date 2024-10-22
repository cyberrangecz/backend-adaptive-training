package cz.cyberrange.platform.training.adaptive.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * Class represents information of hint associated with current level of training run
 */
@Embeddable
public class SolutionInfo {
    @Column(name = "training_phase_id", nullable = false)
    private long trainingPhaseId;
    @Column(name = "task_id", nullable = false)
    private long taskId;
    @Column(name = "solution_content", nullable = false)
    private String solutionContent;

    public SolutionInfo() {
    }

    public SolutionInfo(long trainingPhaseId, long taskId, String solutionContent) {
        this.trainingPhaseId = trainingPhaseId;
        this.taskId = taskId;
        this.solutionContent = solutionContent;
    }

    public long getTrainingPhaseId() {
        return trainingPhaseId;
    }

    public void setTrainingPhaseId(long trainingPhaseId) {
        this.trainingPhaseId = trainingPhaseId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolutionInfo that = (SolutionInfo) o;
        return trainingPhaseId == that.trainingPhaseId && taskId == that.taskId && Objects.equals(solutionContent, that.solutionContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingPhaseId, taskId, solutionContent);
    }

    @Override
    public String toString() {
        return "SolutionInfo{" +
                "trainingPhaseId=" + trainingPhaseId +
                ", taskId=" + taskId +
                ", solutionContent='" + solutionContent + '\'' +
                '}';
    }
}
