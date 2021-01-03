package com.example.demo.dto;

import java.io.Serializable;

public class InfoLevelDto extends BaseLevelDto implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "InfoLevelDto{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
