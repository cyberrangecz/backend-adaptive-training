package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;

import java.io.Serializable;

public abstract class AbstractPhaseDTO implements Serializable {

    private Long id;
    private String title;
    private Integer order;
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
    public String toString() {
        return "AbstractPhaseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", phaseType=" + phaseType +
                '}';
    }
}
