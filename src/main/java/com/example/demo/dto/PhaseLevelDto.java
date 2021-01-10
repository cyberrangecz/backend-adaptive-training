package com.example.demo.dto;

import java.util.List;

public class PhaseLevelDto extends BaseLevelDto {

    // TODO any class extending BaseLevel probable should be used here - this piece of art is here in order to make mapstruct happy
    private List<TaskDto> subLevels;

    public List<TaskDto> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<TaskDto> subLevels) {
        this.subLevels = subLevels;
    }
}
