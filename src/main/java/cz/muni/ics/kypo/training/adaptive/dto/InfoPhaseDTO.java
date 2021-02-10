package cz.muni.ics.kypo.training.adaptive.dto;

public class InfoPhaseDTO extends AbstractPhaseDTO {

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
