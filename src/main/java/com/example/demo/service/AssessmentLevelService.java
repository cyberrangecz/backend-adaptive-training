package com.example.demo.service;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.AssessmentLevelDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AssessmentLevelRepository;
import com.example.demo.repository.BaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentLevelService {

    @Autowired
    private AssessmentLevelRepository assessmentLevelRepository;

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    public AssessmentLevelDto createDefaultAssessmentLevel(Long trainingDefinitionId) {

        AssessmentLevel assessmentLevel = new AssessmentLevel();
        assessmentLevel.setTitle("Title of assessment level");
        assessmentLevel.setOrderInTrainingDefinition(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        AssessmentLevel persistedEntity = assessmentLevelRepository.save(assessmentLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }
}
