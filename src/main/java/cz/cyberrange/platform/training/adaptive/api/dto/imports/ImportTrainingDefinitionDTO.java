package cz.cyberrange.platform.training.adaptive.api.dto.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.AbstractPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulates information about training definition and its phase.
 */
@ApiModel(
        value = "ImportTrainingDefinitionDTO",
        description = "A basic information about hint."
)
@JsonIgnoreProperties({"id","show_stepper_bar"}) // show_stepper_bar is allowed for backwards compatibility with old training definitions
public class ImportTrainingDefinitionDTO {

    @ApiModelProperty(value = "A name of the training/game (e.g., Photo Hunter) .", example = "TrainingDefinition2")
    @NotEmpty(message = "{trainingDefinition.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Description of training definition that is visible to the participant.", example = "Unreleased training definition", position = 1)
    private String description;
    @ApiModelProperty(value = "Current state of training definition.", example = "UNRELEASED", position = 2)
    @NotNull(message = "{trainingDefinition.state.NotNull.message}")
    private TDState state;
    @ApiModelProperty(value = "Estimated time it takes to finish runs created from this definition.", example = "5", position = 4)
    private Integer estimatedDuration;
    @ApiModelProperty(value = "List of knowledge and skills necessary to complete the training.", example = "", position = 5)
    private String[] prerequisites;
    @ApiModelProperty(value = "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ", example = "", position = 6)
    private String[] outcomes;
    @Valid
    @ApiModelProperty(value = "Information about all phase in training definition.", position = 7)
    private List<AbstractPhaseImportDTO> phases = new ArrayList<>();

    /**
     * Instantiates a new Import training definition dto.
     */
    public ImportTrainingDefinitionDTO() {
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
     * @return the list of {@link AbstractPhaseImportDTO}
     */
    public List<AbstractPhaseImportDTO> getPhases() {
        return phases;
    }

    /**
     * Sets phase.
     *
     * @param phases the list of {@link AbstractPhaseImportDTO}
     */
    public void setPhases(List<AbstractPhaseImportDTO> phases) {
        this.phases = new ArrayList<>(phases);
    }


    /**
     * Gets estimated duration.
     *
     * @return the estimated duration
     */
    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Sets estimated duration.
     *
     * @param estimatedDuration the estimated duration
     */
    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }


    @Override
    public String toString() {
        return "ImportTrainingDefinitionDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", prerequisites=" + Arrays.toString(prerequisites) +
                ", outcomes=" + Arrays.toString(outcomes) +
                ", state=" + state +
                ", estimatedDuration=" + estimatedDuration +
                '}';
    }
}
