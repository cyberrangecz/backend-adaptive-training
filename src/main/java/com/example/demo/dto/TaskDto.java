package com.example.demo.dto;

import java.io.Serializable;
import java.util.List;

public class TaskDto extends BaseLevelDto implements Serializable {

    private String content;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    private List<AttachmentDto> attachments;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "TaskDto{" +
                "content='" + content + '\'' +
                ", flag='" + flag + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectFlagLimit='" + incorrectFlagLimit + '\'' +
                ", attachments=" + attachments +
                "} " + super.toString();
    }
}
