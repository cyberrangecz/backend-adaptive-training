package com.example.demo.repository;

import com.example.demo.domain.AssessmentLevel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AssessmentLevelRepository  extends Neo4jRepository<AssessmentLevel, Long> {
}
