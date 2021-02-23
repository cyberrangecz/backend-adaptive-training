package cz.muni.ics.kypo.training.adaptive.dto.imports.phases.info;

import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.AbstractPhaseImportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * The type Info phase import dto. * Encapsulates information about info phase. Inherits from {@link AbstractPhaseImportDTO}
 */
@ApiModel(value = "InfoPhaseImportDTO", description = "An imported info phase.", parent = AbstractPhaseImportDTO.class)
public class InfoPhaseImportDTO extends AbstractPhaseImportDTO {

    @ApiModelProperty(value = "The information and experiences that are directed towards a participant.", example = "Informational stuff")
    @NotEmpty(message = "{info.content.NotEmpty.message}")
    private String content;

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoPhaseImportDTO{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfoPhaseImportDTO)) return false;
        if (!super.equals(o)) return false;
        InfoPhaseImportDTO that = (InfoPhaseImportDTO) o;
        return Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent());
    }
}
