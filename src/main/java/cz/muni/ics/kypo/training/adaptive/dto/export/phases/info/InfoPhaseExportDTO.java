package cz.muni.ics.kypo.training.adaptive.dto.export.phases.info;

import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Encapsulates information about info phase. Inherits from {@link AbstractPhaseExportDTO}
 */
@ApiModel(value = "InfoPhaseExportDTO", description = "Exported info phase.", parent = AbstractPhaseExportDTO.class)
public class InfoPhaseExportDTO extends AbstractPhaseExportDTO {

    @ApiModelProperty(value = "The information and experiences that are directed towards a participant.", example = "Informational stuff")
    private String content;

    /**
     * Instantiates a new Info phase export dto.
     */
    public InfoPhaseExportDTO() {
        this.content = "";
    }

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
        return "InfoPhaseExportDTO{" +
                "content='" + content + '\'' +
                '}';
    }
}
