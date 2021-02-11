package cz.muni.ics.kypo.training.adaptive.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


// good source for entity inheritance: http://blog.marcinchwedczuk.pl/mapping-inheritance-in-hibernate

@Entity
@Table(name = "abstract_phase")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractPhase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phaseGenerator")
    @SequenceGenerator(name = "phaseGenerator", sequenceName = "phase_seq")
    @Column(name = "id_phase", nullable = false, unique = true)
    private Long id;

    private String title;

    @Column(name = "order_in_training_definition", nullable = false)
    private Integer order;

    private Long trainingDefinitionId;

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

    public Long getTrainingDefinitionId() {
        return trainingDefinitionId;
    }

    public void setTrainingDefinitionId(Long trainingDefinition) {
        this.trainingDefinitionId = trainingDefinition;
    }

    @Override
    public String toString() {
        return "AbstractPhase{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", trainingDefinitionId=" + trainingDefinitionId +
                '}';
    }
}
