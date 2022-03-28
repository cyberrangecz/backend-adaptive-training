package cz.muni.ics.kypo.training.adaptive.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCSerializer;
import cz.muni.ics.kypo.training.adaptive.enums.SubmissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "SubmissionDTO")
public class SubmissionDTO {

    @ApiModelProperty(value = "ID of a the submission", example = "1")
    private Long id;
    @ApiModelProperty(value = "Content of the provided answer.", example = "true")
    private String provided;
    @ApiModelProperty(value = "Type of the submission, either correct or incorrect", example = "CORRECT")
    private SubmissionType submissionType;
    @ApiModelProperty(value = "Date time when answer was submitted.", example = "2016-10-19 10:23:54+02")
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    private LocalDateTime date;
    @ApiModelProperty(value = "IP address of the trainee who submitted an answer.", example = "2016-10-19 10:23:54+02")
    private String ipAddress;
    @ApiModelProperty(value = "ID of training run.", example = "2016-10-19 10:23:54+02")
    private Long trainingRunId;
    @ApiModelProperty(value = "ID of phase.", example = "2016-10-19 10:23:54+02")
    private Long phaseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvided() {
        return provided;
    }

    public void setProvided(String provided) {
        this.provided = provided;
    }

    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    @Override
    public String toString() {
        return "SubmissionDTO{" +
                "id=" + id +
                ", provided='" + provided + '\'' +
                ", submissionType=" + submissionType +
                ", date=" + date +
                ", ipAddress='" + ipAddress + '\'' +
                ", trainingRunId=" + trainingRunId +
                ", phaseId=" + phaseId +
                '}';
    }
}
