package com.example.demo.repository;

import com.example.demo.domain.QuestionnaireLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionnaireLevelRepository extends JpaRepository<QuestionnaireLevel, Long> {
}
