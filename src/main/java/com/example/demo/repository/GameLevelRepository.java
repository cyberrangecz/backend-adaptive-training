package com.example.demo.repository;

import com.example.demo.domain.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameLevelRepository extends JpaRepository<GameLevel, Long> {
}
