package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ParticipantPerformance {
    @ApiModelProperty(value = "ID of trainee", example = "1")
    @NotNull(message = "{participantPerformance.traineeID.NotNull.message}")
    private Long traineeID; // user ref id? sandbox id?

    @ApiModelProperty(value = "List containing performance from each phase")
    private List<OverallPhaseStatistics> performance;
}
