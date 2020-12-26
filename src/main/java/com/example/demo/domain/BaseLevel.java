package com.example.demo.domain;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;


// good source for entity inheritance: http://blog.marcinchwedczuk.pl/mapping-inheritance-in-hibernate

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String estimatedDuration;
    private Long maxScore;
    private Integer orderInTrainingDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    private PhaseLevel phaseLevel;

    private Long trainingDefinitionId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Long getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Long maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getOrderInTrainingDefinition() {
        return orderInTrainingDefinition;
    }

    public void setOrderInTrainingDefinition(Integer orderInTrainingDefinition) {
        this.orderInTrainingDefinition = orderInTrainingDefinition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhaseLevel getPhaseLevel() {
        return phaseLevel;
    }

    public void setPhaseLevel(PhaseLevel phaseLevel) {
        this.phaseLevel = phaseLevel;
    }

    public Long getTrainingDefinitionId() {
        return trainingDefinitionId;
    }

    public void setTrainingDefinitionId(Long trainingDefinition) {
        this.trainingDefinitionId = trainingDefinition;
    }

    @Override
    public String toString() {
        return "BaseLevel{" + "title='" + title + '\'' + ", estimatedDuration='" + estimatedDuration + '\'' +
               ", maxScore=" + maxScore + '}';
    }
}
