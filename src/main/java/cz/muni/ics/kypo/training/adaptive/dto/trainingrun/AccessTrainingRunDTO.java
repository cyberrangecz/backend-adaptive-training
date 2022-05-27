package cz.muni.ics.kypo.training.adaptive.dto.trainingrun;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Encapsulates information about Training Run, intended as a response to run accessing.
 */
@ApiModel(
        value = "AccessTrainingRunDTO",
        description = "Just accessed training run."
)
public class AccessTrainingRunDTO {

    @ApiModelProperty(value = "Main identifier of training run.", example = "1")
    private Long trainingRunID;
    @ApiModelProperty(value = "Sign if stepper bar should be displayed.", example = "false")
    private boolean showStepperBar;
    @ApiModelProperty(value = "Main identifier of sandbox which is assigned to training run.", example = "2")
    private Long sandboxInstanceRefId;
    @ApiModelProperty(value = "Current phase in the training run.")
    private AbstractPhaseDTO currentPhase;
    @ApiModelProperty(value = "Information about all phase in training instance.")
    private List<BasicPhaseInfoDTO> infoAboutPhases;
    @ApiModelProperty(value = "Id of associated training instance", example = "1")
    private Long instanceId;
    @ApiModelProperty(value = "Date when training run started.", example = "2016-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "Content of the taken solution of the current training phase.", example = "solution of the task")
    private String takenSolution;
    @ApiModelProperty(value = "Indicates if local sandboxes are used for training runs.", example = "true")
    private boolean localEnvironment;
    @ApiModelProperty(value = "Main identifier of sandbox definition which is assigned to training instance of the training run.", example = "2")
    private Long sandboxDefinitionId;
    @ApiModelProperty(value = "Indicates if trainee can during training run move to the previous already solved phases.", example = "true")
    private boolean backwardMode;
    @ApiModelProperty(value = "Indicates if the current phase has been already corrected/answered.", example = "true")
    private boolean isPhaseAnswered;

    /**
     * Gets training run id.
     *
     * @return the training run id
     */
    public Long getTrainingRunID() {
        return trainingRunID;
    }

    /**
     * Sets training run id.
     *
     * @param trainingRunID the training run id
     */
    public void setTrainingRunID(Long trainingRunID) {
        this.trainingRunID = trainingRunID;
    }

    /**
     * Gets if stepper bar is shown while in run.
     *
     * @return true if bar is shown
     */
    public boolean isShowStepperBar() {
        return showStepperBar;
    }

    /**
     * Sets if stepper bar is shown while in run.
     *
     * @param showStepperBar true if bar is shown
     */
    public void setShowStepperBar(boolean showStepperBar) {
        this.showStepperBar = showStepperBar;
    }

    /**
     * Gets sandbox instance id.
     *
     * @return the sandbox instance id
     */
    public Long getSandboxInstanceRefId() {
        return sandboxInstanceRefId;
    }

    /**
     * Sets sandbox instance id.
     *
     * @param sandboxInstanceRefId the sandbox instance id
     */
    public void setSandboxInstanceRefId(Long sandboxInstanceRefId) {
        this.sandboxInstanceRefId = sandboxInstanceRefId;
    }

    /**
     * Gets current phase.
     *
     * @return the {@link AbstractPhaseDTO}
     */
    public AbstractPhaseDTO getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Sets current phase.
     *
     * @param currentPhase the {@link AbstractPhaseDTO}
     */
    public void setCurrentPhase(AbstractPhaseDTO currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * Gets basic info about all phase.
     *
     * @return the list of {@link BasicPhaseInfoDTO}
     */
    public List<BasicPhaseInfoDTO> getInfoAboutPhases() {
        return infoAboutPhases;
    }

    /**
     * Sets basic info about all phase.
     *
     * @param infoAboutPhases the list of {@link BasicPhaseInfoDTO}
     */
    public void setInfoAboutPhases(List<BasicPhaseInfoDTO> infoAboutPhases) {
        this.infoAboutPhases = infoAboutPhases;
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
     * Gets taken solution.
     *
     * @return the taken solution
     */
    public String getTakenSolution() {
        return takenSolution;
    }

    /**
     * Sets taken solution.
     *
     * @param takenSolution the taken solution
     */
    public void setTakenSolution(String takenSolution) {
        this.takenSolution = takenSolution;
    }

    /**
     * Gets if local environment (local sandboxes) is used for the training runs.
     *
     * @return true if local environment is enabled
     */
    public boolean isLocalEnvironment() {
        return localEnvironment;
    }

    /**
     * Sets if local environment (local sandboxes) is used for the training runs.
     *
     * @param localEnvironment true if local environment is enabled.
     */
    public void setLocalEnvironment(boolean localEnvironment) {
        this.localEnvironment = localEnvironment;
    }

    /**
     * Gets sandbox definition id.
     *
     * @return the sandbox definition id
     */
    public Long getSandboxDefinitionId() {
        return sandboxDefinitionId;
    }

    /**
     * Sets sandbox definition id.
     *
     * @param sandboxDefinitionId the sandbox definition id
     */
    public void setSandboxDefinitionId(Long sandboxDefinitionId) {
        this.sandboxDefinitionId = sandboxDefinitionId;
    }

    /**
     * Gets if trainee can during training run move back to the previous phases.
     *
     * @return true if backward mode is enabled.
     */
    public boolean isBackwardMode() {
        return backwardMode;
    }

    /**
     * Sets if trainee can during training run move back to the previous phases.
     *
     * @param backwardMode true if backward mode is enabled.
     */
    public void setBackwardMode(boolean backwardMode) {
        this.backwardMode = backwardMode;
    }

    public boolean isPhaseAnswered() {
        return isPhaseAnswered;
    }

    public void setPhaseAnswered(boolean phaseAnswered) {
        isPhaseAnswered = phaseAnswered;
    }

    @Override
    public String toString() {
        return "AccessTrainingRunDTO{" +
                "trainingRunID=" + trainingRunID +
                ", showStepperBar=" + showStepperBar +
                ", sandboxInstanceRefId=" + sandboxInstanceRefId +
                ", abstractPhaseDTO=" + currentPhase +
                ", instanceId=" + instanceId +
                ", startTime=" + startTime +
                ", takenSolution='" + takenSolution + '\'' +
                ", localEnvironment='" + localEnvironment +
                ", sandboxDefinitionId='" + sandboxDefinitionId +
                ", backwardMode='" + backwardMode +
                ", isPhaseAnswered='" + isPhaseAnswered +
                '}';
    }
}
