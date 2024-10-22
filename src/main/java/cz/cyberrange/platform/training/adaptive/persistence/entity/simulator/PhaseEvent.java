package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PhaseEvent {
    @ApiModelProperty(value = "Identifier of events associated with trainee in phase.", required = true, example = "2")
    @NotNull(message = "{phaseEvent.trainingRunId.NotNull.message}")
    private Long trainingRunId;
    private Long count;
    private String phaseType;
    private Long taskId;
    @ApiModelProperty(value = "Identifier of phase.", required = true, example = "2")
    @NotNull(message = "{phaseEvent.phaseId.NotNull.message}")
    private Long phaseId;
    private String phaseTitle;
    private SyslogData syslog;
    private Long poolId;
    @ApiModelProperty(value = "Type of an event.", required = true, example = "cz.cyberrange.platform.events.adaptive.trainings.WrongAnswerSubmitted")
    @NotEmpty(message = "{phaseEvent.type.NotNull.message}")
    private String type;
    private Long taskOrder;
    @JsonAlias({"game_time", "training_time"})
    private Long trainingTime;
    @ApiModelProperty(value = "Identifier of phase order.", required = true, example = "2")
    @NotNull(message = "{phaseEvent.phaseOrder.NotNull.message}")
    private Long phaseOrder;
    @ApiModelProperty(value = "Identifier of sandbox associated with trainee in phase.", required = true, example = "2")
    @NotNull(message = "{phaseEvent.sandboxId.NotNull.message}")
    private String sandboxId;
    private Long trainingInstanceId;
    private Long userRefId;
    private Long trainingDefinitionId;
    @ApiModelProperty(value = "Timestamp of event.", required = true, example = "151651561")
    @NotNull(message = "{phaseEvent.timestamp.NotNull.message}")
    private Long timestamp;
    private String passkeyContent;
    private String answerContent;
    private String answers;
    private Long endTime;
    private Long startTime;
}
