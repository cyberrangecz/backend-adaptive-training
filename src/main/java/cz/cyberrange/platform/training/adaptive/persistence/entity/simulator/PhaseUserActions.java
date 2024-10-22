package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cz.cyberrange.platform.training.adaptive.utils.converter.LocalDateTimeUTCDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
