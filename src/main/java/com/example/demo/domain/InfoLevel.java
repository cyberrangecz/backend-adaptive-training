package com.example.demo.domain;

import javax.persistence.Entity;

@Entity
public class InfoLevel extends BaseLevel {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoLevel{" + ", content='" + content + '\'' + "} " + super.toString();
    }
}
