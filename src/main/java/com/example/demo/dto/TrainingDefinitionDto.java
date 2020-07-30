package com.example.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public class TrainingDefinitionDto implements Serializable {

    private Long id;
    private String title;
    private LocalDateTime lastEdited;
    private Long sandboxDefinitionRefId;
    private boolean showStepperBar;
    private String state;
    private String description;
    private Long estimatedDuration;
    private byte[] outcomes;
    private byte[] prerequisites;

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

    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(LocalDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Long getSandboxDefinitionRefId() {
        return sandboxDefinitionRefId;
    }

    public void setSandboxDefinitionRefId(Long sandboxDefinitionRefId) {
        this.sandboxDefinitionRefId = sandboxDefinitionRefId;
    }

    public boolean isShowStepperBar() {
        return showStepperBar;
    }

    public void setShowStepperBar(boolean showStepperBar) {
        this.showStepperBar = showStepperBar;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Long estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public byte[] getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(byte[] outcomes) {
        this.outcomes = outcomes;
    }

    public byte[] getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(byte[] prerequisites) {
        this.prerequisites = prerequisites;
    }

    @Override
    public String toString() {
        return "TrainingDefinitionDto{" + "id=" + id + ", title='" + title + '\'' + ", lastEdited=" + lastEdited +
               ", sandboxDefinitionRefId=" + sandboxDefinitionRefId + ", showStepperBar=" + showStepperBar +
               ", state='" + state + '\'' + ", description='" + description + '\'' + ", estimatedDuration=" +
               estimatedDuration + ", outcomes=" + Arrays.toString(outcomes) + ", prerequisites=" +
               Arrays.toString(prerequisites) + '}';
    }
}
