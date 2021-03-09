package cz.muni.ics.kypo.training.adaptive.dto;

import cz.muni.ics.kypo.training.adaptive.dto.training.DecisionMatrixRowDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.DecisionMatrixRowForAssistantDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "AdaptiveSmartAssistantInput")
public class AdaptiveSmartAssistantInput {

    @ApiModelProperty(value = "The identifier of a given training run representing a given participant", example = "1")
    @NotNull(message = "Training run ID must be specified")
    private Long trainingRunId;
    @ApiModelProperty(value = "The id of a phase X.", example = "5")
    @NotNull(message = "Training phase ID must be specified")
    private Long phaseX;
    @ApiModelProperty(value = "The number of tasks in a phase X.", example = "3")
    @NotNull(message = "Number of tasks in specified training phase must be specified")
    @Min(value = 1, message = "At least one task must be available in training phase")
    private Integer phaseXTasks;
    @ApiModelProperty(value = "The list of phaseIds (the given phase including the given phases).", example = "[1,2,3,4,5]")
    private List<Long> phaseIds = new ArrayList<>();
    @ApiModelProperty(value = "The decision matrix with weights to compute the students' performance.")
    @NotEmpty(message = "Decision matrix must be set")
    private List<DecisionMatrixRowForAssistantDTO> decisionMatrix = new ArrayList<>();
    @ApiModelProperty(value = "The information if the questionnaire was correctly answered for a given phase.", example = "true")
    @NotNull(message = "It must be defined whether the questionnaire prerequisites were met")
    private Boolean questionnaireCorrectlyAnswered;

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public Long getPhaseX() {
        return phaseX;
    }

    public void setPhaseX(Long phaseX) {
        this.phaseX = phaseX;
    }

    public Integer getPhaseXTasks() {
        return phaseXTasks;
    }

    public void setPhaseXTasks(Integer phaseXTasks) {
        this.phaseXTasks = phaseXTasks;
    }

    public List<Long> getPhaseIds() {
        return phaseIds;
    }

    public void setPhaseIds(List<Long> phaseIds) {
        this.phaseIds = phaseIds;
    }

    public List<DecisionMatrixRowForAssistantDTO> getDecisionMatrix() {
        return decisionMatrix;
    }

    public void setDecisionMatrix(List<DecisionMatrixRowForAssistantDTO> decisionMatrix) {
        this.decisionMatrix = decisionMatrix;
    }

    public Boolean getQuestionnaireCorrectlyAnswered() {
        return questionnaireCorrectlyAnswered;
    }

    public void setQuestionnaireCorrectlyAnswered(Boolean questionnaireCorrectlyAnswered) {
        this.questionnaireCorrectlyAnswered = questionnaireCorrectlyAnswered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdaptiveSmartAssistantInput)) return false;
        AdaptiveSmartAssistantInput that = (AdaptiveSmartAssistantInput) o;
        return Objects.equals(trainingRunId, that.trainingRunId) &&
                Objects.equals(phaseX, that.phaseX) &&
                Objects.equals(phaseXTasks, that.phaseXTasks) &&
                Objects.equals(phaseIds, that.phaseIds) &&
                Objects.equals(decisionMatrix, that.decisionMatrix) &&
                Objects.equals(questionnaireCorrectlyAnswered, that.questionnaireCorrectlyAnswered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingRunId, phaseX, phaseXTasks, phaseIds, decisionMatrix, questionnaireCorrectlyAnswered);
    }

    @Override
    public String toString() {
        return "AdaptiveSmartAssistantInput{" +
                "trainingRunId=" + trainingRunId +
                ", phaseX=" + phaseX +
                ", phaseXTasks=" + phaseXTasks +
                ", phaseIds=" + phaseIds +
                ", decisionMatrix=" + decisionMatrix +
                ", questionnaireCorrectlyAnswered=" + questionnaireCorrectlyAnswered +
                '}';
    }
}
