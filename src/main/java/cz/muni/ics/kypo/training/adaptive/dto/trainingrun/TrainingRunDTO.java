package cz.muni.ics.kypo.training.adaptive.dto.trainingrun;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Encapsulates information about Training Run.
 */
@ApiModel(
        value = "TrainingRunDTO",
        description = "The act, or a recording, of performing actions during training from a perspective of one concrete participant."
)
public class TrainingRunDTO {

    @ApiModelProperty(value = "Main identifier of training run.", example = "1")
    private Long id;
    @ApiModelProperty(value = "Date when training run started.", example = "2016-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "Date when training run ends.", example = "2022-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime endTime;
    @ApiModelProperty(value = "Current state of training run.", example = "ALLOCATED")
    private TRState state;
    @ApiModelProperty(value = "Reference to the received sandbox.")
    private Long sandboxInstanceRefId;
    @ApiModelProperty(value = "Reference to participant of training run.")
    private UserRefDTO participantRef;

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
     * Gets start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets state.
     *
     * @return the {@link TRState}
     */
    public TRState getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the {@link TRState}
     */
    public void setState(TRState state) {
        this.state = state;
    }

    /**
     * Gets sandbox instance ref.
     *
     * @return the sandbox instance ref id
     */
    public Long getSandboxInstanceRefId() {
        return sandboxInstanceRefId;
    }

    /**
     * Sets sandbox instance ref.
     *
     * @param sandboxInstanceRefId the sandbox instance ref id
     */
    public void setSandboxInstanceRefId(Long sandboxInstanceRefId) {
        this.sandboxInstanceRefId = sandboxInstanceRefId;
    }

    /**
     * Gets participant ref.
     *
     * @return the {@link UserRefDTO}
     */
    public UserRefDTO getParticipantRef() {
        return participantRef;
    }

    /**
     * Sets participant ref.
     *
     * @param participantRef the {@link UserRefDTO}
     */
    public void setParticipantRef(UserRefDTO participantRef) {
        this.participantRef = participantRef;
    }

    @Override
    public String toString() {
        return "TrainingRunDTO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", state=" + state +
                ", sandboxInstanceRefId=" + sandboxInstanceRefId +
                ", participantRef=" + participantRef +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TrainingRunDTO)) return false;
        TrainingRunDTO that = (TrainingRunDTO) object;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getState(), that.getState()) &&
                Objects.equals(getParticipantRef(), that.getParticipantRef());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getState(), getParticipantRef());
    }
}
