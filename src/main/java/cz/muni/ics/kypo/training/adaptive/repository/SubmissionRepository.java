package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long>, QuerydslPredicateExecutor<Submission> {

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);

    List<Submission> findByTrainingRunId(Long runId);

    List<Submission> findByTrainingRunIdAndPhaseId(Long runId, Long phaseId);
}
