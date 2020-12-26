package com.example.demo.repository;

import com.example.demo.domain.BaseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BaseLevelRepository  extends JpaRepository<BaseLevel, Long> {

    @Query("SELECT COALESCE(MAX(l.orderInTrainingDefinition), -1) FROM BaseLevel l WHERE l.trainingDefinitionId = :trainingDefinitionId")
    Integer getCurrentMaxOrder(@Param("trainingDefinitionId") Long trainingDefinitionId);
}
