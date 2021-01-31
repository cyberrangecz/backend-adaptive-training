package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.BaseLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhaseService {

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    @Autowired
    private PhaseLevelService phaseLevelService;

    @Transactional
    public void deletePhase(Long definitionId, Long phaseId) {
        BaseLevel phase = baseLevelRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)


        int levelOrder = phase.getOrder();
        baseLevelRepository.decreaseOrderAfterLevelWasDeleted(definitionId, levelOrder);

        baseLevelRepository.delete(phase);
    }

    public BaseLevelDto getPhase(Long definitionId, Long phaseId) {
        BaseLevel phase = baseLevelRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        return BeanMapper.INSTANCE.toDto(phase);
    }


    public List<BaseLevelDto> getPhases(Long trainingDefinitionId) {
        List<BaseLevel> phases = baseLevelRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        return BeanMapper.INSTANCE.toDtoList(phases);
    }

    @Transactional
    public void moveLevelToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        BaseLevel levelFrom = baseLevelRepository.findById(phaseIdFrom)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated


        int fromOrder = levelFrom.getOrder();

        if (fromOrder < newPosition) {
            baseLevelRepository.decreaseOrderOfLevelsOnInterval(levelFrom.getTrainingDefinitionId(), fromOrder, newPosition);
        } else if (fromOrder > newPosition) {
            baseLevelRepository.increaseOrderOfLevelsOnInterval(levelFrom.getTrainingDefinitionId(), newPosition, fromOrder);
        } else {
            // nothing should be changed, no further actions needed
            return;
        }

        levelFrom.setOrder(newPosition);
        baseLevelRepository.save(levelFrom);

        phaseLevelService.alignDecisionMatrixForPhasesInTrainingDefinition(levelFrom.getTrainingDefinitionId());
    }

}
