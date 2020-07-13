package com.example.demo.dto;

import java.io.Serializable;

public class AssessmentLevelDto extends BaseLevelDto implements Serializable {

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
        return "AssessmentLevelDto{" +
            "id=" + id +
            ", assessmentType='" + assessmentType + '\'' +
            "} " + super.toString();
    }
}
