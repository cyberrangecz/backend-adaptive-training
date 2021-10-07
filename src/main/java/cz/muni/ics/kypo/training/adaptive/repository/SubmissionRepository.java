package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubmissionRepository extends JpaRepository<Submission, Long>, QuerydslPredicateExecutor<Submission> {

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);
}
