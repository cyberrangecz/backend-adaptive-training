package cz.muni.ics.kypo.training.adaptive.dto.info;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;
@ApiModel(
        value = "InfoPhaseUpdateDTO"
)
public class InfoPhaseUpdateDTO extends AbstractPhaseUpdateDTO {

    @ApiModelProperty(value = "The information of info phase that is displayed to a player", required = true, example = "Read the info")
    @NotEmpty(message = "Info phase content must not be blank")
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
        if (!super.equals(o)) return false;
        InfoPhaseUpdateDTO that = (InfoPhaseUpdateDTO) o;
        return Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent());
    }


    @Override
    public String toString() {
        return "InfoPhaseUpdateDTO{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", phaseType=" + getPhaseType() +
                ", content='" + content + '\'' +
                '}';
    }
}
