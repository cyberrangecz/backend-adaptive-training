package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.TrainingPhaseQuestionsFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdaptiveQuestionsFulfillmentRepository extends JpaRepository<TrainingPhaseQuestionsFulfillment, Long>, QuerydslPredicateExecutor<TrainingPhaseQuestionsFulfillment> {

    List<TrainingPhaseQuestionsFulfillment> findByTrainingRunId(Long trainingRunId);
}
