package com.example.demo.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class BaseLevel {
    private String title;
    private String estimatedDuration;
    private Long maxScore;
}
