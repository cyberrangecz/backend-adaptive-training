package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.DecisionMatrixRow;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.dto.PhaseLevelUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.PhaseLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        phaseLevel.setDecisionMatrix(prepareDefaultDecisionMatrix(trainingDefinitionId, phaseLevel));

        PhaseLevel persistedEntity = phaseLevelRepository.save(phaseLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public PhaseLevelDto updatePhaseLevel(Long definitionId, Long phaseId, PhaseLevelUpdateDto trainingPhaseUpdate) {
        PhaseLevel phaseLevel = BeanMapper.INSTANCE.toEntity(trainingPhaseUpdate);
        phaseLevel.setId(phaseId);

        PhaseLevel persistedPhaseLevel = phaseLevelRepository.findById(phaseLevel.getId())
                .orElseThrow(() -> new RuntimeException("Training phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed);

        phaseLevel.setTrainingDefinitionId(persistedPhaseLevel.getTrainingDefinitionId());
        phaseLevel.setOrder(persistedPhaseLevel.getOrder());
        phaseLevel.setSubLevels(persistedPhaseLevel.getSubLevels());

        if (!CollectionUtils.isEmpty(phaseLevel.getDecisionMatrix())) {
            phaseLevel.getDecisionMatrix().forEach(x -> x.setPhaseLevel(phaseLevel));
        }

        PhaseLevel savedEntity = phaseLevelRepository.save(phaseLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void alignDecisionMatrixForPhasesInTrainingDefinition(Long trainingDefinitionId) {
        List<PhaseLevel> phaseLevels = phaseLevelRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        int currentPhaseOrder = 0;
        for (PhaseLevel phaseLevel : phaseLevels) {
            alignDecisionMatrixForPhase(phaseLevel, currentPhaseOrder);
            currentPhaseOrder++;
        }
    }

    private List<DecisionMatrixRow> prepareDefaultDecisionMatrix(Long trainingDefinitionId, PhaseLevel phaseLevel) {
        List<DecisionMatrixRow> result = new ArrayList<>();

        int numberOfExistingPhases = phaseLevelRepository.getNumberOfExistingPhases(trainingDefinitionId);

        // number of rows should be equal to number of existing phases + 1
        for (int i = 0; i <= numberOfExistingPhases; i++) {
            DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
            decisionMatrixRow.setPhaseLevel(phaseLevel);
            decisionMatrixRow.setOrder(i);

            result.add(decisionMatrixRow);
        }

        return result;
    }

    private void alignDecisionMatrixForPhase(PhaseLevel phaseLevel, int currentPhaseOrder) {
        if (Objects.isNull(phaseLevel)) {
            return;
        }

        int numberOfRows = 0;
        if (!CollectionUtils.isEmpty(phaseLevel.getDecisionMatrix())) {
            numberOfRows = phaseLevel.getDecisionMatrix().size();
        }

        final int expectedNumberOfRows = currentPhaseOrder + 1;

        if (numberOfRows == expectedNumberOfRows) {
            return;
        } else if (numberOfRows < expectedNumberOfRows) {
            List<DecisionMatrixRow> newDecisionMatrixRows = getNewDecisionMatrixRows(numberOfRows, expectedNumberOfRows, phaseLevel);
            phaseLevel.getDecisionMatrix().addAll(newDecisionMatrixRows);
        } else {
            List<DecisionMatrixRow> neededDecisionMatrixRows = getOnlyNeededDecisionMatrixRows(expectedNumberOfRows, phaseLevel);

            phaseLevel.getDecisionMatrix().clear();
            phaseLevel.getDecisionMatrix().addAll(neededDecisionMatrixRows);
        }

        phaseLevelRepository.save(phaseLevel);
    }

    private List<DecisionMatrixRow> getNewDecisionMatrixRows(int currentNumberOfNewRows, int expectedNumberOfRows,  PhaseLevel phaseLevel) {
        List<DecisionMatrixRow> result = new ArrayList<>();
        for (int i = currentNumberOfNewRows; i < expectedNumberOfRows; i++) {
            DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
            decisionMatrixRow.setPhaseLevel(phaseLevel);
            decisionMatrixRow.setOrder(i);

            result.add(decisionMatrixRow);
        }

        return result;
    }

    private List<DecisionMatrixRow> getOnlyNeededDecisionMatrixRows(int expectedNumberOfRows, PhaseLevel phaseLevel) {
        List<DecisionMatrixRow> decisionMatrix = phaseLevel.getDecisionMatrix();

        return decisionMatrix.stream()
                .sorted(Comparator.comparingInt(DecisionMatrixRow::getOrder))
                .limit(expectedNumberOfRows)
                .collect(Collectors.toList());

    }
}
