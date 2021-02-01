package com.example.demo.service;

import com.example.demo.domain.AbstractPhase;
import com.example.demo.dto.AbstractPhaseDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AbstractPhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhaseService {

    @Autowired
    private AbstractPhaseRepository abstractPhaseRepository;

    @Autowired
    private TrainingPhaseService trainingPhaseService;

    @Transactional
    public void deletePhase(Long definitionId, Long phaseId) {
        AbstractPhase phase = abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)


        int levelOrder = phase.getOrder();
        abstractPhaseRepository.decreaseOrderAfterLevelWasDeleted(definitionId, levelOrder);

        abstractPhaseRepository.delete(phase);
    }

    public AbstractPhaseDto getPhase(Long definitionId, Long phaseId) {
        AbstractPhase phase = abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        return BeanMapper.INSTANCE.toDto(phase);
    }


    public List<AbstractPhaseDto> getPhases(Long trainingDefinitionId) {
        List<AbstractPhase> phases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        return BeanMapper.INSTANCE.toDtoList(phases);
    }

    @Transactional
    public void moveLevelToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        AbstractPhase levelFrom = abstractPhaseRepository.findById(phaseIdFrom)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated


        int fromOrder = levelFrom.getOrder();

        if (fromOrder < newPosition) {
            abstractPhaseRepository.decreaseOrderOfLevelsOnInterval(levelFrom.getTrainingDefinitionId(), fromOrder, newPosition);
        } else if (fromOrder > newPosition) {
            abstractPhaseRepository.increaseOrderOfLevelsOnInterval(levelFrom.getTrainingDefinitionId(), newPosition, fromOrder);
        } else {
            // nothing should be changed, no further actions needed
            return;
        }

        levelFrom.setOrder(newPosition);
        abstractPhaseRepository.save(levelFrom);

        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(levelFrom.getTrainingDefinitionId());
    }

}
