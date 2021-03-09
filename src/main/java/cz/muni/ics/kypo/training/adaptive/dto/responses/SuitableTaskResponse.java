package cz.muni.ics.kypo.training.adaptive.dto.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SuitableTaskResponseDto")
public class SuitableTaskResponse {

    @ApiModelProperty(value = "Returns the number representing the suitable task for a given participant", example = "1")
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
