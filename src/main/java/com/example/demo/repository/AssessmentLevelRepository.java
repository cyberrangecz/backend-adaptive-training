package com.example.demo.repository;

import com.example.demo.domain.AssessmentLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentLevelRepository  extends JpaRepository<AssessmentLevel, Long> {
}
