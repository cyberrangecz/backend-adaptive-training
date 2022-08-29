package cz.muni.ics.kypo.training.adaptive.domain.simulator.imports;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.AbstractPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Encapsulates information about training definition and its phase.
 */
@ApiModel(
        value = "ImportTrainingDefinition",
        description = "A basic information about training definition."
)
@Data
public class ImportTrainingDefinition {
    @ApiModelProperty(value = "ID of training definition", example = "1")
    private Long id;
    @ApiModelProperty(value = "A name of the training/game (e.g., Photo Hunter) .", example = "TrainingDefinition2")
    private String title;
    @ApiModelProperty(value = "Description of training definition that is visible to the participant.", example = "Unreleased training definition", position = 1)
    private String description;
    @ApiModelProperty(value = "Current state of training definition.", example = "UNRELEASED", position = 2)
    private TDState state;
    @ApiModelProperty(value = "Sign if stepper bar should be displayed.", example = "false", position = 3)
    private boolean showStepperBar;
    @ApiModelProperty(value = "Estimated time it takes to finish runs created from this definition.", example = "5", position = 4)
    private long estimatedDuration;
    @ApiModelProperty(value = "List of knowledge and skills necessary to complete the training.", example = "", position = 5)
    private String[] prerequisites;
    @ApiModelProperty(value = "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ", example = "", position = 6)
    private String[] outcomes;
    @ApiModelProperty(value = "Information about all phase in training definition.", position = 7)
    @Valid
    @NotEmpty(message = "{importTrainingDefinition.phases.NotEmpty.message}")
    private List<AbstractPhaseImport> phases = new ArrayList<>();
    @ApiModelProperty(value = "Sign if training definition can be archived or not.", example = "true", position = 7)
    private boolean canBeArchived;
    @ApiModelProperty(value = "Time of last edit done to definition.", example = "2017-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime lastEdited;
    @ApiModelProperty(value = "Name of the user who has done the last edit in definition.", example = "John Doe")
    private String lastEditedBy;
}
