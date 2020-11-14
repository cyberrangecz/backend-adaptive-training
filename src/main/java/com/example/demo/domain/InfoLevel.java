package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class InfoLevel extends BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoLevel{" +
            "id=" + id +
            ", content='" + content + '\'' +
            "} " + super.toString();
    }
}
