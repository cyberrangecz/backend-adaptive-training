package cz.muni.ics.kypo.training.adaptive.dto.trainingrun;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.enums.Actions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Encapsulates information about already accessed training run.
 */
@ApiModel(
        value = "AccessedTrainingRunDTO",
        description = "Already accessed training run by some participant."
)
public class AccessedTrainingRunDTO {

    @ApiModelProperty(value = "Main identifier of training run.", example = "1")
    private Long id;
    @ApiModelProperty(value = "Short textual description of the training instance.", example = "Concluded Instance")
    private String title;
    @ApiModelProperty(value = "Start date of training instance for which the training run was created.", example = "2016-10-19T10:23:54")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime trainingInstanceStartDate;
    @ApiModelProperty(value = "End date of training instance for which the training run was created.", example = "2017-10-19T10:23:54")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime trainingInstanceEndDate;
    @ApiModelProperty(value = "Current phase order of training run.", example = "1")
    private int currentPhaseOrder;
    @ApiModelProperty(value = "The number of phase in the training instance.", example = "3")
    private int numberOfPhases;
    @ApiModelProperty(value = "Possible action which can be executed with training Run.", example = "RESULTS")
    private Actions possibleAction;
    @ApiModelProperty(value = "Id of associated training instance", example = "1")
    private Long instanceId;

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
     * Gets start date of training instance.
     *
     * @return the training instance start date
     */
    public LocalDateTime getTrainingInstanceStartDate() {
        return trainingInstanceStartDate;
    }

    /**
     * Sets start date of training instance.
     *
     * @param trainingInstanceStartDate the training instance start date
     */
    public void setTrainingInstanceStartDate(LocalDateTime trainingInstanceStartDate) {
        this.trainingInstanceStartDate = trainingInstanceStartDate;
    }

    /**
     * Gets end date of training instance.
     *
     * @return the training instance end date
     */
    public LocalDateTime getTrainingInstanceEndDate() {
        return trainingInstanceEndDate;
    }

    /**
     * Sets end date of training instance.
     *
     * @param trainingInstanceEndDate the training instance end date
     */
    public void setTrainingInstanceEndDate(LocalDateTime trainingInstanceEndDate) {
        this.trainingInstanceEndDate = trainingInstanceEndDate;
    }

    /**
     * Gets current phase order.
     *
     * @return the current phase order
     */
    public int getCurrentPhaseOrder() {
        return currentPhaseOrder;
    }

    /**
     * Sets current phase order.
     *
     * @param currentPhaseOrder the current phase order
     */
    public void setCurrentPhaseOrder(int currentPhaseOrder) {
        this.currentPhaseOrder = currentPhaseOrder;
    }

    /**
     * Gets number of phase.
     *
     * @return the number of phase
     */
    public int getNumberOfPhases() {
        return numberOfPhases;
    }

    /**
     * Sets number of phase.
     *
     * @param numberOfPhases the number of phase
     */
    public void setNumberOfPhases(int numberOfPhases) {
        this.numberOfPhases = numberOfPhases;
    }

    /**
     * Gets possible action.
     *
     * @return the possible {@link Actions}
     */
    public Actions getPossibleAction() {
        return possibleAction;
    }

    /**
     * Sets possible action.
     *
     * @param possibleAction the possible {@link Actions}
     */
    public void setPossibleAction(Actions possibleAction) {
        this.possibleAction = possibleAction;
    }

    /**
     * Gets instance id.
     *
     * @return the instance id
     */
    public Long getInstanceId() {
        return instanceId;
    }

    /**
     * Sets instance id.
     *
     * @param instanceId the instance id
     */
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }



    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccessedTrainingRunDTO)) return false;
        AccessedTrainingRunDTO that = (AccessedTrainingRunDTO) object;
        return Objects.equals(getCurrentPhaseOrder(), that.getCurrentPhaseOrder()) &&
                Objects.equals(getNumberOfPhases(), that.getNumberOfPhases()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getInstanceId(), that.getInstanceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getCurrentPhaseOrder(), getNumberOfPhases(), getInstanceId());
    }

    @Override
    public String toString() {
        return "AccessedTrainingRunDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", trainingInstanceStartDate=" + trainingInstanceStartDate +
                ", trainingInstanceEndDate=" + trainingInstanceEndDate +
                ", currentPhaseOrder=" + currentPhaseOrder +
                ", numberOfPhases=" + numberOfPhases +
                ", possibleAction=" + possibleAction +
                ", instanceId=" + instanceId +
                '}';
    }
}
