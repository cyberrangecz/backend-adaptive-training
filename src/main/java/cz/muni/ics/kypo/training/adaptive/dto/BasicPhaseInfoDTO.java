package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Encapsulates basic information about phase.
 */
@ApiModel(
        value = "BasicPhaseInfoDTO",
        description = "Basic information about the phase and its type."
)
public class BasicPhaseInfoDTO {

    @ApiModelProperty(value = "Main identifier of phase.", example = "1")
    private Long id;
    @ApiModelProperty(value = "Short textual description of the phase.", example = "Training phase")
    private String title;
    @ApiModelProperty(value = "Order of phase among phase in training definition.", example = "1")
    private int order;
    @ApiModelProperty(value = "Type of the phase.", example = "TRAINING")
    private PhaseType phaseType;

    /**
     * Instantiates a new Basic phase info dto.
     */
    public BasicPhaseInfoDTO() {
    }

    /**
     * Instantiates a new Basic phase info dto.
     *
     * @param id        the id
     * @param title     the title
     * @param phaseType the phase type
     * @param order     the order
     */
    public BasicPhaseInfoDTO(Long id, String title, PhaseType phaseType, int order) {
        this.id = id;
        this.title = title;
        this.phaseType = phaseType;
        this.order = order;
    }

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
     * Gets order number of phase that is compared with order numbers of other phase associated with same definition.
     * First phase from definition has order of 0
     *
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets order number of phase that is compared with order numbers of other phase associated with same definition.
     * First phase from definition has order of 0
     *
     * @param order the order
     */
    public void setOrder(int order) {
        this.order = order;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPhaseInfoDTO that = (BasicPhaseInfoDTO) o;
        return order == that.order &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                phaseType == that.phaseType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, order, phaseType);
    }

    @Override
    public String toString() {
        return "BasicPhaseInfoDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", phaseType=" + phaseType +
                '}';
    }
}
