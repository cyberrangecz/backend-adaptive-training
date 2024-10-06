package cz.muni.ics.kypo.training.adaptive.dto.trainingdefinition;

import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;


/**
 * Encapsulates information about Training Definition, intended for edit of the definition.
 */
@ApiModel(
        value = "TrainingDefinitionUpdateDTO",
        description = "Training definition to update."
)
public class TrainingDefinitionUpdateDTO {

    @ApiModelProperty(value = "Main identifier of training definition.", required = true, example = "2")
    @NotNull(message = "{trainingDefinition.id.NotNull.message}")
    private Long id;
    @ApiModelProperty(value = "A name of the training/game (e.g., Photo Hunter) .", required = true, example = "TrainingDefinition2", position = 1)
    @NotEmpty(message = "{trainingDefinition.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Description of training definition that is visible to the participant.", example = "Unreleased training definition", position = 3)
    private String description;
    @ApiModelProperty(value = "List of knowledge and skills necessary to complete the training.", example = "[phishing]", position = 4)
    private String[] prerequisites;
    @ApiModelProperty(value = "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ", example = "", position = 5)
    private String[] outcomes;
    @ApiModelProperty(value = "Current state of training definition.", required = true, example = "UNRELEASED", position = 6)
    @NotNull(message = "{trainingDefinition.state.NotNull.message}")
    private TDState state;

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
     * @return the prerequisites.
     */
    public String[] getPrerequisites() {
        return prerequisites;
    }

    /**
     * Sets prerequisites.
     *
     * @param prerequisites the prerequisites.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingDefinitionUpdateDTO)) return false;
        TrainingDefinitionUpdateDTO that = (TrainingDefinitionUpdateDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                getState() == that.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getState());
    }

    @Override
    public String toString() {
        return "TrainingDefinitionUpdateDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prerequisites=" + Arrays.toString(prerequisites) +
                ", outcomes=" + Arrays.toString(outcomes) +
                ", state=" + state +
                '}';
    }
}
