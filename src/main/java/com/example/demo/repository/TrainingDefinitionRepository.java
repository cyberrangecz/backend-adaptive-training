package com.example.demo.repository;

import com.example.demo.domain.TrainingDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingDefinitionRepository extends JpaRepository<TrainingDefinition, Long> {
}
