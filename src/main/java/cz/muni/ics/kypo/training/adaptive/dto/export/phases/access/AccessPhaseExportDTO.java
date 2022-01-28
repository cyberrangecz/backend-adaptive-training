package cz.muni.ics.kypo.training.adaptive.dto.export.phases.access;

import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Encapsulates information about info phase. Inherits from {@link AbstractPhaseArchiveDTO}
 * Used for archiving.
 */
@ApiModel(
        value = "AccessPhaseExportDTO",
        description = "Exported access phase.",
        parent = AccessPhaseExportDTO.class
)
public class AccessPhaseExportDTO extends AbstractPhaseExportDTO {

    @ApiModelProperty(value = "Keyword used for access next level.", example = "secretAnswer")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessPhaseExportDTO)) return false;
        AccessPhaseExportDTO that = (AccessPhaseExportDTO) o;
        return Objects.equals(getPasskey(), that.getPasskey()) &&
                Objects.equals(getCloudContent(), that.getCloudContent()) &&
                Objects.equals(getLocalContent(), that.getLocalContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPasskey(), getCloudContent(), getLocalContent());
    }

    @Override
    public String toString() {
        return "AccessPhaseExportDTO{" +
                "passkey='" + passkey + '\'' +
                "cloudContent='" + cloudContent + '\'' +
                "localContent='" + localContent + '\'' +
                '}';
    }
}
