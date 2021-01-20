package com.example.demo.dto;

import java.util.List;

public class TaskUpdateDto {

    private Long id;
    private String title;
    private String content;
    private boolean solutionPenalized;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    private List<AttachmentDto> attachments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSolutionPenalized() {
        return solutionPenalized;
    }

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

    @Override
    public String toString() {
        return "TaskUpdateDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", solutionPenalized=" + solutionPenalized +
                ", flag='" + flag + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectFlagLimit=" + incorrectFlagLimit +
                ", attachments=" + attachments +
                '}';
    }
}
