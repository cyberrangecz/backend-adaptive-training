package com.example.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;


@Entity
public class Task extends BaseLevel {

    private String content;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    @OrderBy
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Attachment> attachments;

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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
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
        return "Task{" + "content='" + content + '\'' + ", flag='" +
                flag + '\'' + ", solution='" + solution + '\'' + ", incorrectFlagLimit=" + incorrectFlagLimit +
                ", attachments=" + attachments + "} " + super.toString();
    }
}
