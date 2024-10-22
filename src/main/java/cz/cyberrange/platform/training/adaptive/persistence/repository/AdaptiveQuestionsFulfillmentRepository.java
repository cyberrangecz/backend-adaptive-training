package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.TrainingPhaseQuestionsFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdaptiveQuestionsFulfillmentRepository extends JpaRepository<TrainingPhaseQuestionsFulfillment, Long>, QuerydslPredicateExecutor<TrainingPhaseQuestionsFulfillment> {

    List<TrainingPhaseQuestionsFulfillment> findByTrainingRunId(Long trainingRunId);
}
