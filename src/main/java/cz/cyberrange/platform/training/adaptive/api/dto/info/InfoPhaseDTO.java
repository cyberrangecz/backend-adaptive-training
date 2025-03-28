package cz.cyberrange.platform.training.adaptive.api.dto.info;

import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(
        value = "InfoPhaseDTO"
)
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
