package com.example.demo.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;


@NodeEntity
@Data
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
}
