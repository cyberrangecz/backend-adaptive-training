package cz.muni.ics.kypo.training.adaptive.dto;

/**
 * Encapsulates information used for auditing.
 */
public class AuditInfoDTO {

    private long userRefId;
    private long sandboxId;
    private long poolId;
    private long trainingRunId;
    private long trainingDefinitionId;
    private long trainingInstanceId;
    private long trainingTime;
    private long phase;

    /**
     * Instantiates a new Audit info dto.
     */
    public AuditInfoDTO() {
    }

    /**
     * Gets user ref id.
     *
     * @return the user ref id
     */
    public long getUserRefId() {
        return userRefId;
    }

    /**
     * Sets user ref id.
     *
     * @param userRefId the user ref id
     */
    public void setUserRefId(long userRefId) {
        this.userRefId = userRefId;
    }

    /**
     * Gets sandbox id.
     *
     * @return the sandbox id
     */
    public long getSandboxId() {
        return sandboxId;
    }

    /**
     * Sets sandbox id.
     *
     * @param sandboxId the sandbox id
     */
    public void setSandboxId(long sandboxId) {
        this.sandboxId = sandboxId;
    }

    /**
     * Gets pool id.
     *
     * @return the pool id
     */
    public long getPoolId() {
        return poolId;
    }

    /**
     * Sets pool id.
     *
     * @param poolId the pool id
     */
    public void setPoolId(long poolId) {
        this.poolId = poolId;
    }

    /**
     * Gets training run id.
     *
     * @return the training run id
     */
    public long getTrainingRunId() {
        return trainingRunId;
    }

    /**
     * Sets training run id.
     *
     * @param trainingRunId the training run id
     */
    public void setTrainingRunId(long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    /**
     * Gets training definition id.
     *
     * @return the training definition id
     */
    public long getTrainingDefinitionId() {
        return trainingDefinitionId;
    }

    /**
     * Sets training definition id.
     *
     * @param trainingDefinitionId the training definition id
     */
    public void setTrainingDefinitionId(long trainingDefinitionId) {
        this.trainingDefinitionId = trainingDefinitionId;
    }

    /**
     * Gets training instance id.
     *
     * @return the training instance id
     */
    public long getTrainingInstanceId() {
        return trainingInstanceId;
    }

    /**
     * Sets training instance id.
     *
     * @param trainingInstanceId the training instance id
     */
    public void setTrainingInstanceId(long trainingInstanceId) {
        this.trainingInstanceId = trainingInstanceId;
    }

    /**
     * Gets training time.
     *
     * @return the training time
     */
    public long getTrainingTime() {
        return trainingTime;
    }

    /**
     * Sets training time.
     *
     * @param trainingTime the training time
     */
    public void setTrainingTime(long trainingTime) {
        this.trainingTime = trainingTime;
    }

    /**
     * Gets phase.
     *
     * @return the phase
     */
    public long getPhase() {
        return phase;
    }

    /**
     * Sets phase.
     *
     * @param phase the phase
     */
    public void setPhase(long phase) {
        this.phase = phase;
    }

    @Override
    public String toString() {
        return "AuditInfoDTO{" +
                "userRefId=" + userRefId +
                ", sandboxId=" + sandboxId +
                ", poolId=" + poolId +
                ", trainingRunId=" + trainingRunId +
                ", trainingDefinitionId=" + trainingDefinitionId +
                ", trainingInstanceId=" + trainingInstanceId +
                ", trainingTime=" + trainingTime +
                ", phase=" + phase +
                '}';
    }
}
