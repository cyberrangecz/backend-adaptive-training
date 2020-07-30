package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameLevelUpdateDto extends BaseLevelDto {

    private String content;
    private boolean solutionPenalized;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    private List<AttachmentDto> attachments;
    private List<HintDto> hints;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getSolutionPenalized() {
        return solutionPenalized;
    }

    public void setSolutionPenalized(boolean solutionPenalized) {
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

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public Long getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(Long incorrectFlagLimit) {
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
               "content='" + content + '\'' +
               ", solutionPenalized='" + solutionPenalized + '\'' +
               ", flag='" + flag + '\'' +
               ", solution='" + solution + '\'' +
               ", attachments='" + attachments + '\'' +
               ", incorrectFlagLimit='" + incorrectFlagLimit + '\'' +
               ", hints=" + hints +
               "} " + super.toString();
    }
}
