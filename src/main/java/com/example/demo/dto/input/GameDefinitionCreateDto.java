package com.example.demo.dto.input;

import com.example.demo.dto.AttachmentDto;
import com.example.demo.enums.PhaseType;

import java.util.List;

// TODO make sure this class can be removed
public class GameDefinitionCreateDto {
    private Long id;
    private String title;
    private Integer order;
    private PhaseType type;

    // assessment level fields
    private String assessmentType;

    // game level fields
    private String content;
    private String flag;
    private String solution;
    private Long incorrectFlagLimit;

    private List<AttachmentDto> attachments;

    // info level fields
    // currently none special

    // unity level fields
    private List<GameDefinitionCreateDto> subLevels;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public PhaseType getType() {
        return type;
    }

    public void setType(PhaseType type) {
        this.type = type;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getIncorrectFlagLimit() {
        return incorrectFlagLimit;
    }

    public void setIncorrectFlagLimit(Long incorrectFlagLimit) {
        this.incorrectFlagLimit = incorrectFlagLimit;
    }

    public List<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDto> attachments) {
        this.attachments = attachments;
    }

    public List<GameDefinitionCreateDto> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<GameDefinitionCreateDto> subLevels) {
        this.subLevels = subLevels;
    }
}
