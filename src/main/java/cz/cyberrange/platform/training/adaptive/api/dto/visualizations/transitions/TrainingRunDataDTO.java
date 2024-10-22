package cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions;

import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * Encapsulates information about Training Run for transition graph.
 */
@ApiModel(
        value = "TrainingRunDataDTO",
        description = "Data for visualizations that displays the transitions between phases of the particular training run."
)
public class TrainingRunDataDTO {
    @ApiModelProperty(value = "Main identifier of training run.", example = "1")
    private Long trainingRunId;
    @ApiModelProperty(value = "Participant of the training run.")
    private UserRefDTO trainee;
    @ApiModelProperty(value = "List of the visited phases and tasks in case of training phase during the training run.")
    private List<TrainingRunPathNode> trainingRunPathNodes;
    @ApiModelProperty(value = "Indicates if local sandboxes are used for training runs.", example = "true")
    private boolean localEnvironment;

    public Long getTrainingRunId() {
        return trainingRunId;
    }

    public void setTrainingRunId(Long trainingRunId) {
        this.trainingRunId = trainingRunId;
    }

    public UserRefDTO getTrainee() {
        return trainee;
    }

    public void setTrainee(UserRefDTO trainee) {
        this.trainee = trainee;
    }

    public List<TrainingRunPathNode> getTrainingRunPathNodes() {
        return trainingRunPathNodes;
    }

    public void setTrainingRunPathNodes(List<TrainingRunPathNode> trainingRunPathNodes) {
        this.trainingRunPathNodes = trainingRunPathNodes;
    }

    public boolean isLocalEnvironment() {
        return localEnvironment;
    }

    public void setLocalEnvironment(boolean localEnvironment) {
        this.localEnvironment = localEnvironment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingRunDataDTO that = (TrainingRunDataDTO) o;
        return Objects.equals(getTrainingRunId(), that.getTrainingRunId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrainingRunId());
    }

    @Override
    public String toString() {
        return "TrainingRunDataDTO{" +
                "trainingRunId=" + trainingRunId +
                ", trainee=" + trainee +
                ", trainingRunPathNodes=" + trainingRunPathNodes +
                ", localEnvironment=" + localEnvironment +
                '}';
    }
}
