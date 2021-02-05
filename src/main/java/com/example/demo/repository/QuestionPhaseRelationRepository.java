package com.example.demo.repository;

import com.example.demo.domain.QuestionPhaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionPhaseRelationRepository extends JpaRepository<QuestionPhaseRelation, Long> {

    @Query("SELECT COALESCE(MAX(q.order), -1) FROM QuestionPhaseRelation q WHERE q.questionnairePhase.id = :phaseId")
    Integer getCurrentMaxOrder(@Param("phaseId") Long phaseId);
}
