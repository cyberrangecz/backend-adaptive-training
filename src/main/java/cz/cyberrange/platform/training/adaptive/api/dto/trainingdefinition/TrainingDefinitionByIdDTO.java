package cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.cyberrange.platform.training.adaptive.utils.converter.LocalDateTimeUTCSerializer;
import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates information about Training Definition.
 */
@ApiModel(
        value = "TrainingDefinitionByIdDTO",
        description = "A definition of the training with phases."
)
public class TrainingDefinitionByIdDTO {

    @ApiModelProperty(value = "Main identifier of training definition.", example = "1")
    private Long id;
    @ApiModelProperty(value = "Description of training definition that is visible to the participant.", example = "Unreleased training definition")
    private String description;
    @ApiModelProperty(value = "A name of the training/game (e.g., Photo Hunter) .", example = "TrainingDefinition2")
    private String title;
    @ApiModelProperty(value = "List of knowledge and skills necessary to complete the training.", example = "")
    private String[] prerequisites;
    @ApiModelProperty(value = "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ", example = "")
    private String[] outcomes;
    @ApiModelProperty(value = "Current state of training definition.", example = "UNRELEASED")
    private TDState state;
    @ApiModelProperty(value = "Information about all phase in training definition.")
    private List<AbstractPhaseDTO> phases = new ArrayList<>();
    @ApiModelProperty(value = "Sign if training definition can be archived or not.", example = "true", position = 7)
    private boolean canBeArchived;
    @ApiModelProperty(value = "Estimated time it takes to finish runs created from this definition.", example = "5")
    private long estimatedDuration;
    @ApiModelProperty(value = "Time of last edit done to definition.", example = "2017-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime lastEdited;
    @ApiModelProperty(value = "Name of the user who has done the last edit in definition.", example = "John Doe")
    private String lastEditedBy;
    @ApiModelProperty(value = "Time of creation of definition.", example = "2017-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime createdAt;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get prerequisites.
     *
     * @return the prerequisites
     */
    public String[] getPrerequisites() {
        return prerequisites;
    }

    /**
     * Sets prerequisites.
     *
     * @param prerequisites the prerequisites
     */
    public void setPrerequisites(String[] prerequisites) {
        this.prerequisites = prerequisites;
    }

    /**
     * Get outcomes.
     *
     * @return the outcomes
     */
    public String[] getOutcomes() {
        return outcomes;
    }

    /**
     * Sets outcomes.
     *
     * @param outcomes the outcomes
     */
    public void setOutcomes(String[] outcomes) {
        this.outcomes = outcomes;
    }

    /**
     * Gets state.
     *
     * @return the {@link TDState}
     */
    public TDState getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the {@link TDState}
     */
    public void setState(TDState state) {
        this.state = state;
    }

    /**
     * Gets phase.
     *
     * @return the list of {@link AbstractPhaseDTO}
     */
    public List<AbstractPhaseDTO> getPhases() {
        return phases;
    }

    /**
     * Sets phase.
     *
     * @param phases the list of {@link AbstractPhaseDTO}
     */
    public void setPhases(List<AbstractPhaseDTO> phases) {
        this.phases = phases;
    }

    /**
     * Definition can be archived if no associated instances are active.
     *
     * @return true if definition can be archived
     */
    public boolean isCanBeArchived() {
        return canBeArchived;
    }

    /**
     * Definition can be archived if no associated instances are active.
     *
     * @param canBeArchived true if definition can be archived
     */
    public void setCanBeArchived(boolean canBeArchived) {
        this.canBeArchived = canBeArchived;
    }

    /**
     * Gets estimated duration.
     *
     * @return the estimated duration
     */
    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Sets estimated duration.
     *
     * @param estimatedDuration the estimated duration
     */
    public void setEstimatedDuration(long estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Gets time of last edit.
     *
     * @return the last edited
     */
    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    /**
     * Sets time of last edit.
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TrainingDefinitionByIdDTO)) return false;
        TrainingDefinitionByIdDTO that = (TrainingDefinitionByIdDTO) object;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getState(), that.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getState());
    }


    @Override
    public String toString() {
        return "TrainingDefinitionByIdDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prerequisites=" + Arrays.toString(prerequisites) +
                ", outcomes=" + Arrays.toString(outcomes) +
                ", state=" + state +
                ", phases=" + phases +
                ", canBeArchived=" + canBeArchived +
                ", estimatedDuration=" + estimatedDuration +
                ", lastEdited=" + lastEdited +
                ", createdAt=" + createdAt +
                '}';
    }
}
