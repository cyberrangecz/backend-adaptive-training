package com.example.demo.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
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
}
