package cz.muni.ics.kypo.training.adaptive.domain.phase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "info_phase")
@PrimaryKeyJoinColumn(name = "phase_id")
public class InfoPhase extends AbstractPhase {

    @Column(name = "content")
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
                "content='" + this.getContent() + '\'' +
                ", title='" + super.getTitle() + '\'' +
                ", order=" + super.getOrder() +
                ", id=" + super.getId() +
                '}';
    }
}
