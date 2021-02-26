package cz.muni.ics.kypo.training.adaptive.dto.export.phases;

import cz.muni.ics.kypo.training.adaptive.dto.export.phases.info.InfoPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire.QuestionnairePhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.training.TrainingPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Encapsulates information about abstract phase.
 * Extended by {@link QuestionnairePhaseExportDTO}, {@link TrainingPhaseExportDTO} and {@link InfoPhaseExportDTO}
 */
@ApiModel(
        value = "AbstractPhaseExportDTO",
        subTypes = {TrainingPhaseExportDTO.class, InfoPhaseExportDTO.class, QuestionnairePhaseExportDTO.class},
        description = "Superclass for classes TrainingPhaseExportDTO, InfoPhaseExportDTO and QuestionnairePhaseExportDTO"
)
public abstract class AbstractPhaseExportDTO {

    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training Phase")
    protected String title;
    @ApiModelProperty(value = "Type of the phase.", example = "TRAINING")
    protected PhaseType phaseType;
    @ApiModelProperty(value = "Order of phase, starts with 0", example = "2")
    protected int order;

    /**
     * Instantiates a new Abstract phase export dto.
     */
    public AbstractPhaseExportDTO() {
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
     * Gets order.
     *
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets order.
     *
     * @param order the order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPhaseExportDTO)) return false;
        AbstractPhaseExportDTO that = (AbstractPhaseExportDTO) o;
        return getOrder() == that.getOrder() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                getPhaseType() == that.getPhaseType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getPhaseType(), getOrder());
    }

    @Override
    public String toString() {
        return "AbstractPhaseExportDTO{" +
                "title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }
}
