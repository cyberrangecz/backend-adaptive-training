package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "access_phase")
@PrimaryKeyJoinColumn(name = "phase_id")
public class AccessPhase extends AbstractPhase {

    @Column(name = "passkey")
    private String passkey;
    @Column(name = "cloud_content", nullable = false)
    private String cloudContent;
    @Column(name = "local_content", nullable = false)
    private String localContent;

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
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

    @Override
    public String toString() {
        return "InfoPhase{" +
                "passkey='" + this.getPasskey() + '\'' +
                "cloudContent='" + this.getCloudContent() + '\'' +
                "localContent='" + this.getLocalContent() + '\'' +
                ", title='" + super.getTitle() + '\'' +
                ", order=" + super.getOrder() +
                ", id=" + super.getId() +
                '}';
    }
}
