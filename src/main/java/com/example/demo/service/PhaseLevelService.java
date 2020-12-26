package com.example.demo.service;

import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.PhaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhaseLevelService {

    @Autowired
    private PhaseLevelRepository phaseLevelRepository;

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    public PhaseLevelDto createDefaultPhaseLevel(Long trainingDefinitionId) {

        PhaseLevel phaseLevel = new PhaseLevel();
        phaseLevel.setTitle("Title of assessment level");
        phaseLevel.setOrderInTrainingDefinition(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        PhaseLevel persistedEntity = phaseLevelRepository.save(phaseLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }
}
