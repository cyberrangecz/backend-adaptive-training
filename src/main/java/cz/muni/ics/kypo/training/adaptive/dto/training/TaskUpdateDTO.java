package cz.muni.ics.kypo.training.adaptive.dto.training;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class TaskUpdateDTO {

    @ApiModelProperty(value = "Short description of task", required = true, example = "Task title")
    @NotEmpty(message = "Task title must not be blank")
    private String title;

    @ApiModelProperty(value = "The information that are displayed to a player", required = true, example = "Capture the flag")
    @NotEmpty(message = "Task content must not be blank")
    private String content;

    @ApiModelProperty(value = "Keyword that must be found in the task. Necessary in order to get to the next phase", required = true, example = "secretFlag")
    @NotEmpty(message = "Answer of task cannot be null")
    private String answer;

    @ApiModelProperty(value = "Description how to get the answer", required = true, example = "Open secret.txt")
    @NotEmpty(message = "Solution of task cannot be null")
    private String solution;

    @ApiModelProperty(value = "It defines the allowed number of incorrect answers submitted by the player", required = true, example = "5")
    @NotNull(message = "Limit of the number of provided incorrect answers must be specified")
    @PositiveOrZero(message = "Limit of the number of provided incorrect answers must not be a negative number")
    private Integer incorrectAnswerLimit;

    @ApiModelProperty(value = "It defines whether the sandbox can be modified", example = "true")
    private boolean isSandboxModified;

    @ApiModelProperty(value = "It defines the expected duration of sandbox change defined in seconds", example = "15")
    @PositiveOrZero(message = "Estimated duration of sandbox change must not be a negative number")
    private int sandboxChangeExpectedDuration;

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

    public boolean isSandboxModified() {
        return isSandboxModified;
    }

    public void setSandboxModified(boolean sandboxModified) {
        isSandboxModified = sandboxModified;
    }

    public int getSandboxChangeExpectedDuration() {
        return sandboxChangeExpectedDuration;
    }

    public void setSandboxChangeExpectedDuration(int sandboxChangeExpectedDuration) {
        this.sandboxChangeExpectedDuration = sandboxChangeExpectedDuration;
    }

    @Override
    public String toString() {
        return "TaskUpdateDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectAnswerLimit=" + incorrectAnswerLimit +
                ", isSandboxModified=" + isSandboxModified +
                ", sandboxChangeExpectedDuration=" + sandboxChangeExpectedDuration +
                '}';
    }
}
