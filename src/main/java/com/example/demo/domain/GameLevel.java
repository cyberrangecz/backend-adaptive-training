package com.example.demo.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NodeEntity
public class GameLevel extends BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
    private String solutionPenalized;
    private String flag;
    private String solution;
    private String attachments;
    private String incorrectFlagLimit;

    @Relationship(type = "GAME_LEVEL_HINTS", direction = Relationship.UNDIRECTED)
    private List<Hint> hints;

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

    public String getSolutionPenalized() {
        return solutionPenalized;
    }

    public void setSolutionPenalized(String solutionPenalized) {
        this.solutionPenalized = solutionPenalized;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(String incorrectFlagLimit) {
        this.incorrectFlagLimit = incorrectFlagLimit;
    }

    public List<Hint> getHints() {
        if (Objects.isNull(hints)) {
            hints = new ArrayList<>();
        }
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    @Override
    public String toString() {
        return "GameLevel{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", solutionPenalized='" + solutionPenalized + '\'' +
            ", flag='" + flag + '\'' +
            ", solution='" + solution + '\'' +
            ", attachments='" + attachments + '\'' +
            ", incorrectFlagLimit='" + incorrectFlagLimit + '\'' +
            ", hints=" + hints +
            "} " + super.toString();
    }
}
