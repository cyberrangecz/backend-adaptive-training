package cz.muni.ics.kypo.training.adaptive.dto.questionnaire;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;

import java.util.List;

public class QuestionnairePhaseDTO extends AbstractPhaseDTO {

    private List<QuestionDTO> questions;
    private QuestionnaireType questionnaireType;
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
