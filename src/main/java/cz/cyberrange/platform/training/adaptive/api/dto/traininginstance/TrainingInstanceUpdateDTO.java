package cz.cyberrange.platform.training.adaptive.api.dto.traininginstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cz.cyberrange.platform.training.adaptive.utils.converter.LocalDateTimeUTCDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Encapsulates information about Training Instance, intended for edit of the instance.
 */
@ApiModel(
        value = "TrainingInstanceUpdateDTO",
        description = "Training Instance to update."
)
public class TrainingInstanceUpdateDTO {

    @ApiModelProperty(value = "Main identifier of training instance.", required = true, example = "2")
    @NotNull(message = "{trainingInstance.id.NotNull.message}")
    private Long id;
    @ApiModelProperty(value = "Short textual description of the training instance.", required = true, example = "Current Instance", position = 1)
    @NotEmpty(message = "{trainingInstance.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Date when training instance starts.", required = true, example = "2019-10-19T10:28:02.727Z", position = 2)
    @NotNull(message = "{trainingInstance.startTime.NotNull.message}")
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "Date when training instance ends.", required = true, example = "2019-10-25T10:28:02.727Z", position = 2)
    @NotNull(message = "{trainingInstance.endTime.NotNull.message}")
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime endTime;
    @ApiModelProperty(value = "AccessToken which will be modified and then used for accessing training run.", required = true, example = "hello-6578", position = 3)
    @NotEmpty(message = "{trainingInstance.accessToken.NotEmpty.message}")
    private String accessToken;
    @ApiModelProperty(value = "Reference to training definition from which is training instance created.", required = true, example = "1", position = 4)
    @NotNull(message = "{trainingInstance.trainingDefinitionId.NotNull.message}")
    private Long trainingDefinitionId;
    @ApiModelProperty(value = "Id of sandbox pool assigned to training instance", example = "1")
    private Long poolId;
    @ApiModelProperty(value = "Indicates if local sandboxes are used for training runs.", example = "true")
    private boolean localEnvironment;
    @ApiModelProperty(value = "Id of sandbox definition assigned to training instance", example = "1")
    private Long sandboxDefinitionId;
    @ApiModelProperty(value = "Indicates if trainee can during training run move to the previous already solved phases.", example = "true")
    private boolean backwardMode;
    @ApiModelProperty(value = "Sign if stepper bar should be displayed.", example = "false")
    private boolean showStepperBar;

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
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets access token.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets training definition id.
     *
     * @return the training definition id
     */
    public Long getTrainingDefinitionId() {
        return trainingDefinitionId;
    }

    /**
     * Sets training definition id.
     *
     * @param trainingDefinitionId the training definition id
     */
    public void setTrainingDefinitionId(Long trainingDefinitionId) {
        this.trainingDefinitionId = trainingDefinitionId;
    }

    /**
     * Gets pool id.
     *
     * @return the pool id
     */
    public Long getPoolId() {
        return poolId;
    }

    /**
     * Sets pool id.
     *
     * @param poolId the pool id
     */
    public void setPoolId(Long poolId) {
        this.poolId = poolId;
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

    /**
     * Is show stepper bar boolean.
     *
     * @return the boolean
     */
    public boolean isShowStepperBar() {
        return showStepperBar;
    }

    /**
     * Sets show stepper bar.
     *
     * @param showStepperBar the show stepper bar
     */
    public void setShowStepperBar(boolean showStepperBar) {
        this.showStepperBar = showStepperBar;
    }

    @Override
    public String toString() {
        return "TrainingInstanceUpdateDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", accessToken='" + accessToken + '\'' +
                ", trainingDefinitionId=" + trainingDefinitionId +
                ", poolId=" + poolId +
                ", localEnvironment=" + localEnvironment +
                ", sandboxDefinitionId=" + sandboxDefinitionId +
                ", backwardMode=" + backwardMode +
                ", showStepperBar=" + showStepperBar +
                '}';
    }
}
