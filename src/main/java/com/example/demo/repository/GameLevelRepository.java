package com.example.demo.repository;

import com.example.demo.domain.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameLevelRepository extends JpaRepository<GameLevel, Long> {

    @Query("SELECT COALESCE(MAX(g.order), -1) FROM GameLevel g WHERE g.phaseLevel.id = :phaseId")
    Integer getCurrentMaxOrder(@Param("phaseId") Long phaseId);
}
