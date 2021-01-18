package com.example.demo.repository;

import com.example.demo.domain.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {

    @Query("SELECT COALESCE(MAX(q.order), -1) FROM QuestionChoice q WHERE q.question.id = :questionId")
    Integer getCurrentMaxOrder(@Param("questionId") Long questionId);
}
