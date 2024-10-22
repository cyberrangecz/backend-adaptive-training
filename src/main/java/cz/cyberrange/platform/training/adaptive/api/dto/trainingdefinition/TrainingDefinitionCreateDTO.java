package cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition;

import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * Encapsulates information about Training definition, intended for creation of new definition.
 */
@ApiModel(
        value = "TrainingDefinitionCreateDTO",
        description = "Training definition to create."
)
public class TrainingDefinitionCreateDTO {

    @ApiModelProperty(value = "A name of the training/game (e.g., Photo Hunter) .", required = true, example = "Photo Hunter")
    @NotEmpty(message = "{trainingDefinition.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Description of training definition that is visible to the participant.", example = "Description of Photo Hunter", position = 1)
    private String description;
    @ApiModelProperty(value = "List of knowledge and skills necessary to complete the training.", example = "[HTML, http protocol]")
    private String[] prerequisites;
    @ApiModelProperty(value = "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ", example = "[outcomes]", position = 1)
    private String[] outcomes;
    @ApiModelProperty(value = "Current state of training definition.", required = true, example = "UNRELEASED", position = 1)
    @NotNull(message = "{trainingDefinition.state.NotNull.message}")
    private TDState state;
    @ApiModelProperty(value = "Sign if default phases should be created.", example = "false")
    private boolean defaultContent;

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
     * Gets if he default phases are created along with the new training definition.
     *
     * @return true if default phases are created
     */
    public boolean isDefaultContent() {
        return defaultContent;
    }

    /**
     * Sets if the default phases are created along with the new training definition.
     *
     * @param defaultContent true if default phases should be created
     */
    public void setDefaultContent(boolean defaultContent) {
        this.defaultContent = defaultContent;
    }

    @Override
    public String toString() {
        return "TrainingDefinitionCreateDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prerequisites=" + Arrays.toString(prerequisites) +
                ", outcomes=" + Arrays.toString(outcomes) +
                ", state=" + state +
                ", defaultContent=" + defaultContent +
                '}';
    }
}
