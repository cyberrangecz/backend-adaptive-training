package cz.muni.ics.kypo.training.adaptive.startup;

import javax.validation.constraints.NotEmpty;

public class DefaultInfoPhase {
    @NotEmpty(message = "{phase.title.NotEmpty.message}")
    private String title;
    @NotEmpty(message = "{infoPhase.content.NotEmpty.message}")
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
}
