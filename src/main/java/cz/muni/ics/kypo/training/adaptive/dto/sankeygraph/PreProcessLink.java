package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.Objects;

public class PreProcessLink {

    private Long sourceTaskId;
    private Long targetTaskId;
    private Long value;

    public PreProcessLink(Long sourceTaskId, Long targetTaskId, Long value) {
        this.sourceTaskId = sourceTaskId;
        this.targetTaskId = targetTaskId;
        this.value = value;
    }

    public Long getSourceTaskId() {
        return sourceTaskId;
    }

    public void setSourceTaskId(Long sourceTaskId) {
        this.sourceTaskId = sourceTaskId;
    }

    public Long getTargetTaskId() {
        return targetTaskId;
    }

    public void setTargetTaskId(Long targetTaskId) {
        this.targetTaskId = targetTaskId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreProcessLink)) return false;
        PreProcessLink that = (PreProcessLink) o;
        return Objects.equals(getSourceTaskId(), that.getSourceTaskId()) &&
                Objects.equals(getTargetTaskId(), that.getTargetTaskId()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceTaskId(), getTargetTaskId(), getValue());
    }

    @Override
    public String toString() {
        return "PreProcessLink{" +
                "sourceTaskId=" + sourceTaskId +
                ", targetTaskId=" + targetTaskId +
                ", value=" + value +
                '}';
    }
}
