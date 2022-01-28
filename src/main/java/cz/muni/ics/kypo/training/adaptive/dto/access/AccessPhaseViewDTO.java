package cz.muni.ics.kypo.training.adaptive.dto.access;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Encapsulates basic information about access level.
 */

@ApiModel(value = "AccessPhaseViewDTO", description = "A level containing instructions on how to connect to the virtual machines.", parent = AbstractPhaseDTO.class)
public class AccessPhaseViewDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "The instructions on how to connect to the machine in cloud environment.", example = "Connect using SSH config.")
    private String cloudContent;
    @ApiModelProperty(value = "The instructions on how to connect to the machine in local (non-cloud) environment.", example = "Use vagrant SSH connection.")
    private String localContent;

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
        return "TrainingLevelViewDTO{" +
                "cloudContent='" + cloudContent + '\'' +
                "localContent='" + localContent + '\'' +
                '}';
    }
}
