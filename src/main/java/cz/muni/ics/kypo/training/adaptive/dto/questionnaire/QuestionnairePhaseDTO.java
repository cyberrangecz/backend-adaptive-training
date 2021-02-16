package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class QuestionnairePhaseDTO extends AbstractPhaseDTO {

    @ApiModelProperty(value = "List of questions associated with the questionnaire phase")
    private List<QuestionDTO> questions;

    @ApiModelProperty(value = "Type of questionnaire phase", allowableValues = "ADAPTIVE,GENERAL", example = "ADAPTIVE")
    private QuestionnaireType questionnaireType;

    @ApiModelProperty(value = "List of relations between questions and a training phase")
    private List<QuestionPhaseRelationDTO> phaseRelations;

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionPhaseRelationDTO> getPhaseRelations() {
        return phaseRelations;
    }

    public void setPhaseRelations(List<QuestionPhaseRelationDTO> phaseRelations) {
        this.phaseRelations = phaseRelations;
    }
}
