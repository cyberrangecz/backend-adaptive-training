package com.example.demo.service;

import com.example.demo.domain.DecisionMatrixRow;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.PhaseLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Optional;

@Service
public class PhaseLevelService {

    private static final Logger LOG = LoggerFactory.getLogger(PhaseLevelService.class);

    @Autowired
    private PhaseLevelRepository phaseLevelRepository;

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    public PhaseLevelDto createDefaultPhaseLevel(Long trainingDefinitionId) {

        PhaseLevel phaseLevel = new PhaseLevel();
        phaseLevel.setTitle("Title of phase level");
        phaseLevel.setTrainingDefinitionId(trainingDefinitionId);
        phaseLevel.setOrder(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        DecisionMatrixRow matrixRow = new DecisionMatrixRow();
        matrixRow.setAA(0.8);
        matrixRow.setCiT(0.7);
        matrixRow.setKU(0.6);
        matrixRow.setSD(0.5);
        matrixRow.setWF(0.4);
        matrixRow.setOrder(0);
        matrixRow.setPhaseLevel(phaseLevel);

        phaseLevel.setDecisionMatrix(Collections.singletonList(matrixRow));

        PhaseLevel persistedEntity = phaseLevelRepository.save(phaseLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public PhaseLevelDto updatePhaseLevel(PhaseLevel phaseLevel) {
        Optional<PhaseLevel> persistedPhaseLevel = phaseLevelRepository.findById(phaseLevel.getId());

        if (persistedPhaseLevel.isEmpty()) {
            // TODO return 404
            LOG.error("No phase level found with ID {}.", phaseLevel.getId());
            return new PhaseLevelDto();
        }

        phaseLevel.setTrainingDefinitionId(persistedPhaseLevel.get().getTrainingDefinitionId());
        phaseLevel.setOrder(persistedPhaseLevel.get().getOrder());
        phaseLevel.setSubLevels(persistedPhaseLevel.get().getSubLevels());

        if (!CollectionUtils.isEmpty(phaseLevel.getDecisionMatrix())) {
            phaseLevel.getDecisionMatrix().forEach(x -> x.setPhaseLevel(phaseLevel));
        }

        PhaseLevel savedEntity = phaseLevelRepository.save(phaseLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
