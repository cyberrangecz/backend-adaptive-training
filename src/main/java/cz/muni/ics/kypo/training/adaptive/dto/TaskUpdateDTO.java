package cz.muni.ics.kypo.training.adaptive.dto;

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
    @NotEmpty(message = "Flag of task cannot be null")
    private String flag;

    @ApiModelProperty(value = "Description how to get the flag", required = true, example = "Open secret.txt")
    @NotEmpty(message = "Solution of task cannot be null")
    private String solution;

    @ApiModelProperty(value = "It defines the allowed number of incorrect flags submitted by the player", required = true, example = "5")
    @NotNull(message = "Limit of the number of provided incorrect flags must be specified")
    @PositiveOrZero(message = "Limit of the number of provided incorrect flags must not be a negative number")
    private Integer incorrectFlagLimit;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Integer getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(Integer incorrectFlagLimit) {
        this.incorrectFlagLimit = incorrectFlagLimit;
    }

    @Override
    public String toString() {
        return "TaskUpdateDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", flag='" + flag + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectFlagLimit=" + incorrectFlagLimit +
                '}';
    }
}
