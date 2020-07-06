package com.example.demo.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class AssessmentLevel extends BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String assessmentType;


}
