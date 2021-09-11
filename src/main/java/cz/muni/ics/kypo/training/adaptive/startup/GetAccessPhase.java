package cz.muni.ics.kypo.training.adaptive.startup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetAccessPhase {

    @NotEmpty(message = "{trainingPhase.title.NotEmpty.message}")
    private String title;
    @NotNull(message = "{trainingPhase.estimatedDuration.NotNull.message}")
    @Min(value = 0, message = "{trainingPhase.estimatedDuration.Min.message}")
    private Integer estimatedDuration;
    @NotEmpty(message = "{getAccessPhase.content.NotEmpty.message}")
    private String content;
    @NotEmpty(message = "{getAccessPhase.answer.NotEmpty.message}")
    @Size(max = 50, message = "{getAccessPhase.answer.Size.message}")
    private String answer;
    @NotEmpty(message = "{getAccessPhase.solution.NotEmpty.message}")
    private String solution;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
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
}
