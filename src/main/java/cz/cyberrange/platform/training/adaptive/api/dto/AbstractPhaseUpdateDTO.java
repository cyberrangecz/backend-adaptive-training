package cz.cyberrange.platform.training.adaptive.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cyberrange.platform.training.adaptive.api.dto.access.AccessPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.info.InfoPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel(
        value = "AbstractPhaseUpdateDTO",
        subTypes = {TrainingPhaseUpdateDTO.class, AccessPhaseUpdateDTO.class, InfoPhaseUpdateDTO.class, QuestionnaireUpdateDTO.class},
        description = "Abstract superclass for classes TrainingPhaseUpdateDTO, AccessPhaseUpdateDTO, InfoPhaseUpdateDTO and QuestionnaireUpdateDTO"
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "phase_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingPhaseUpdateDTO.class, name = "TRAINING"),
        @JsonSubTypes.Type(value = AccessPhaseUpdateDTO.class, name = "ACCESS"),
        @JsonSubTypes.Type(value = QuestionnaireUpdateDTO.class, name = "QUESTIONNAIRE"),
        @JsonSubTypes.Type(value = InfoPhaseUpdateDTO.class, name = "INFO")})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractPhaseUpdateDTO {

    @ApiModelProperty(value = "ID of phase", required = true, example = "1")
    @NotNull(message = "{phase.id.NotNull.message}")
    private Long id;
    @ApiModelProperty(value = "Short description of training phase", required = true, example = "Training phase title")
    @NotEmpty(message = "{phase.title.NotEmpty.message}")
    private String title;
    @ApiModelProperty(value = "Type of phase", required = true, allowableValues = "QUESTIONNAIRE,INFO,TRAINING", example = "TRAINING")
    @NotNull
    private PhaseType phaseType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPhaseUpdateDTO that = (AbstractPhaseUpdateDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(), that.getTitle()) && getPhaseType() == that.getPhaseType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getPhaseType());
    }


    @Override
    public String toString() {
        return "AbstractPhaseUpdateDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", phaseType=" + phaseType +
                '}';
    }
}
