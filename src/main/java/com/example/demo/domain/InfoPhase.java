package com.example.demo.domain;

import javax.persistence.Entity;

@Entity
public class InfoPhase extends AbstractPhase {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoPhase{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
