package cz.muni.ics.kypo.training.adaptive.dto.training.preview;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class TrainingPhasePreviewDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Task associated with the training phase", required = true)
    private TaskPreviewDTO task;

    public TaskPreviewDTO getTask() {
        return task;
    }

    public void setTask(TaskPreviewDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrainingPhasePreviewDTO that = (TrainingPhasePreviewDTO) o;
        return Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), task);
    }

    @Override
    public String toString() {
        return "TrainingPhasePreviewDTO{" +
                "task=" + task +
                '}';
    }
}
