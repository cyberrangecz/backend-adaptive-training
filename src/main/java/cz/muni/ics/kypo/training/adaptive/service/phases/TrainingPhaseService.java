package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.DecisionMatrixRow;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TrainingPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

@Service
@Transactional
public class TrainingPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingPhaseService.class);

    private final TrainingPhaseRepository trainingPhaseRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final TrainingDefinitionRepository trainingDefinitionRepository;

    @Autowired
    public TrainingPhaseService(TrainingDefinitionRepository trainingDefinitionRepository,
                                TrainingInstanceRepository trainingInstanceRepository,
                                TrainingPhaseRepository trainingPhaseRepository,
                                AbstractPhaseRepository abstractPhaseRepository) {
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
    }

    public TrainingPhase createDefaultTrainingPhase(Long trainingDefinitionId) {
        TrainingDefinition trainingDefinition = findDefinitionById(trainingDefinitionId);
        checkIfCanBeUpdated(trainingDefinition);

        TrainingPhase trainingPhase = new TrainingPhase();
        trainingPhase.setTitle("Title of training phase");
        trainingPhase.setTrainingDefinition(trainingDefinition);
        trainingPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);
        trainingPhase.setDecisionMatrix(prepareDefaultDecisionMatrix(trainingDefinitionId, trainingPhase));
        TrainingPhase persistedEntity = trainingPhaseRepository.save(trainingPhase);
        trainingDefinition.setLastEdited(getCurrentTimeInUTC());
        return persistedEntity;
    }


    /**
     * Updates training phase in training definition
     *
     * @param phaseId               - id of the phase to be updated
     * @param trainingPhaseToUpdate phase to be updated
     * @throws EntityNotFoundException training definition is not found.
     * @throws EntityConflictException phase cannot be updated in released or archived training definition.
     */
    public TrainingPhase updateTrainingPhase(Long phaseId, TrainingPhase trainingPhaseToUpdate) {
        TrainingPhase persistedTrainingPhase = findPhaseById(phaseId);
        TrainingDefinition trainingDefinition = persistedTrainingPhase.getTrainingDefinition();
        checkIfCanBeUpdated(trainingDefinition);
        trainingPhaseToUpdate.setId(phaseId);
        trainingPhaseToUpdate.setTrainingDefinition(persistedTrainingPhase.getTrainingDefinition());
        trainingPhaseToUpdate.setOrder(persistedTrainingPhase.getOrder());
        trainingPhaseToUpdate.setTasks(persistedTrainingPhase.getTasks());
        if (!CollectionUtils.isEmpty(trainingPhaseToUpdate.getDecisionMatrix())) {
            Set<Long> persistedMatrixRows = persistedTrainingPhase.getDecisionMatrix()
                    .stream()
                    .map(DecisionMatrixRow::getId)
                    .collect(Collectors.toSet());
            trainingPhaseToUpdate.getDecisionMatrix()
                    .stream()
                    .filter(matrixRow -> matrixRow.getId() == null || persistedMatrixRows.contains(matrixRow.getId()))
                    .forEach(matrixRow -> matrixRow.setTrainingPhase(trainingPhaseToUpdate));
        }
        return trainingPhaseRepository.save(trainingPhaseToUpdate);
    }

    public TrainingPhase findPhaseById(Long phaseId) {
        return trainingPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
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

        // number of rows should be equal to number of existing phase + 1
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

    private List<DecisionMatrixRow> getNewDecisionMatrixRows(int currentNumberOfNewRows, int expectedNumberOfRows, TrainingPhase trainingPhase) {
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

    private TrainingDefinition findDefinitionById(Long id) {
        return trainingDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", Long.class, id)));
    }

    private void checkIfCanBeUpdated(TrainingDefinition trainingDefinition) {
        if (!trainingDefinition.getState().equals(TDState.UNRELEASED)) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    ARCHIVED_OR_RELEASED));
        }
        if (trainingInstanceRepository.existsAnyForTrainingDefinition(trainingDefinition.getId())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingDefinition.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(),
                    "Cannot update training definition with already created training instance. " +
                            "Remove training instance/s before updating training definition."));
        }
    }

    private LocalDateTime getCurrentTimeInUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }
}
