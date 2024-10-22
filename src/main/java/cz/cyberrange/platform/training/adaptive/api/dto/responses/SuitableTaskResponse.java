package cz.cyberrange.platform.training.adaptive.api.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SuitableTaskResponseDto")
public class SuitableTaskResponse {

    @ApiModelProperty(value = "Returns the number representing the suitable task for a given participant", example = "1")
    @JsonProperty("suitable_task")
    private int suitableTask;

    public int getSuitableTask() {
        return suitableTask;
    }

    public void setSuitableTask(int suitableTask) {
        this.suitableTask = suitableTask;
    }

    @Override
    public String toString() {
        return "SuitableTaskResponseDto{" +
                "suitableTask=" + suitableTask +
                '}';
    }
}
