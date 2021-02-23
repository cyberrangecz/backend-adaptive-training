package cz.muni.ics.kypo.training.adaptive.dto.traininginstance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Encapsulates information about Training Instance, intended for assigning pool id.
 */
@ApiModel(value = "TrainingInstanceAssignPoolIdDTO", description = "Training Instance assign pool ID.")
public class TrainingInstanceAssignPoolIdDTO {

    @ApiModelProperty(value = "Pool associated with training instance.", example = "2", required = true)
    @NotNull(message = "{assignPool.poolId.NotNull.message}")
    @Min(value = 0, message = "{assignPool.poolId.Min.message}")
    private Long poolId;

    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingInstanceAssignPoolIdDTO)) return false;
        TrainingInstanceAssignPoolIdDTO that = (TrainingInstanceAssignPoolIdDTO) o;
        return Objects.equals(getPoolId(), that.getPoolId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPoolId());
    }

    @Override
    public String toString() {
        return "TrainingInstanceAssignPoolIdDTO{" +
                ", poolId=" + poolId +
                '}';
    }
}
