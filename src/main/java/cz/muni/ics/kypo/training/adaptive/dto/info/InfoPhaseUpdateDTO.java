package cz.muni.ics.kypo.training.adaptive.dto.info;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class InfoPhaseUpdateDTO {

    @ApiModelProperty(value = "Short description of info phase", required = true, example = "Info phase title")
    @NotEmpty(message = "Info phase title must not be blank")
    private String title;

    @ApiModelProperty(value = "The information of info phase that is displayed to a player", required = true, example = "Read the info")
    @NotEmpty(message = "Info phase content must not be blank")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
        InfoPhaseUpdateDTO that = (InfoPhaseUpdateDTO) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

    @Override
    public String toString() {
        return "InfoPhaseUpdateDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
