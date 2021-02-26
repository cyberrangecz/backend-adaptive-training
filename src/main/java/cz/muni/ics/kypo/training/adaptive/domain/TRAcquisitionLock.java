package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The entity which prevents multiple training runs to be created in parallel threads. Basically it determines active training runs.
 */
@Entity
@Table(name = "training_run_acquisition_lock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"participant_ref_id", "training_instance_id"}))
@NamedQueries({
        @NamedQuery(
                name = "TRAcquisitionLock.deleteByParticipantRefIdAndTrainingInstanceId",
                query = "DELETE FROM TRAcquisitionLock tral WHERE tral.participantRefId = :participantRefId AND tral.trainingInstanceId = :trainingInstanceId"
        )
})
public class TRAcquisitionLock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trAcquisitionLockGenerator")
    @SequenceGenerator(name = "trAcquisitionLockGenerator", sequenceName = "tr_acquisition_lock_seq")
    @Column(name = "training_run_acquisition_lock_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "participant_ref_id")
    private Long participantRefId;
    @Column(name = "training_instance_id")
    private Long trainingInstanceId;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    /**
     * Instantiates a new Tr acquisition lock.
     */
    public TRAcquisitionLock() {
    }

    /**
     * Instantiates a new Tr acquisition lock.
     *
     * @param participantRefId   the participant ref id
     * @param trainingInstanceId the training instance id
     * @param creationTime       the creation time
     */
    public TRAcquisitionLock(Long participantRefId, Long trainingInstanceId, LocalDateTime creationTime) {
        this.participantRefId = participantRefId;
        this.trainingInstanceId = trainingInstanceId;
        this.creationTime = creationTime;
    }

    /**
     * Gets unique identification number of tr acquisition lock
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique identification number of tr acquisition lock
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets participant ref id.
     *
     * @return the participant ref id
     */
    public Long getParticipantRefId() {
        return participantRefId;
    }

    /**
     * Sets participant ref id.
     *
     * @param participantRefId the participant ref id
     */
    public void setParticipantRefId(Long participantRefId) {
        this.participantRefId = participantRefId;
    }

    /**
     * Gets training instance id.
     *
     * @return the training instance id
     */
    public Long getTrainingInstanceId() {
        return trainingInstanceId;
    }

    /**
     * Sets training instance id.
     *
     * @param trainingInstanceId the training instance id
     */
    public void setTrainingInstanceId(Long trainingInstanceId) {
        this.trainingInstanceId = trainingInstanceId;
    }

    /**
     * Gets creation time.
     *
     * @return the creation time
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Sets creation time.
     *
     * @param creationTime the creation time
     */
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TRAcquisitionLock))
            return false;
        TRAcquisitionLock that = (TRAcquisitionLock) o;
        return Objects.equals(getParticipantRefId(), that.getParticipantRefId()) &&
                Objects.equals(getTrainingInstanceId(), that.getTrainingInstanceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParticipantRefId(), getTrainingInstanceId());
    }

    @Override
    public String toString() {
        return "TRAcquisitionLock{" +
                "id=" + this.getId() +
                ", participantRefId=" + this.getParticipantRefId() +
                ", trainingInstanceId=" + this.getTrainingInstanceId() +
                ", creationTime=" + this.getCreationTime() +
                '}';
    }
}
