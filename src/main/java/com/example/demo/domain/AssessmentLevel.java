package com.example.demo.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class AssessmentLevel extends BaseLevel {

    @Id
    @GeneratedValue
    private Long id;

    private String assessmentType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    @Override
    public String toString() {
        return "AssessmentLevel{" +
            "id=" + id +
            ", assessmentType='" + assessmentType + '\'' +
            "} " + super.toString();
    }
}
