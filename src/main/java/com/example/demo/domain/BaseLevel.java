package com.example.demo.domain;


import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseLevel {
    private String title;
    private String estimatedDuration;
    private Long maxScore;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Long getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Long maxScore) {
        this.maxScore = maxScore;
    }

    @Override
    public String toString() {
        return "BaseLevel{" +
            "title='" + title + '\'' +
            ", estimatedDuration='" + estimatedDuration + '\'' +
            ", maxScore=" + maxScore +
            '}';
    }
}
