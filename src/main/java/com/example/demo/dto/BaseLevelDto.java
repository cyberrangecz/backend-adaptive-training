package com.example.demo.dto;

import com.example.demo.dto.input.LevelType;

import java.io.Serializable;

public abstract class BaseLevelDto implements Serializable {

    private Long id;
    private String title;
    private Integer order;
    private String estimatedDuration;
    private Long maxScore;
    private LevelType levelType;
    private Integer allowedCommands;
    private Integer allowedWrongFlags;

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

    public LevelType getLevelType() {
        return levelType;
    }

    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }

    public Integer getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(Integer allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    public Integer getAllowedWrongFlags() {
        return allowedWrongFlags;
    }

    public void setAllowedWrongFlags(Integer allowedWrongFlags) {
        this.allowedWrongFlags = allowedWrongFlags;
    }

    @Override
    public String toString() {
        return "BaseLevelDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", estimatedDuration='" + estimatedDuration + '\'' +
                ", maxScore=" + maxScore +
                ", levelType=" + levelType +
                ", allowedCommands=" + allowedCommands +
                ", allowedWrongFlags=" + allowedWrongFlags +
                '}';
    }
}
