package com.example.demo.repository;

import com.example.demo.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT COALESCE(MAX(g.order), -1) FROM Task g WHERE g.phaseLevel.id = :phaseId")
    Integer getCurrentMaxOrder(@Param("phaseId") Long phaseId);

    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.order = t.order - 1 " +
            "WHERE t.phaseLevel.id = :gamePhaseId " +
            "AND t.order > :order ")
    void decreaseOrderAfterTaskWasDeleted(@Param("gamePhaseId") Long gamePhaseId,
                                          @Param("order") int order);
}
