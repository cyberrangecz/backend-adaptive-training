package com.example.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AttachmentDto implements Serializable {

    private Long id;
    private String content;
    private LocalDateTime creationTime;

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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "AttachmentDto{" + "id=" + id + ", content='" + content + '\'' + ", creationTime=" + creationTime + '}';
    }
}
