package cz.cyberrange.platform.training.adaptive.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Encapsulates information used for auditing.
 */
@ApiModel(
        value = "AuditInfoDTO",
        description = "Common information used for auditing."
)
public class AuditInfoDTO {

    @ApiModelProperty(value = "Main identifier of the user in user managements service.", example = "1")
    private long userRefId;
    @ApiModelProperty(value = "Main identifier of the sandbox.", example = "1")
    private String sandboxId;
    @ApiModelProperty(value = "Main identifier of the pool.", example = "1")
    private long poolId;
    @ApiModelProperty(value = "Main identifier of the training run.", example = "1")
    private long trainingRunId;
    @ApiModelProperty(value = "Main identifier of the training definition.", example = "1")
    private long trainingDefinitionId;
    @ApiModelProperty(value = "Main identifier of the training instance.", example = "1")
    private long trainingInstanceId;
    @ApiModelProperty(value = "Current duration of the training phase.", example = "1")
    private long trainingTime;
    @ApiModelProperty(value = "Main identifier of the phase.", example = "1")
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
    public String getSandboxId() {
        return sandboxId;
    }

    /**
     * Sets sandbox id.
     *
     * @param sandboxId the sandbox id
     */
    public void setSandboxId(String sandboxId) {
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
