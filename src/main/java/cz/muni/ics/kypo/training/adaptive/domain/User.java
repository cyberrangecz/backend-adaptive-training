package cz.muni.ics.kypo.training.adaptive.domain;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing DB reference for user and training instances and definition they can access
 */
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_ref_id"}))
@NamedQueries({
        @NamedQuery(
                name = "User.findUsers",
                query = "SELECT u FROM User u WHERE u.userRefId IN :userRefId"
        ),
        @NamedQuery(
                name = "User.findUserByUserRefId",
                query = "SELECT u FROM User u WHERE u.userRefId = :userRefId"
        ),
        @NamedQuery(
                name = "User.findParticipantsRefByTrainingInstanceId",
                query = "SELECT pr.userRefId FROM TrainingRun tr " +
                        "INNER JOIN tr.participantRef pr " +
                        "INNER JOIN tr.trainingInstance ti " +
                        "WHERE ti.id = :trainingInstanceId"
        )
})
public class User implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userGenerator"
    )
    @SequenceGenerator(
            name = "userGenerator",
            sequenceName = "user_seq"
    )
    @Column(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private Long id;
    @Column(
            name = "user_ref_id",
            nullable = false
    )
    private Long userRefId;
    @ManyToMany(
            mappedBy = "organizers",
            fetch = FetchType.LAZY
    )
    private Set<TrainingInstance> trainingInstances = new HashSet<>();
    @ManyToMany(
            mappedBy = "authors",
            fetch = FetchType.LAZY
    )
    private Set<TrainingDefinition> trainingDefinitions = new HashSet<>();

    /**
     * Instantiates a new user reference
     */
    public User() {
    }

    /**
     * Instantiates a new user reference
     * @param userRefId id of the user stored in user management service
     */
    public User(Long userRefId) {
        this.userRefId = userRefId;
    }

    /**
     * Gets unique identification number of user reference
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique identification number of user reference
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets user ref id.
     *
     * @return the user ref id
     */
    public Long getUserRefId() {
        return userRefId;
    }

    /**
     * Sets user ref id.
     *
     * @param userRefId the user ref id
     */
    public void setUserRefId(Long userRefId) {
        this.userRefId = userRefId;
    }

    /**
     * Gets set of training instances user can access
     *
     * @return the training instances
     */
    public Set<TrainingInstance> getTrainingInstances() {
        return Collections.unmodifiableSet(trainingInstances);
    }

    /**
     * Sets set of training instances user can access
     *
     * @param trainingInstances the training instances
     */
    public void setTrainingInstances(Set<TrainingInstance> trainingInstances) {
        this.trainingInstances = trainingInstances;
    }

    /**
     * Gets set of training definitions user can access
     *
     * @return the training definitions
     */
    public Set<TrainingDefinition> getTrainingDefinitions() {
        return Collections.unmodifiableSet(trainingDefinitions);
    }

    /**
     * Sets set of training definitions user can access
     *
     * @param trainingDefinitions the training definitions
     */
    public void setTrainingDefinitions(Set<TrainingDefinition> trainingDefinitions) {
        this.trainingDefinitions = trainingDefinitions;
    }

    /**
     * Adds definition to the set of training definitions user can access
     *
     * @param trainingDefinition the training definition
     */
    public void addTrainingDefinition(TrainingDefinition trainingDefinition) {
        this.trainingDefinitions.add(trainingDefinition);
    }

    /**
     * Removes definition from the set of training definitions user can access
     *
     * @param trainingDefinition the training definition
     */
    public void removeTrainingDefinition(TrainingDefinition trainingDefinition) {
        this.trainingDefinitions.remove(trainingDefinition);
    }

    /**
     * Adds instance to the set of training instances user can access
     *
     * @param trainingInstance the training instance
     */
    public void addTrainingInstance(TrainingInstance trainingInstance) {
        this.trainingInstances.add(trainingInstance);
    }

    /**
     * Removes instance from the set of training instances user can access
     *
     * @param trainingInstance the training instance
     */
    public void removeTrainingInstance(TrainingInstance trainingInstance) {
        this.trainingInstances.remove(trainingInstance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserRefId(), user.getUserRefId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserRefId());
    }

    @Override
    public String toString() {
        return "UserRef{" +
                "id=" + id +
                ", userRefId=" + userRefId +
                '}';
    }
}
