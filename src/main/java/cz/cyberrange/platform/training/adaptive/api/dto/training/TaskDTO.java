package cz.cyberrange.platform.training.adaptive.api.dto.training;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(
        value = "TaskDTO"
)
public class TaskDTO {

    @ApiModelProperty(value = "ID of task", required = true, example = "1")
    private Long id;
    @ApiModelProperty(value = "Short description of task", required = true, example = "Task title")
    private String title;
    @ApiModelProperty(value = "Order of task in the training phase", required = true, example = "1")
    private Integer order;
    @ApiModelProperty(value = "The information that are displayed to a player", required = true, example = "Capture the flag")
    private String content;
    @ApiModelProperty(value = "Keyword that must be found in the task. Necessary in order to get to the next phase", required = true, example = "secretFlag")
    private String answer;
    @ApiModelProperty(value = "Description how to get the answer", required = true, example = "Open secret.txt")
    private String solution;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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
        TaskDTO taskDTO = (TaskDTO) o;
        return incorrectAnswerLimit == taskDTO.incorrectAnswerLimit &&
                modifySandbox == taskDTO.modifySandbox &&
                sandboxChangeExpectedDuration == taskDTO.sandboxChangeExpectedDuration &&
                Objects.equals(id, taskDTO.id) &&
                Objects.equals(title, taskDTO.title) &&
                Objects.equals(order, taskDTO.order) &&
                Objects.equals(content, taskDTO.content) &&
                Objects.equals(answer, taskDTO.answer) &&
                Objects.equals(solution, taskDTO.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, order, content, answer, solution, incorrectAnswerLimit, modifySandbox, sandboxChangeExpectedDuration);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectAnswerLimit=" + incorrectAnswerLimit +
                ", modifySandbox=" + modifySandbox +
                ", sandboxChangeExpectedDuration=" + sandboxChangeExpectedDuration +
                '}';
    }
}
