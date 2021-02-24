package cz.muni.ics.kypo.training.adaptive.domain.phase;


import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;

import javax.persistence.*;
import java.io.Serializable;


// good source for entity inheritance: http://blog.marcinchwedczuk.pl/mapping-inheritance-in-hibernate

@Entity
@Table(name = "abstract_phase")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPhase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phaseGenerator")
    @SequenceGenerator(name = "phaseGenerator", sequenceName = "phase_seq")
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", trainingDefinitionId=" + trainingDefinition +
                '}';
    }
}
