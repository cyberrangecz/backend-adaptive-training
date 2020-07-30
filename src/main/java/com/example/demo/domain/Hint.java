package com.example.demo.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Hint {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private Long hintPenalty;
    private Long orderInLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getHintPenalty() {
        return hintPenalty;
    }

    public void setHintPenalty(Long hintPenalty) {
        this.hintPenalty = hintPenalty;
    }

    public Long getOrderInLevel() {
        return orderInLevel;
    }

    public void setOrderInLevel(Long orderInLevel) {
        this.orderInLevel = orderInLevel;
    }

    @Override
    public String toString() {
        return "Hint{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", hintPenalty='" + hintPenalty + '\'' +
            ", orderInLevel='" + orderInLevel + '\'' +
            '}';
    }
}
