package com.example.demo.repository;

import com.example.demo.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT COALESCE(MAX(g.order), -1) FROM Task g WHERE g.phaseLevel.id = :phaseId")
    Integer getCurrentMaxOrder(@Param("phaseId") Long phaseId);
}
