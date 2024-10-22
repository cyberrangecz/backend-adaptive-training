package cz.cyberrange.platform.training.adaptive.persistence.entity.phase;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
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
