package cz.cyberrange.platform.training.adaptive.api.dto.access;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Encapsulates information about training level. Inherits from {@link AbstractPhaseDTO}
 *
 */
@ApiModel(value = "AccessPhaseDTO", description = "A level containing instructions on how to connect to the virtual machines.", parent = AbstractPhaseDTO.class)
public class AccessPhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Keyword found in training, used for access next level.", example = "secretAnswer")
    private String passkey;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in cloud environment.", example = "Connect using SSH config.")
    private String cloudContent;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in local (non-cloud) environment.", example = "Use vagrant SSH connection.")
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
        return "AccessPhaseDTO{" +
                "passkey='" + passkey + '\'' +
                ", cloudContent='" + cloudContent + '\'' +
                ", localContent='" + localContent + '\'' +
                '}';
    }
}
