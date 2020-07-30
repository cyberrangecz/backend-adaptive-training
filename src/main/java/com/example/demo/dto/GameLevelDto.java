package com.example.demo.dto;

import java.io.Serializable;

public class GameLevelDto extends GameLevelUpdateDto implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "GameLevelDto{" +
            "id=" + id +
            "} " + super.toString();
    }
}
