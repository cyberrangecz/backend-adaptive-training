package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@ApiModel(
        value = "QuestionnaireUpdateDTO"
)
public class QuestionnaireUpdateDTO extends AbstractPhaseUpdateDTO {

    @Valid
    @ApiModelProperty(value = "Questions in the questionnaire", required = true)
    private List<QuestionDTO> questions = new ArrayList<>();
    @Valid
    @ApiModelProperty(value = "The relation between questions in the questionnaire and phase in the training definition", required = true)
    private List<QuestionPhaseRelationDTO> phaseRelations = new ArrayList<>();

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public List<QuestionPhaseRelationDTO> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationDTO> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionnaireUpdateDTO that = (QuestionnaireUpdateDTO) o;
        return Objects.equals(getQuestions(), that.getQuestions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getQuestions());
    }


    @Override
    public String toString() {
        return "QuestionnaireUpdateDTO{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", phaseType=" + getPhaseType() +
                ", questions=" + questions +
                '}';
    }
}
