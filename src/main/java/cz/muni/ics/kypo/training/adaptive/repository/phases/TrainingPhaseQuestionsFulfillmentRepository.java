package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.TrainingPhaseQuestionsFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingPhaseQuestionsFulfillmentRepository extends JpaRepository<TrainingPhaseQuestionsFulfillment, Long>,
        QuerydslPredicateExecutor<TrainingPhaseQuestionsFulfillment> {

    Optional<TrainingPhaseQuestionsFulfillment> findByTrainingPhaseIdAndTrainingRunId(@Param("trainingPhaseId") Long trainingPhaseId,
                                                                                      @Param("trainingRunId") Long trainingRunId);
}
