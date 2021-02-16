package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public abstract class AbstractPhaseDTO {

    @ApiModelProperty(value = "ID of task", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Short description of phase", required = true, example = "Training Phase 1")
    private String title;

    @ApiModelProperty(value = "Order of phase in a training definition", required = true, example = "1")
    private Integer order;

    @ApiModelProperty(value = "Type of phase", required = true, allowableValues = "QUESTIONNAIRE,INFO,TRAINING", example = "TRAINING")
    private PhaseType phaseType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPhaseDTO that = (AbstractPhaseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(order, that.order) &&
                phaseType == that.phaseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, order, phaseType);
    }

    @Override
    public String toString() {
        return "AbstractPhaseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", phaseType=" + phaseType +
                '}';
    }
}
