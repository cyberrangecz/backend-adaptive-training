package com.example.demo.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


// good source for entity inheritance: http://blog.marcinchwedczuk.pl/mapping-inheritance-in-hibernate

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractPhase {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String estimatedDuration;
    private Integer allowedCommands;
    private Integer allowedWrongFlags;

    @Column(name = "order_in_training_definition", nullable = false)
    private Integer order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private TrainingPhase trainingPhase;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public TrainingPhase getPhaseLevel() {
//        return trainingPhase;
//    }

//    public void setPhaseLevel(TrainingPhase trainingPhase) {
//        this.trainingPhase = trainingPhase;
//    }

    public Long getTrainingDefinitionId() {
        return trainingDefinitionId;
    }

    public void setTrainingDefinitionId(Long trainingDefinition) {
        this.trainingDefinitionId = trainingDefinition;
    }

    public Integer getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(Integer allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public Integer getAllowedWrongFlags() {
        return allowedWrongFlags;
    }

    public void setAllowedWrongFlags(Integer allowedWrongFlags) {
        this.allowedWrongFlags = allowedWrongFlags;
    }

    @Override
    public String toString() {
        return "AbstractPhase{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", estimatedDuration='" + estimatedDuration + '\'' +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongFlags=" + allowedWrongFlags +
                ", order=" + order +
                ", trainingDefinitionId=" + trainingDefinitionId +
                '}';
    }
}
