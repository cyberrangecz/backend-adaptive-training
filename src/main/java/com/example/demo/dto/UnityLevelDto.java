package com.example.demo.dto;

import java.util.List;

public class UnityLevelDto extends BaseLevelDto {

    // TODO any class extending BaseLevel probable should be used here - this piece of art is here in order to make mapstruct happy
    private List<GameLevelDto> subLevels;

    public List<GameLevelDto> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<GameLevelDto> subLevels) {
        this.subLevels = subLevels;
    }
}
