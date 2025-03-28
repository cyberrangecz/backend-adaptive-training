package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionsPhaseRelationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsPhaseRelationResultRepository extends JpaRepository<QuestionsPhaseRelationResult, Long>, QuerydslPredicateExecutor<QuestionsPhaseRelationResult> {

    List<QuestionsPhaseRelationResult> findByTrainingRunId(Long trainingRunId);

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);
}
