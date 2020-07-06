package com.example.demo.repository;

import com.example.demo.domain.BaseLevel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BaseLevelRepository  extends Neo4jRepository<BaseLevel, Long> {
}
