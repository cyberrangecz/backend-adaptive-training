package com.example.demo.domain;


import javax.persistence.Entity;

@Entity
public class AssessmentLevel extends BaseLevel {

    private String assessmentType;

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    @Override
    public String toString() {
        return "AssessmentLevel{" + ", assessmentType='" + assessmentType + '\'' + "} " + super.toString();
    }
}
