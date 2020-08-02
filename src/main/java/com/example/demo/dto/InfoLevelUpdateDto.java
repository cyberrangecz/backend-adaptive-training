package com.example.demo.dto;

public class InfoLevelUpdateDto extends BaseLevelDto {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoLevelUpdateDto{" + "content='" + content + '\'' + '}';
    }
}
