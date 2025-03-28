package cz.cyberrange.platform.training.adaptive.api.dto.archive.training;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.cyberrange.platform.training.adaptive.utils.converter.LocalDateTimeUTCSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Encapsulates information about Training instance.
 * Used for archiving
 */
@ApiModel(
        value = "TrainingInstanceArchiveDTO",
        description = "The finished and archived instance of training definition which includes individual finished training runs of participants."
)
public class TrainingInstanceArchiveDTO {

    @ApiModelProperty(value = "Main identifier of training instance.", example = "1")
    private Long id;
    @ApiModelProperty(value = "Main identifier of training definition associated with this instance.", example = "1")
    private Long definitionId;
    @ApiModelProperty(value = "Short textual description of the training instance.", example = "Concluded Instance")
    private String title;
    @ApiModelProperty(value = "Date when training instance starts.", example = "2016-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "Date when training instance ends.", example = "2017-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime endTime;
    @ApiModelProperty(value = "Token needed to access runs created from this definition", example = "pass-1234")
    private String accessToken;
    @ApiModelProperty(value = "Reference to organizersRefIds which organize training instance.")
    private Set<Long> organizersRefIds;
    @ApiModelProperty(value = "Indicates if local sandboxes are used for training runs.", example = "true")
    private boolean localEnvironment;
    @ApiModelProperty(value = "Indicates if trainee can during training run move to the previous already solved phases.", example = "true")
    private boolean backwardMode;
    @ApiModelProperty(value = "Sign if stepper bar should be displayed.", example = "false")
    private boolean showStepperBar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    /**
     * Gets start time of training instance.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets start time of training instance.
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time of training instance.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets end time of training instance.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets title of training instance.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of training instance.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets organizersRefIds of training instance.
     *
     * @return the organizersRefIds
     */
    public Set<Long> getOrganizersRefIds() {
        return organizersRefIds;
    }

    /**
     * Sets organizersRefIds of training instance.
     *
     * @param organizersRefIds the organizersRefIds
     */
    public void setOrganizersRefIds(Set<Long> organizersRefIds) {
        this.organizersRefIds = organizersRefIds;
    }

    /**
     * Gets access token of training instance.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets access token of training instance.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
        return "TrainingInstanceArchiveDTO{" +
                "id=" + id +
                ", definitionId=" + definitionId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", title='" + title + '\'' +
                ", organizersRefIds=" + organizersRefIds +
                ", accessToken='" + accessToken + '\'' +
                ", localEnvironment=" + localEnvironment +
                ", backwardMode=" + backwardMode +
                ", showStepperBar=" + showStepperBar +
                '}';
    }
}
