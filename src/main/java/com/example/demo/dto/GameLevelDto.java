package com.example.demo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameLevelDto extends BaseLevelDto implements Serializable {

    private Long id;

    private String content;
    private String solutionPenalized;
    private String flag;
    private String solution;
    private String attachments;
    private String incorrectFlagLimit;

    private List<HintDto> hints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolutionPenalized() {
        return solutionPenalized;
    }

    public void setSolutionPenalized(String solutionPenalized) {
        this.solutionPenalized = solutionPenalized;
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

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(String incorrectFlagLimit) {
        this.incorrectFlagLimit = incorrectFlagLimit;
    }

    public List<HintDto> getHints() {
        if (Objects.isNull(hints)) {
            hints = new ArrayList<>();
        }
        return hints;
    }

    public void setHints(List<HintDto> hints) {
        this.hints = hints;
    }

    @Override
    public String toString() {
        return "GameLevelDto{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", solutionPenalized='" + solutionPenalized + '\'' +
            ", flag='" + flag + '\'' +
            ", solution='" + solution + '\'' +
            ", attachments='" + attachments + '\'' +
            ", incorrectFlagLimit='" + incorrectFlagLimit + '\'' +
            ", hints=" + hints +
            "} " + super.toString();
    }
}
