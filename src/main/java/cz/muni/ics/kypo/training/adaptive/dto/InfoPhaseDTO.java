package cz.muni.ics.kypo.training.adaptive.dto;

import java.io.Serializable;

public class InfoPhaseDTO extends AbstractPhaseDTO implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "InfoPhaseDto{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
