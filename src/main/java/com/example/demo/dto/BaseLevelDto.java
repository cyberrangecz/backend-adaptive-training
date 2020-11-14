package com.example.demo.dto;

import java.io.Serializable;

public abstract class BaseLevelDto implements Serializable {

    private Long id;
    private String title;
    private Integer order;
    private String estimatedDuration;
    private Long maxScore;

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
        return "BaseLevelDto{" + "id=" + id + ", title='" + title + '\'' + ", order=" + order +
               ", estimatedDuration='" + estimatedDuration + '\'' + ", maxScore=" + maxScore + '}';
    }
}
