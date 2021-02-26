package cz.muni.ics.kypo.training.adaptive.dto.training.view;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class TaskViewDTO {

    @ApiModelProperty(value = "ID of task", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of task", required = true, example = "Task title")
    private String title;
    @ApiModelProperty(value = "The information that are displayed to a player", required = true, example = "Capture the flag")
    private String content;
    @ApiModelProperty(value = "It defines the allowed number of incorrect answers submitted by the player", required = true, example = "5")
    private int incorrectAnswerLimit;
    @ApiModelProperty(value = "It defines whether the sandbox can be modified", example = "true")
    private boolean modifySandbox;
    @ApiModelProperty(value = "It defines the expected duration of sandbox change defined in seconds", example = "15")
    private int sandboxChangeExpectedDuration;

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

    public int getIncorrectAnswerLimit() {
        return incorrectAnswerLimit;
    }

    public void setIncorrectAnswerLimit(int incorrectAnswerLimit) {
        this.incorrectAnswerLimit = incorrectAnswerLimit;
    }

    public boolean isModifySandbox() {
        return modifySandbox;
    }

    public void setModifySandbox(boolean modifySandbox) {
        this.modifySandbox = modifySandbox;
    }

    public int getSandboxChangeExpectedDuration() {
        return sandboxChangeExpectedDuration;
    }

    public void setSandboxChangeExpectedDuration(int sandboxChangeExpectedDuration) {
        this.sandboxChangeExpectedDuration = sandboxChangeExpectedDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskViewDTO taskDTO = (TaskViewDTO) o;
        return incorrectAnswerLimit == taskDTO.incorrectAnswerLimit &&
                modifySandbox == taskDTO.modifySandbox &&
                sandboxChangeExpectedDuration == taskDTO.sandboxChangeExpectedDuration &&
                Objects.equals(id, taskDTO.id) &&
                Objects.equals(title, taskDTO.title) &&
                Objects.equals(content, taskDTO.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, incorrectAnswerLimit, modifySandbox, sandboxChangeExpectedDuration);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", incorrectAnswerLimit=" + incorrectAnswerLimit +
                ", modifySandbox=" + modifySandbox +
                ", sandboxChangeExpectedDuration=" + sandboxChangeExpectedDuration +
                '}';
    }
}
