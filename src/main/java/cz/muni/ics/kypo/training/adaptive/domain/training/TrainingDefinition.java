package cz.muni.ics.kypo.training.adaptive.domain.training;

import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class represents Training definition.
 * Training instances can be created based on definitions.
 */
@Entity
@Table(name = "training_definition")
@NamedQueries({
        @NamedQuery(
                name = "TrainingDefinition.findAllForOrganizers",
                query = "SELECT DISTINCT td FROM TrainingDefinition td WHERE td.state = :state"
        ),
        @NamedQuery(
                name = "TrainingDefinition.findAllForDesigner",
                query = "SELECT DISTINCT td FROM TrainingDefinition td " +
                        "LEFT JOIN td.authors aut " +
                        "WHERE aut.userRefId = :userRefId  AND td.state = 'UNRELEASED'"
        )
})
public class TrainingDefinition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trainingDefinitionGenerator")
    @SequenceGenerator(name = "trainingDefinitionGenerator", sequenceName = "training_definition_seq")
    @Column(name = "training_definition_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "prerequisites")
    private String[] prerequisites;
    @Column(name = "outcomes")
    private String[] outcomes;
    @Column(name = "state", length = 128, nullable = false)
    @Enumerated(EnumType.STRING)
    private TDState state;
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "training_definition_user",
            joinColumns = @JoinColumn(name = "training_definition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> authors = new HashSet<>();
    @Column(name = "show_stepper_bar", nullable = false)
    private boolean showStepperBar;
    @Column(name = "estimated_duration", nullable = true)
    private long estimatedDuration;
    @Column(name = "last_edited", nullable = false)
    private LocalDateTime lastEdited;
    @Column(name = "last_edited_by", nullable = false)
    private String lastEditedBy;


    /**
     * Gets unique identification number of Training definition
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique identification number of Training definition
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets title of Training definition
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of Training definition
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets text description specifying info about Training definition
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets text description specifying info about Training definition
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets skill prerequisites that trainee should have to be able to complete training runs created
     * from this Training definition
     *
     * @return the string [ ]
     */
    public String[] getPrerequisites() {
        return prerequisites;
    }

    /**
     * Sets skill prerequisites that trainee should have to be able to complete training runs created
     * from this Training definition
     *
     * @param prerequisites the prerequisites
     */
    public void setPrerequisites(String[] prerequisites) {
        this.prerequisites = prerequisites;
    }

    /**
     * Gets knowledge that trainee can learn from training runs created from this Training definition
     *
     * @return the string [ ]
     */
    public String[] getOutcomes() {
        return outcomes;
    }

    /**
     * Sets knowledge that trainee can learn from training runs created from this Training definition
     *
     * @param outcomes the outcomes
     */
    public void setOutcomes(String[] outcomes) {
        this.outcomes = outcomes;
    }

    /**
     * Gets development state in which is Training definition
     * States are PRIVATED, RELEASED, ARCHIVED and UNRELEASED
     *
     * @return the state
     */
    public TDState getState() {
        return state;
    }

    /**
     * Sets development state in which is Training definition
     * States are PRIVATED, RELEASED, ARCHIVED and UNRELEASED
     *
     * @param state the state
     */
    public void setState(TDState state) {
        this.state = state;
    }

    /**
     * Gets set of users that can make changes to the Training definition
     *
     * @return the authors
     */
    public Set<User> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }

    /**
     * Sets set of users that can make changes to the Training definition
     *
     * @param authors the authors
     */
    public void setAuthors(Set<User> authors) {
        this.authors = authors;
    }

    /**
     * Adds user to the set of authors that can make changes to the Training definition
     *
     * @param authorRef the author ref
     */
    public void addAuthor(User authorRef) {
        this.authors.add(authorRef);
        authorRef.addTrainingDefinition(this);
    }

    /**
     * Remove authors with given ids from the set of authors.
     *
     * @param userRefIds ids of the authors to be removed.
     */
    public void removeAuthorsByUserRefIds(Set<Long> userRefIds) {
        this.authors.removeIf(userRef -> userRefIds.contains(userRef.getUserRefId()));
    }

    /**
     * Gets if stepper bar is shown while in run.
     *
     * @return true if bar is shown
     */
    public boolean isShowStepperBar() {
        return showStepperBar;
    }

    /**
     * Sets if stepper bar is shown while in run.
     *
     * @param showStepperBar true if bar is shown
     */
    public void setShowStepperBar(boolean showStepperBar) {
        this.showStepperBar = showStepperBar;
    }

    /**
     * Gets estimated duration in minutes that it should take to complete run based on given Training definition
     *
     * @return the estimated duration
     */
    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Sets estimated duration in minutes that it should take to complete run based on given Training definition
     *
     * @param estimatedDuration the estimated duration
     */
    public void setEstimatedDuration(long estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Gets time of last edit done to Training Definition
     *
     * @return the last edited
     */
    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    /**
     * Sets time of last edit done to Training Definition
     *
     * @param lastEdited the last edited
     */
    public void setLastEdited(LocalDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    /**
     * Gets the name of the user who has done the last edit in Training Definition
     *
     * @return the name of the user
     */
    public String getLastEditedBy() {
        return lastEditedBy;
    }

    /**
     * Sets the name of the user who has done the last edit in Training Definition
     *
     * @param lastEditedBy the name of the user
     */
    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, outcomes, prerequisites, state, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TrainingDefinition))
            return false;
        TrainingDefinition other = (TrainingDefinition) obj;
        return Objects.equals(description, other.getDescription())
                && Arrays.equals(outcomes, other.getOutcomes())
                && Arrays.equals(prerequisites, other.getPrerequisites())
                && Objects.equals(state, other.getState())
                && Objects.equals(title, other.getTitle());
    }

    @Override
    public String toString() {
        return "TrainingDefinition{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", prerequisites=" + Arrays.toString(this.getPrerequisites()) +
                ", outcomes=" + Arrays.toString(this.getOutcomes()) +
                ", state=" + this.getState() +
                ", showStepperBar=" + this.isShowStepperBar() +
                ", estimatedDuration=" + this.getEstimatedDuration() +
                ", lastEdited=" + this.getLastEdited() +
                '}';
    }
}
