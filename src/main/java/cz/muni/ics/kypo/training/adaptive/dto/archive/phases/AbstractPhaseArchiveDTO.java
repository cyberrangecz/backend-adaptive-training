package cz.muni.ics.kypo.training.adaptive.dto.archive.phases;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.access.AccessPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.info.InfoPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionnairePhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.training.TrainingPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Encapsulates information about abstract phase.
 * Used for archiving.
 * Extended by {@link QuestionnairePhaseArchiveDTO}, {@link TrainingPhaseArchiveDTO}, {@link AccessPhaseArchiveDTO} and {@link InfoPhaseArchiveDTO}.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingPhaseArchiveDTO.class, name = "TrainingPhaseArchiveDTO"),
        @JsonSubTypes.Type(value = AccessPhaseArchiveDTO.class, name = "AccessPhaseArchiveDTO"),
        @JsonSubTypes.Type(value = QuestionnairePhaseArchiveDTO.class, name = "QuestionnairePhaseArchiveDTO"),
        @JsonSubTypes.Type(value = InfoPhaseArchiveDTO.class, name = "InfoPhaseArchiveDTO")})
@ApiModel(
        value = "AbstractPhaseArchiveDTO",
        subTypes = {TrainingPhaseArchiveDTO.class, AccessPhaseArchiveDTO.class, InfoPhaseArchiveDTO.class, QuestionnairePhaseArchiveDTO.class},
        description = "Superclass for classes TrainingPhaseArchiveDTO, AccessPhaseArchiveDTO, InfoPhaseArchiveDTO and QuestionnairePhaseArchiveDTO"
)
public abstract class AbstractPhaseArchiveDTO {

    @ApiModelProperty(value = "Main identifier of phase.", example = "1")
    protected Long id;
    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training phase")
    protected String title;
    @ApiModelProperty(value = "Type of the phase.", example = "TRAINING")
    protected PhaseType phaseType;
    @ApiModelProperty(value = "Order of phase, starts with 0", example = "2")
    protected Integer order;

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
     * Gets phase type.
     *
     * @return the phase type
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    /**
     * Sets phase type.
     *
     * @param phaseType the phase type
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
    public String toString() {
        return "AbstractPhaseArchiveDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }
}
