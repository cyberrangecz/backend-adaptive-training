package com.example.demo.dto.input;

import java.util.List;

public class GameDefinitionCreateDto {
    private Long id;
    private String title;
    private Integer order;
    private LevelType type;
    private List<? extends GameDefinitionCreateDto> subLevels;

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

    public LevelType getType() {
        return type;
    }

    public void setType(LevelType type) {
        this.type = type;
    }

    public List<? extends GameDefinitionCreateDto> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<? extends GameDefinitionCreateDto> subLevels) {
        this.subLevels = subLevels;
    }
}
