package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing mitre technique used in Training Level.
 */
@Entity
@Table(name = "mitre_technique")
@NamedQueries({
        @NamedQuery(
                name = "MitreTechnique.findByTechniqueKey",
                query = "SELECT mt FROM MitreTechnique mt WHERE mt.techniqueKey = :techniqueKey"
        ),
})
public class MitreTechnique {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mitreTechniqueGenerator")
    @SequenceGenerator(name = "mitreTechniqueGenerator")
    @Column(name = "mitre_technique_id", nullable = false, unique = true)
    private Long id;
    @ManyToMany(mappedBy = "mitreTechniques")
    private Set<TrainingPhase> trainingPhases = new HashSet<>();
    @Column(name = "technique_key", nullable = false, unique = true)
    private String techniqueKey;

    /**
     * Instantiates a new Mitre Technique
     */
    public MitreTechnique() {
    }

    /**
     * Instantiates a new Mitre Technique
     *
     * @param id          unique identification number of mitre technique
     * @param techniqueKey string representing unique key of the technique
     */
    public MitreTechnique(Long id, String techniqueKey) {
        this.techniqueKey = techniqueKey;
        this.id = id;
    }

    /**
     * Gets unique identification number of Mitre technique
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets unique identification number of Mitre technique
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets string representing unique key of the technique
     *
     * @return the Mitre technique
     */
    public String getTechniqueKey() {
        return techniqueKey;
    }

    /**
     * Sets string representing unique key of the technique
     *
     * @param techniqueKey the Mitre technique
     */
    public void setTechniqueKey(String techniqueKey) {
        this.techniqueKey = techniqueKey;
    }

    /**
     * Gets set of training phases using the technique
     *
     * @return set of training phases
     */
    public Set<TrainingPhase> getTrainingPhases() {
        return trainingPhases;
    }

    /**
     * Sets set of training phases using the technique
     *
     * @param trainingPhases set of training phases
     */
    public void setTrainingPhases(Set<TrainingPhase> trainingPhases) {
        this.trainingPhases = trainingPhases;
    }


    public void addTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhases.add(trainingPhase);
    }

    public void removeTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhases.remove(trainingPhase);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MitreTechnique))
            return false;
        MitreTechnique accessToken = (MitreTechnique) o;
        return Objects.equals(this.techniqueKey, accessToken.getTechniqueKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(techniqueKey);
    }

    @Override
    public String toString() {
        return "MitreTechnique{" +
                "id=" + id +
                ", techniqueKey='" + techniqueKey + '\'' +
                '}';
    }
}
