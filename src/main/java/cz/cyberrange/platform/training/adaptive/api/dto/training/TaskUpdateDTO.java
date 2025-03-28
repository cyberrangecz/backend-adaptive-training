package cz.cyberrange.platform.training.adaptive.api.dto.training;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@ApiModel(
        value = "TaskUpdateDTO"
)
public class TaskUpdateDTO {

    @ApiModelProperty(value = "Main identifier of the task", required = true, example = "1")
    @NotNull(message = "{task.id.NotNull.message}")
    private Long id;
    @ApiModelProperty(value = "Short description of task", required = true, example = "Task title")
    @NotEmpty(message = "{task.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "The information that are displayed to a player", required = true, example = "Capture the flag", position = 1)
    @NotEmpty(message = "{task.content.NotEmpty.message}")
    private String content;
    @ApiModelProperty(value = "Keyword that must be found in the task. Necessary in order to get to the next phase", required = true, example = "secretFlag", position = 2)
    @NotEmpty(message = "{task.answer.NotEmpty.message}")
    @Size(max = 50, message = "{task.answer.Size.message}")
    private String answer;
    @ApiModelProperty(value = "Description how to get the answer", required = true, example = "Open secret.txt", position = 3)
    @NotEmpty(message = "{task.solution.NotEmpty.message}")
    private String solution;
    @ApiModelProperty(value = "It defines the allowed number of incorrect answers submitted by the player", required = true, example = "5", position = 4)
    @NotNull(message = "{task.incorrectAnswerLimit.NotNull.message}")
    @Min(value = 0, message = "{task.incorrectAnswerLimit.Min.message}")
    @Max(value = 100, message = "{task.incorrectAnswerLimit.Max.message}")
    private Integer incorrectAnswerLimit;
    @ApiModelProperty(value = "It defines whether the sandbox can be modified", example = "true", position = 5)
    private boolean modifySandbox;
    @ApiModelProperty(value = "It defines the expected duration of sandbox change defined in seconds", example = "15", position = 1)
    @Min(value = 0, message = "{task.sandboxChangeExpectedDuration.Min.message}")
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Integer getIncorrectAnswerLimit() {
        return incorrectAnswerLimit;
    }

    public void setIncorrectAnswerLimit(Integer incorrectAnswerLimit) {
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
        TaskUpdateDTO that = (TaskUpdateDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }


    @Override
    public String toString() {
        return "TaskUpdateDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectAnswerLimit=" + incorrectAnswerLimit +
                ", modifySandbox=" + modifySandbox +
                ", sandboxChangeExpectedDuration=" + sandboxChangeExpectedDuration +
                '}';
    }
}
