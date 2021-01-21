package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Attachment {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
    private LocalDateTime creationTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Task task;

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

//    public Task getTask() {
//        return task;
//    }

//    public void setTask(Task task) {
//        this.task = task;
//    }

    @Override
    public String toString() {
        return "Attachment{" + "id=" + id + ", content='" + content + '\'' + ", creationTime=" + creationTime + '}';
    }
}
