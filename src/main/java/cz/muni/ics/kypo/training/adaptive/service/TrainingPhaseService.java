package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.DecisionMatrixRow;
import cz.muni.ics.kypo.training.adaptive.domain.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.mapper.BeanMapper;
import cz.muni.ics.kypo.training.adaptive.repository.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.TrainingPhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainingPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingPhaseService.class);

    @Autowired
    private TrainingPhaseRepository trainingPhaseRepository;

    @Autowired
    private AbstractPhaseRepository abstractPhaseRepository;

    public TrainingPhaseDTO createDefaultTrainingPhase(Long trainingDefinitionId) {

        TrainingPhase trainingPhase = new TrainingPhase();
        trainingPhase.setTitle("Title of training phase");
        trainingPhase.setTrainingDefinitionId(trainingDefinitionId);
        trainingPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        trainingPhase.setDecisionMatrix(prepareDefaultDecisionMatrix(trainingDefinitionId, trainingPhase));

        TrainingPhase persistedEntity = trainingPhaseRepository.save(trainingPhase);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TrainingPhaseDTO updateTrainingPhase(Long definitionId, Long phaseId, TrainingPhaseUpdateDTO trainingPhaseUpdate) {
        TrainingPhase trainingPhase = BeanMapper.INSTANCE.toEntity(trainingPhaseUpdate);
        trainingPhase.setId(phaseId);

        TrainingPhase persistedTrainingPhase = trainingPhaseRepository.findById(trainingPhase.getId())
                .orElseThrow(() -> new RuntimeException("Training phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed);

        trainingPhase.setTrainingDefinitionId(persistedTrainingPhase.getTrainingDefinitionId());
        trainingPhase.setOrder(persistedTrainingPhase.getOrder());
        trainingPhase.setTasks(persistedTrainingPhase.getTasks());

        if (!CollectionUtils.isEmpty(trainingPhase.getDecisionMatrix())) {
            trainingPhase.getDecisionMatrix().forEach(x -> x.setTrainingPhase(trainingPhase));
        }

        TrainingPhase savedEntity = trainingPhaseRepository.save(trainingPhase);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void alignDecisionMatrixForPhasesInTrainingDefinition(Long trainingDefinitionId) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        int currentPhaseOrder = 0;
        for (TrainingPhase trainingPhase : trainingPhases) {
            alignDecisionMatrixForPhase(trainingPhase, currentPhaseOrder);
            currentPhaseOrder++;
        }
    }

    private List<DecisionMatrixRow> prepareDefaultDecisionMatrix(Long trainingDefinitionId, TrainingPhase trainingPhase) {
        List<DecisionMatrixRow> result = new ArrayList<>();

        int numberOfExistingPhases = trainingPhaseRepository.getNumberOfExistingPhases(trainingDefinitionId);

        // number of rows should be equal to number of existing phases + 1
        for (int i = 0; i <= numberOfExistingPhases; i++) {
            DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
            decisionMatrixRow.setTrainingPhase(trainingPhase);
            decisionMatrixRow.setOrder(i);

            result.add(decisionMatrixRow);
        }

        return result;
    }

    private void alignDecisionMatrixForPhase(TrainingPhase trainingPhase, int currentPhaseOrder) {
        if (Objects.isNull(trainingPhase)) {
            return;
        }

        int numberOfRows = 0;
        if (!CollectionUtils.isEmpty(trainingPhase.getDecisionMatrix())) {
            numberOfRows = trainingPhase.getDecisionMatrix().size();
        }

        final int expectedNumberOfRows = currentPhaseOrder + 1;

        if (numberOfRows == expectedNumberOfRows) {
            return;
        } else if (numberOfRows < expectedNumberOfRows) {
            List<DecisionMatrixRow> newDecisionMatrixRows = getNewDecisionMatrixRows(numberOfRows, expectedNumberOfRows, trainingPhase);
            trainingPhase.getDecisionMatrix().addAll(newDecisionMatrixRows);
        } else {
            List<DecisionMatrixRow> neededDecisionMatrixRows = getOnlyNeededDecisionMatrixRows(expectedNumberOfRows, trainingPhase);

            trainingPhase.getDecisionMatrix().clear();
            trainingPhase.getDecisionMatrix().addAll(neededDecisionMatrixRows);
        }

        trainingPhaseRepository.save(trainingPhase);
    }

    private List<DecisionMatrixRow> getNewDecisionMatrixRows(int currentNumberOfNewRows, int expectedNumberOfRows,  TrainingPhase trainingPhase) {
        List<DecisionMatrixRow> result = new ArrayList<>();
        for (int i = currentNumberOfNewRows; i < expectedNumberOfRows; i++) {
            DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
            decisionMatrixRow.setTrainingPhase(trainingPhase);
            decisionMatrixRow.setOrder(i);

            result.add(decisionMatrixRow);
        }

        return result;
    }

    private List<DecisionMatrixRow> getOnlyNeededDecisionMatrixRows(int expectedNumberOfRows, TrainingPhase trainingPhase) {
        List<DecisionMatrixRow> decisionMatrix = trainingPhase.getDecisionMatrix();

        return decisionMatrix.stream()
                .sorted(Comparator.comparingInt(DecisionMatrixRow::getOrder))
                .limit(expectedNumberOfRows)
                .collect(Collectors.toList());

    }
}
