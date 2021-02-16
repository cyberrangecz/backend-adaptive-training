package cz.muni.ics.kypo.training.adaptive.dto.info;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoPhaseDTO that = (InfoPhaseDTO) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "InfoPhaseDto{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
