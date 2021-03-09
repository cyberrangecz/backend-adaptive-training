package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.Objects;

public class LinkDTO {

    private Integer source;
    private Integer target;
    private Long value;

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
