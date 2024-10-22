package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(
        value = "AbstractPhaseImport",
        subTypes = {TrainingPhaseImport.class, AccessPhaseImport.class, InfoPhaseImport.class, QuestionnairePhaseImport.class},
        description = "Abstract superclass for classes TrainingPhaseDTO, AccessPhaseDTO, InfoPhaseDTO and QuestionnairePhaseDTO"
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "phase_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingPhaseImport.class, name = "TRAINING"),
        @JsonSubTypes.Type(value = QuestionnairePhaseImport.class, name = "QUESTIONNAIRE"),
        @JsonSubTypes.Type(value = InfoPhaseImport.class, name = "INFO"),
        @JsonSubTypes.Type(value = AccessPhaseImport.class, name = "ACCESS")})
@Data
public class AbstractPhaseImport {
    @ApiModelProperty(value = "ID of phase", example = "1")
    @NotNull(message = "{abstractPhaseImport.id.NotNull.message}")
    private Long id;
    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training phase description")
    protected String title;
    @ApiModelProperty(value = "Type of the phase.", allowableValues = "QUESTIONNAIRE,INFO,TRAINING", example = "TRAINING")
    protected PhaseType phaseType;
    @ApiModelProperty(value = "Order of phase, starts with 0", example = "2")
    @NotNull(message = "{abstractPhaseImport.order.NotNull.message}")
    protected Integer order;
}
