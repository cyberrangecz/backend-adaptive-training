package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PhaseUserActions {
    private String hostname;
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime timestampStr;
    private String ip;
    @ApiModelProperty(value = "Identifier of sandbox associated with trainee.", required = true, example = "2")
    @NotNull(message = "{phaseUserActions.sandboxId.NotNull.message}")
    private String sandboxId;
    private String cmd;
    private String poolId;
    private String wd;
    private String cmdType;
    private String username;
}
