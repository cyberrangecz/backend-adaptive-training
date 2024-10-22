package cz.cyberrange.platform.training.adaptive.persistence.repository.phases;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionPhaseRelationRepository extends JpaRepository<QuestionPhaseRelation, Long>, QuerydslPredicateExecutor<QuestionPhaseRelation> {

    @Query("SELECT DISTINCT r FROM QuestionPhaseRelation r INNER JOIN r.questions q WHERE q.id IN :questionIdList")
    List<QuestionPhaseRelation> findAllByQuestionIdList(@Param("questionIdList") Set<Long> questionIdList);
}
