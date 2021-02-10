package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Set;

@Entity
public class QuestionPhaseRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_in_questionnaire", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionnairePhase questionnairePhase;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingPhase relatedTrainingPhase;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "question_phase_relation_question",
            joinColumns = @JoinColumn(name = "question_phase_relation_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions;

    private int successRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public QuestionnairePhase getQuestionnairePhase() {
        return questionnairePhase;
    }

    public void setQuestionnairePhase(QuestionnairePhase questionnairePhase) {
        this.questionnairePhase = questionnairePhase;
    }

    public TrainingPhase getRelatedTrainingPhase() {
        return relatedTrainingPhase;
    }

    public void setRelatedTrainingPhase(TrainingPhase relatedPhase) {
        this.relatedTrainingPhase = relatedPhase;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }
}
