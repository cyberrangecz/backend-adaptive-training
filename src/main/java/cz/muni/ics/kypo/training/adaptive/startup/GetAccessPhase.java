package cz.muni.ics.kypo.training.adaptive.startup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class GetAccessPhase {

    @NotEmpty(message = "{abstractPhase.title.NotEmpty.message}")
    private String title;
    @NotEmpty(message = "{accessPhase.cloudContent.NotEmpty.message}")
    private String cloudContent;
    @NotEmpty(message = "{accessPhase.localContent.NotEmpty.message}")
    private String localContent;
    @Size(max = 50, message = "{accessPhase.passkey.Size.message}")
    @NotEmpty(message = "{accessPhase.passkey.NotEmpty.message}")
    private String passkey;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCloudContent() {
        return cloudContent;
    }

    public void setCloudContent(String cloudContent) {
        this.cloudContent = cloudContent;
    }

    public String getLocalContent() {
        return localContent;
    }

    public void setLocalContent(String localContent) {
        this.localContent = localContent;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }
}
