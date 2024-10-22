package cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class LinkDTO {

    private Integer source;
    private Integer target;
    private Long value;
    @JsonIgnore
    private Long sourceTaskId;

    public LinkDTO() {
    }

    public LinkDTO(Integer source, Integer target, Long value, Long sourceTaskId) {
        this(source, target, value);
        this.sourceTaskId = sourceTaskId;
    }

    public LinkDTO(Integer source, Integer target, Long value) {
        this.source = source;
        this.target = target;
        this.value = value;
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getSourceTaskId() {
        return sourceTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkDTO)) return false;
        LinkDTO linkDTO = (LinkDTO) o;
        return Objects.equals(getSource(), linkDTO.getSource()) &&
                Objects.equals(getTarget(), linkDTO.getTarget()) &&
                Objects.equals(getValue(), linkDTO.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getTarget(), getValue());
    }

    @Override
    public String toString() {
        return "LinksDTO{" +
                "source=" + source +
                ", target=" + target +
                ", value=" + value +
                '}';
    }
}
