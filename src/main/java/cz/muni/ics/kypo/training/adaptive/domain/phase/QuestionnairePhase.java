package cz.muni.ics.kypo.training.adaptive.domain.phase;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questionnaire_phase")
public class QuestionnairePhase extends AbstractPhase {

    @Enumerated(EnumType.STRING)
    @Column(name = "questionnaire_type")
    private QuestionnaireType questionnaireType;
    @OrderBy
    @OneToMany(
            mappedBy = "questionnairePhase",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Question> questions = new ArrayList<>();
    @OrderBy
    @OneToMany(
            mappedBy = "questionnairePhase",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<QuestionPhaseRelation> questionPhaseRelations = new ArrayList<>();

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public QuestionnaireType getQuestionnaireType() {
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public List<QuestionPhaseRelation> getQuestionPhaseRelations() {
        return questionPhaseRelations;
    }

    public void setQuestionPhaseRelations(List<QuestionPhaseRelation> questionPhaseRelations) {
        this.questionPhaseRelations = questionPhaseRelations;
    }

    public void addQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation) {
        if (this.questionPhaseRelations == null) {
            this.questionPhaseRelations = new ArrayList<>();
        }
        this.questionPhaseRelations.add(questionPhaseRelation);
    }


    @Override
    public String toString() {
        return "QuestionnairePhase{" +
                "questionnaireType=" + this.getQuestionnaireType() +
                ", title='" + super.getTitle() + '\'' +
                ", order=" + super.getOrder() +
                ", id=" + super.getId() +
                '}';
    }
}
