package com.example.demo.repository;

import com.example.demo.domain.GameLevel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GameLevelRepository extends Neo4jRepository<GameLevel, Long> {
}
