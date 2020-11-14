package com.example.demo.domain;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Hint {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private Long hintPenalty;
    private Long orderInLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameLevel gameLevel;

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

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
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
