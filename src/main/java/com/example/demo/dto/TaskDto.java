package com.example.demo.dto;

import java.io.Serializable;

public class TaskDto extends TaskUpdateDto implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "TaskDto{" +
            "id=" + id +
            "} " + super.toString();
    }
}
