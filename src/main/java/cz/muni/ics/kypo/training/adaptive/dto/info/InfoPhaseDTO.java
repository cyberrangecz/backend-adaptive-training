package cz.muni.ics.kypo.training.adaptive.dto.info;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModelProperty;

public class InfoPhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "Short description of info phase", required = true, example = "Info phase title")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "InfoPhaseDto{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
