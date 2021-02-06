package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {

    @Query("SELECT COALESCE(MAX(q.order), -1) FROM QuestionChoice q WHERE q.question.id = :questionId")
    Integer getCurrentMaxOrder(@Param("questionId") Long questionId);

    @Modifying
    @Query("UPDATE QuestionChoice q SET q.order = q.order - 1 " +
            "WHERE q.question.id  = :questionId " +
            "AND q.order > :order ")
    void decreaseOrderAfterQuestionChoiceWasDeleted(@Param("order") int order, @Param("questionId") Long questionId);

}
