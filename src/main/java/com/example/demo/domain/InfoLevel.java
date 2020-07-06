package com.example.demo.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class InfoLevel extends BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
}
