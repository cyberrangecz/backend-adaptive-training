package com.example.demo.dto;

import com.example.demo.dto.input.LevelType;

import java.io.Serializable;

public abstract class BaseLevelDto implements Serializable {

    private Long id;
    private String title;
    private Integer order;
    private String estimatedDuration;
    private Long maxScore;
    private LevelType type;
    private Integer maxCommands;
    private Integer maxWrongFlags;

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

    public LevelType getType() {
        return type;
    }

    public void setType(LevelType type) {
        this.type = type;
    }

    public Integer getMaxCommands() {
        return maxCommands;
    }

    public void setMaxCommands(Integer maxCommands) {
        this.maxCommands = maxCommands;
    }

    public Integer getMaxWrongFlags() {
        return maxWrongFlags;
    }

    public void setMaxWrongFlags(Integer maxWrongFlags) {
        this.maxWrongFlags = maxWrongFlags;
    }

    @Override
    public String toString() {
        return "BaseLevelDto{" + "id=" + id + ", title='" + title + '\'' + ", order=" + order +
               ", estimatedDuration='" + estimatedDuration + '\'' + ", maxScore=" + maxScore + '}';
    }
}
