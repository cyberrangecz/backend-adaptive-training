package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cz.muni.ics.kypo.training.adaptive.converter.LocalDateTimeUTCDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrainingInstanceInfo {
    @ApiModelProperty(value = "Main identifier of training instance.", required = true, example = "2")
    @NotNull(message = "{trainingInstance.id.NotNull.message}")
    private Long id;
    private Long definitionId;
    private String title;
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime startTime;
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime endTime;
    @ApiModelProperty(value = "AccessToken which will be modified and then used for accessing training run.", required = true, example = "hello-6578", position = 3)
    @NotEmpty(message = "{trainingInstance.accessToken.NotEmpty.message}")
    private String accessToken;
    private List<Long> organizersRefIds;
    private Boolean localEnvironment;
    private Boolean backwardMode;
}
