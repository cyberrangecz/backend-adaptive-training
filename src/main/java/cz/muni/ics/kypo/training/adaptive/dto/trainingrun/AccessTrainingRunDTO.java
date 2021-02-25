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
@ApiModel(value = "AccessTrainingRunDTO", description = "Just accessed training run.")
public class AccessTrainingRunDTO {

    @ApiModelProperty(value = "Main identifier of training run.", example = "1")
    private Long trainingRunID;
    @ApiModelProperty(value = "Sign if stepper bar should be displayed.", example = "false")
    private boolean showStepperBar;
    @ApiModelProperty(value = "Main identifier of sandbox which is assigned to training run.", example = "2")
    private Long sandboxInstanceRefId;
    @ApiModelProperty(value = "First phase in the current training run.")
    private AbstractPhaseDTO abstractPhaseDTO;
    @ApiModelProperty(value = "Information about all phase in training instance.")
    private List<BasicPhaseInfoDTO> infoAboutPhases;
    @ApiModelProperty(value = "Id of associated training instance", example = "1")
    private Long instanceId;
    @ApiModelProperty(value = "Date when training run started.", example = "2016-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "Sign if solution of current training phase was taken", example = "true")
    private String takenSolution;

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
    public AbstractPhaseDTO getAbstractPhaseDTO() {
        return abstractPhaseDTO;
    }

    /**
     * Sets current phase.
     *
     * @param abstractPhaseDTO the {@link AbstractPhaseDTO}
     */
    public void setAbstractPhaseDTO(AbstractPhaseDTO abstractPhaseDTO) {
        this.abstractPhaseDTO = abstractPhaseDTO;
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


    @Override
    public String toString() {
        return "AccessTrainingRunDTO{" +
                "trainingRunID=" + trainingRunID +
                ", showStepperBar=" + showStepperBar +
                ", sandboxInstanceRefId=" + sandboxInstanceRefId +
                ", abstractPhaseDTO=" + abstractPhaseDTO +
                ", instanceId=" + instanceId +
                ", startTime=" + startTime +
                ", takenSolution='" + takenSolution + '\'' +
                '}';
    }
}
