package cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey;

import java.util.Objects;

public class PreProcessLink {
    private Integer source;
    private Integer target;
    private Long sourceTaskId;
    private Long targetTaskId;
    private Integer sourcePhaseOrder;
    private Integer targetPhaseOrder;
    private Long value;

    public PreProcessLink(Long sourceTaskId,
                          Long targetTaskId,
                          Long value,
                          Integer sourcePhaseOrder,
                          Integer targetPhaseOrder) {
        this.sourceTaskId = sourceTaskId;
        this.targetTaskId = targetTaskId;
        this.value = value;
        this.sourcePhaseOrder = sourcePhaseOrder;
        this.targetPhaseOrder = targetPhaseOrder;
    }

    public PreProcessLink(Integer source,
                          Integer target,
                          Long sourceTaskId,
                          Long targetTaskId,
                          Integer sourcePhaseOrder,
                          Integer targetPhaseOrder,
                          Long value) {
        this(sourceTaskId, targetTaskId, value, sourcePhaseOrder, targetPhaseOrder);
        this.source = source;
        this.target = target;
    }

    public PreProcessLink(Integer source,
                          Integer target,
                          Long targetTaskId,
                          Integer targetPhaseOrder,
                          Long value) {
        this(source, target, null, targetTaskId,  null, targetPhaseOrder, value);
    }

    public PreProcessLink(Integer source,
                          Long sourceTaskId,
                          Integer sourcePhaseOrder,
                          Integer target,
                          Long value) {
        this(source, target, sourceTaskId, null,  sourcePhaseOrder, null, value);
    }

    public PreProcessLink(Integer source,
                          Integer target,
                          Long value) {
        this(source, target, null, null,  null, null, value);
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
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

    public Integer getSourcePhaseOrder() {
        return sourcePhaseOrder;
    }

    public void setSourcePhaseOrder(Integer sourcePhaseOrder) {
        this.sourcePhaseOrder = sourcePhaseOrder;
    }

    public Integer getTargetPhaseOrder() {
        return targetPhaseOrder;
    }

    public void setTargetPhaseOrder(Integer targetPhaseOrder) {
        this.targetPhaseOrder = targetPhaseOrder;
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
                "source=" + source +
                ", target=" + target +
                ", sourceTaskId=" + sourceTaskId +
                ", targetTaskId=" + targetTaskId +
                ", sourcePhaseOrder=" + sourcePhaseOrder +
                ", targetPhaseOrder=" + targetPhaseOrder +
                ", value=" + value +
                '}';
    }
}