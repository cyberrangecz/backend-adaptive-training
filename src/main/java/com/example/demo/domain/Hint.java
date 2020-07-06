package com.example.demo.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

@Data
public class Hint {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private String hintPenalty;
    private String orderInLevel;
}
