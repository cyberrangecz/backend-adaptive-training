package com.example.demo.repository;

import com.example.demo.domain.BaseLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaseLevelRepository extends JpaRepository<BaseLevel, Long> {

    List<BaseLevel> findAllByTrainingDefinitionIdOrderByOrder(long trainingDefinitionId);

    @Query("SELECT COALESCE(MAX(l.order), -1) FROM BaseLevel l WHERE l.trainingDefinitionId = :trainingDefinitionId")
    Integer getCurrentMaxOrder(@Param("trainingDefinitionId") Long trainingDefinitionId);

    @Modifying
    @Query("UPDATE BaseLevel l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order > :order ")
    void decreaseOrderAfterLevelWasDeleted(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                           @Param("order") int order);

    @Modifying
    @Query("UPDATE BaseLevel l SET l.order = l.order + 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order >= :lowerBound " +
            "AND l.order < :upperBound ")
    void increaseOrderOfLevelsOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

    @Modifying
    @Query("UPDATE BaseLevel l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order > :lowerBound " +
            "AND l.order <= :upperBound ")
    void decreaseOrderOfLevelsOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

}
