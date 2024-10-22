package cz.cyberrange.platform.training.adaptive.api.dto.access;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Encapsulates information needed to update training level.
 *
 */
@ApiModel(value = "AccessPhaseUpdateDTO", description = "Access level to update.")
public class AccessPhaseUpdateDTO extends AbstractPhaseUpdateDTO {

    @ApiModelProperty(value = "Keyword found in training, used for access next level.", required = true, example = "secretAnswer")
    @Size(max = 50, message = "{accessPhase.passkey.Size.message}")
    @NotEmpty(message = "{accessPhase.passkey.NotEmpty.message}")
    private String passkey;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in cloud environment.", example = "Connect using SSH config.")
    @NotEmpty(message = "{accessPhase.cloudContent.NotEmpty.message}")
    private String cloudContent;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in local (non-cloud) environment.", example = "Use vagrant SSH connection.")
    @NotEmpty(message = "{accessPhase.localContent.NotEmpty.message}")
    private String localContent;

    /**
     * Gets passkey.
     *
     * @return the passkey
     */
    public String getPasskey() {
        return passkey;
    }

    /**
     * Sets passkey.
     *
     * @param passkey the passkey
     */
    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getCloudContent() {
        return cloudContent;
    }

    /**
     * Sets content.
     *
     * @param cloudContent the content
     */
    public void setCloudContent(String cloudContent) {
        this.cloudContent = cloudContent;
    }

    /**
     * Gets instructions on how to access machine in local (non-cloud) environment
     *
     * @return the local content
     */
    public String getLocalContent() {
        return localContent;
    }

    /**
     * Sets instructions on how to access machine in local (non-cloud) environment
     *
     * @param localContent the local content
     */
    public void setLocalContent(String localContent) {
        this.localContent = localContent;
    }

    @Override
    public String toString() {
        return "AccessPhaseUpdateDTO{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", passkey='" + passkey + '\'' +
                ", cloudContent='" + cloudContent + '\'' +
                ", localContent='" + localContent + '\'' +
                '}';
    }
}
