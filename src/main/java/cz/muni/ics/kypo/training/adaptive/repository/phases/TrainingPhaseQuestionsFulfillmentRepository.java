package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.TrainingPhaseQuestionsFulfillment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPhaseQuestionsFulfillmentRepository extends JpaRepository<TrainingPhaseQuestionsFulfillment, Long>,
        QuerydslPredicateExecutor<TrainingPhaseQuestionsFulfillment> {

    Optional<TrainingPhaseQuestionsFulfillment> findByTrainingPhaseIdAndTrainingRunId(@Param("trainingPhaseId") Long trainingPhaseId,
                                                                                      @Param("trainingRunId") Long trainingRunId);

    @Query("SELECT tpqf FROM TrainingPhaseQuestionsFulfillment tpqf " +
            "JOIN FETCH tpqf.trainingPhase tp " +
            "JOIN FETCH tpqf.trainingRun tr " +
            "WHERE tr.id = :trainingRunId " +
            "AND tp.id IN :trainingPhaseIds " +
            "ORDER BY tp.order")
    List<TrainingPhaseQuestionsFulfillment> findByTrainingPhasesAndTrainingRun(@Param("trainingPhaseIds") List<Long> trainingPhaseIds,
                                                                               @Param("trainingRunId") Long trainingRunId);

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);
}
