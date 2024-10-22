package cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.access;

import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.AbstractPhaseImportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Encapsulates information about info phase. Inherits from {@link AbstractPhaseArchiveDTO}
 * Used for archiving.
 */
@ApiModel(
        value = "AccessPhaseImportDTO",
        description = "Imported access phase.",
        parent = AbstractPhaseImportDTO.class
)
public class AccessPhaseImportDTO extends AbstractPhaseImportDTO {

    @ApiModelProperty(value = "Keyword used for access next level.", example = "secretAnswer")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessPhaseImportDTO)) return false;
        AccessPhaseImportDTO that = (AccessPhaseImportDTO) o;
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
        return "AccessPhaseImportDTO{" +
                "passkey='" + passkey + '\'' +
                "cloudContent='" + cloudContent + '\'' +
                "localContent='" + localContent + '\'' +
                '}';
    }
}
