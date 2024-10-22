package cz.cyberrange.platform.training.adaptive.api.dto.imports.phases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.access.AccessPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.info.InfoPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.questionnaire.QuestionnairePhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training.TaskImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training.TrainingPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Encapsulates information about abstract phase.
 * Extended by {@link QuestionnairePhaseImportDTO}, {@link TaskImportDTO} and {@link InfoPhaseImportDTO}
 */
@ApiModel(
        value = "AbstractPhaseImportDTO",
        subTypes = {TrainingPhaseImportDTO.class, InfoPhaseImportDTO.class, QuestionnairePhaseImportDTO.class},
        description = "Superclass for classes TrainingPhaseImportDTO, QuestionnairePhaseImportDTO and InfoPhaseImportDTO"
)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "phase_type", visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingPhaseImportDTO.class, name = "TRAINING"),
        @JsonSubTypes.Type(value = QuestionnairePhaseImportDTO.class, name = "QUESTIONNAIRE"),
        @JsonSubTypes.Type(value = InfoPhaseImportDTO.class, name = "INFO"),
        @JsonSubTypes.Type(value = AccessPhaseImportDTO.class, name = "ACCESS")})
@JsonIgnoreProperties({"id"})
public abstract class AbstractPhaseImportDTO {

    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training phase description")
    @NotEmpty(message = "{phase.title.NotEmpty.message}")
    protected String title;
    @ApiModelProperty(value = "Type of the phase.", example = "TRAINING", position = 1)
    @NotNull(message = "{phase.phaseType.NotNull.message}")
    protected PhaseType phaseType;
    @ApiModelProperty(value = "Order of phase, starts with 0", example = "2", position = 2)
    @NotNull(message = "{phase.order.NotNull.message}")
    @Min(value = 0, message = "{phase.order.Min.message}")
    protected Integer order;

    /**
     * Instantiates a new Abstract phase import dto.
     */
    public AbstractPhaseImportDTO() {
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
     * Gets phase type.
     *
     * @return the {@link PhaseType}
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    /**
     * Sets phase type.
     *
     * @param phaseType the {@link PhaseType}
     */
    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    /**
     * Gets order number of phase that is compared with order numbers of other phase associated with same definition.
     * First phase from definition has order of 0
     *
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Sets order number of phase that is compared with order numbers of other phase associated with same definition.
     * First phase from definition has order of 0
     *
     * @param order the order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "AbstractPhaseImportDTO{" +
                "title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPhaseImportDTO)) return false;
        AbstractPhaseImportDTO that = (AbstractPhaseImportDTO) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                getPhaseType() == that.getPhaseType() &&
                Objects.equals(getOrder(), that.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getPhaseType(), getOrder());
    }
}
