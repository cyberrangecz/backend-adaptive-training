package com.example.demo.repository;

import com.example.demo.domain.PhaseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhaseLevelRepository extends JpaRepository<PhaseLevel, Long> {

    @Query("SELECT COUNT(p.id) FROM PhaseLevel p WHERE p.trainingDefinitionId = :trainingDefinitionId")
    int getNumberOfExistingPhases(@Param("trainingDefinitionId") Long trainingDefinitionId);
}
