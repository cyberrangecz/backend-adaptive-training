package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionPhaseRelationRepository extends JpaRepository<QuestionPhaseRelation, Long> {

    @Query("SELECT r FROM QuestionPhaseRelation r INNER JOIN r.questions q WHERE q.id IN :questionIdList")
    List<QuestionPhaseRelation> findAllByQuestionIdList(@Param("questionIdList") Set<Long> questionIdList);
}
