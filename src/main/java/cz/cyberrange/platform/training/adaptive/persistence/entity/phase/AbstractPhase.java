package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;


import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


// good source for entity inheritance: http://blog.marcinchwedczuk.pl/mapping-inheritance-in-hibernate

@Entity
@Table(name = "abstract_phase")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPhase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phaseGenerator")
    @SequenceGenerator(name = "phaseGenerator")
    @Column(name = "phase_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "order_in_training_definition", nullable = false)
    private Integer order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_definition_id")
    private TrainingDefinition trainingDefinition;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingDefinition getTrainingDefinition() {
        return trainingDefinition;
    }

    public void setTrainingDefinition(TrainingDefinition trainingDefinition) {
        this.trainingDefinition = trainingDefinition;
    }

    @Override
    public String toString() {
        return "AbstractPhase{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", order=" + this.getOrder() +
                '}';
    }
}
