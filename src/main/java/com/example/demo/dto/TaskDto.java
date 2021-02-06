package com.example.demo.dto;

import java.io.Serializable;

public class TaskDto implements Serializable {

    private Long id;
    private String title;
    private Integer order;
    private String content;
    private String flag;
    private String solution;
    private int incorrectFlagLimit;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

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

    public int getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(int incorrectFlagLimit) {
        this.incorrectFlagLimit = incorrectFlagLimit;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", content='" + content + '\'' +
                ", flag='" + flag + '\'' +
                ", solution='" + solution + '\'' +
                ", incorrectFlagLimit=" + incorrectFlagLimit +
                "} " + super.toString();
    }
}
