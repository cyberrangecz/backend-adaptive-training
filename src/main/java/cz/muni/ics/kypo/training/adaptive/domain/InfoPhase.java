package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "info_phase")
public class InfoPhase extends AbstractPhase {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoPhase{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
