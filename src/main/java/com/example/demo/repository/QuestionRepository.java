package com.example.demo.repository;

import com.example.demo.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT COALESCE(MAX(q.order), -1) FROM Question q WHERE q.questionnaireLevel.id = :questionnaireId")
    Integer getCurrentMaxOrder(@Param("questionnaireId") Long questionnaireId);

    @Modifying
    @Query("UPDATE Question q SET q.order = q.order - 1 " +
            "WHERE q.questionnaireLevel.id  = :questionnaireLevelId " +
            "AND q.order > :order ")
    void decreaseOrderAfterQuestionWasDeleted(@Param("order") int order, @Param("questionnaireLevelId") Long questionnaireLevelId);
}
