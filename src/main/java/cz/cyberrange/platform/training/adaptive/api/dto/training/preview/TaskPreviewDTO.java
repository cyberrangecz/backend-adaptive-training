package cz.cyberrange.platform.training.adaptive.api.dto.training.preview;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class TaskPreviewDTO {

    @ApiModelProperty(value = "ID of task", example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of task", example = "Task title")
    private String title;
    @ApiModelProperty(value = "The information that are displayed to a player", example = "Capture the flag")
    private String content;
    @ApiModelProperty(value = "Description how to get the answer", example = "Open secret.txt")
    private String solution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPreviewDTO that = (TaskPreviewDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(solution, that.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, solution);
    }

    @Override
    public String toString() {
        return "TaskPreviewDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", solution='" + solution + '\'' +
                '}';
    }
}
