package com.example.demo.dto;

import java.io.Serializable;

public class TaskDto extends AbstractPhaseDto implements Serializable {

    private String content;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

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
                "} " + super.toString();
    }
}
