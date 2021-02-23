package cz.muni.ics.kypo.training.adaptive.dto.archive.phases.info;

import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.AbstractPhaseArchiveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Encapsulates information about info phase. Inherits from {@link AbstractPhaseArchiveDTO}
 * Used for archiving.
 */
@ApiModel(value = "InfoPhaseArchiveDTO", description = "Archived info phase.", parent = AbstractPhaseArchiveDTO.class)
public class InfoPhaseArchiveDTO extends AbstractPhaseArchiveDTO {

    @ApiModelProperty(value = "The information and experiences that are directed towards a participant.", example = "Informational stuff")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfoPhaseArchiveDTO)) return false;
        InfoPhaseArchiveDTO that = (InfoPhaseArchiveDTO) o;
        return Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent());
    }

    @Override
    public String toString() {
        return "InfoPhaseArchiveDTO{" +
                "content='" + content + '\'' +
                '}';
    }
}
