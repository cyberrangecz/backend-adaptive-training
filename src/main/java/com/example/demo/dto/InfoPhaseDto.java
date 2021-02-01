package com.example.demo.dto;

import java.io.Serializable;

public class InfoPhaseDto extends AbstractPhaseDto implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "InfoPhaseDto{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
