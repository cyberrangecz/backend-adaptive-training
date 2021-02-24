package cz.muni.ics.kypo.training.adaptive.domain.phase.questions;

import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "question_phase_relation")
public class QuestionPhaseRelation implements Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "questionPhaseGenerator"
    )
    @SequenceGenerator(
            name = "questionPhaseGenerator",
            sequenceName = "question_phase_seq"
    )
    @Column(
            name = "question_phase_relation_id",
            nullable = false,
            unique = true
    )
    private Long id;
    @Column(
            name = "order_in_questionnaire",
            nullable = false
    )
    private Integer order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_phase_id")
    private QuestionnairePhase questionnairePhase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_training_phase_id")
    private TrainingPhase relatedTrainingPhase;
    @ManyToMany(fetch = FetchType.LAZY,
                cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "question_phase_relation_question",
            joinColumns = @JoinColumn(name = "question_phase_relation_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private Set<Question> questions = new HashSet<>();

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
        for (Question question : questions) {
            question.addQuestionPhaseRelation(this);
        }
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }
}
