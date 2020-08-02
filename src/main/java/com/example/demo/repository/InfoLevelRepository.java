package com.example.demo.repository;

import com.example.demo.domain.InfoLevel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface InfoLevelRepository extends Neo4jRepository<InfoLevel, Long> {
}
