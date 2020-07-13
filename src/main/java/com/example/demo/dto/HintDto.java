package com.example.demo.dto;

import java.io.Serializable;

public class HintDto implements Serializable {

    private Long id;

    private String title;
    private String content;
    private String hintPenalty;
    private String orderInLevel;

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

    public String getHintPenalty() {
        return hintPenalty;
    }

    public void setHintPenalty(String hintPenalty) {
        this.hintPenalty = hintPenalty;
    }

    public String getOrderInLevel() {
        return orderInLevel;
    }

    public void setOrderInLevel(String orderInLevel) {
        this.orderInLevel = orderInLevel;
    }

    @Override
    public String toString() {
        return "HintDto{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", hintPenalty='" + hintPenalty + '\'' +
            ", orderInLevel='" + orderInLevel + '\'' +
            '}';
    }
}
