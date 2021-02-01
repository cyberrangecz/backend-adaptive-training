package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Entity
public class Task extends AbstractPhase {

    private String content;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingPhase trainingPhase;

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

    public TrainingPhase getTrainingPhase() {
        return trainingPhase;
    }

    public void setTrainingPhase(TrainingPhase trainingPhase) {
        this.trainingPhase = trainingPhase;
    }

    @Override
    public String toString() {
        return "Task{" + "content='" + content + '\'' + ", flag='" +
                flag + '\'' + ", solution='" + solution + '\'' + ", incorrectFlagLimit=" + incorrectFlagLimit +
                super.toString();
    }
}
