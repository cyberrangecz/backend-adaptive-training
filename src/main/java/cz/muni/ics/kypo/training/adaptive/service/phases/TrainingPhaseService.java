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

    public TrainingPhase createDefaultTrainingPhase(TrainingDefinition trainingDefinition) {
        checkIfCanBeUpdated(trainingDefinition);

        TrainingPhase trainingPhase = new TrainingPhase();
        trainingPhase.setTitle("Title of training phase");
        trainingPhase.setTrainingDefinition(trainingDefinition);
        trainingPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinition.getId()) + 1);
        trainingPhase.setDecisionMatrix(prepareDefaultDecisionMatrix(trainingDefinition.getId(), trainingPhase));
        trainingPhase.setAllowedWrongAnswers(10);
        trainingPhase.setAllowedCommands(10);
        trainingPhase.setEstimatedDuration(10);
        return trainingPhaseRepository.save(trainingPhase);
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

    public void alignDecisionMatrixOfTrainingPhasesAfterMove(Long trainingDefinitionId, int trainingPhaseFromOrder, int trainingPhaseToOrder) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);
        if(trainingPhaseToOrder > trainingPhaseFromOrder) {
            this.alignDecisionMatricesLowerToUpper(trainingPhaseFromOrder, trainingPhaseToOrder, trainingPhases);
        } else if (trainingPhaseToOrder < trainingPhaseFromOrder) {
            this.alignDecisionMatricesUpperToLower(trainingPhaseFromOrder, trainingPhaseToOrder, trainingPhases);
        }
    }

    private void alignDecisionMatricesLowerToUpper(int trainingPhaseFromOrder, int trainingPhaseToOrder, List<TrainingPhase> trainingPhases) {
        int currentPhaseOrder = trainingPhaseFromOrder;
        for (TrainingPhase trainingPhase : trainingPhases.subList(trainingPhaseFromOrder, trainingPhaseToOrder + 1)) {
            if(currentPhaseOrder == trainingPhaseToOrder) {
                this.addMissingDecisionMatrixRowsAfterMove(trainingPhaseFromOrder, trainingPhaseToOrder, trainingPhase);
            } else {
                this.removeRedundantDecisionMatrixRows(trainingPhaseFromOrder, trainingPhaseFromOrder + 1, trainingPhase);
            }
            currentPhaseOrder++;
        }
        this.updateMatricesOfSubsequentTrainingPhasesLowerToUpper(trainingPhaseFromOrder, trainingPhaseToOrder, trainingPhases);
    }

    private void alignDecisionMatricesUpperToLower(int trainingPhaseFromOrder, int trainingPhaseToOrder, List<TrainingPhase> trainingPhases) {
        int currentPhaseOrder = trainingPhaseToOrder;

        for (TrainingPhase trainingPhase : trainingPhases.subList(trainingPhaseToOrder, trainingPhaseFromOrder + 1)) {
            if(currentPhaseOrder == trainingPhaseToOrder) {
                this.removeRedundantDecisionMatrixRows(trainingPhaseToOrder, trainingPhaseFromOrder, trainingPhase);
            } else {
                this.addMissingDecisionMatrixRowsAfterMove(trainingPhaseToOrder, trainingPhaseToOrder + 1, trainingPhase);
            }
            currentPhaseOrder++;
        }

        this.updateMatricesOfSubsequentTrainingPhasesUpperToLower(trainingPhaseFromOrder, trainingPhaseToOrder, trainingPhases);
    }

    private void updateMatricesOfSubsequentTrainingPhasesLowerToUpper(int fromOrder, int toOrder, List<TrainingPhase> trainingPhases) {
        for(int j = toOrder + 1; j < trainingPhases.size(); j++) {
            this.moveToIndex(fromOrder, toOrder, trainingPhases.get(j).getDecisionMatrix());
            this.decreaseRowOrdersByOne(fromOrder, toOrder, trainingPhases.get(j).getDecisionMatrix());
            trainingPhases.get(j).getDecisionMatrix().get(toOrder).incrementOrder(toOrder - fromOrder);
        }
    }

    private void updateMatricesOfSubsequentTrainingPhasesUpperToLower(int fromOrder, int toOrder, List<TrainingPhase> trainingPhases) {
        for(int j = fromOrder + 1; j < trainingPhases.size(); j++) {
            this.moveToIndex(fromOrder, toOrder, trainingPhases.get(j).getDecisionMatrix());
            this.increaseRowOrdersByOne(toOrder + 1, fromOrder + 1, trainingPhases.get(j).getDecisionMatrix());
            trainingPhases.get(j).getDecisionMatrix().get(toOrder).decrementOrder(fromOrder - toOrder);
        }
    }

    private <T> void moveToIndex(int from, int to, List<T> list) {
        T tmp = list.remove(from);
        list.add(to, tmp);
    }

    private void decreaseRowOrdersByOne(int from, int to, List<DecisionMatrixRow> decisionMatrixRows) {
        for(int k = from; k < to; k++) {
            decisionMatrixRows.get(k).decrementOrder(1);
        }
    }

    private void increaseRowOrdersByOne(int from, int to, List<DecisionMatrixRow> decisionMatrixRows) {
        for(int k = from; k < to; k++) {
            decisionMatrixRows.get(k).incrementOrder(1);
        }
    }

    private void removeRedundantDecisionMatrixRows(int from, int to, TrainingPhase trainingPhase) {
        List<DecisionMatrixRow> firstPartOfDecisionMatrix = new ArrayList<>(trainingPhase.getDecisionMatrix().subList(0, from));
        List<DecisionMatrixRow> secondPartOfDecisionMatrix = new ArrayList<>(trainingPhase.getDecisionMatrix().subList(to, trainingPhase.getDecisionMatrix().size()));
        secondPartOfDecisionMatrix.forEach(row -> row.setOrder(row.getOrder() - (to - from)));
        firstPartOfDecisionMatrix.addAll(secondPartOfDecisionMatrix);
        trainingPhase.getDecisionMatrix().clear();
        trainingPhase.getDecisionMatrix().addAll(firstPartOfDecisionMatrix);
    }

    private void addMissingDecisionMatrixRowsAfterMove(int fromOrder, int toOrder, TrainingPhase trainingPhase) {
        List<DecisionMatrixRow> missingMatrixRows = new ArrayList<>();
        for (int i = fromOrder; i < toOrder; i++) {
            DecisionMatrixRow decisionMatrixRow = new DecisionMatrixRow();
            decisionMatrixRow.setTrainingPhase(trainingPhase);
            decisionMatrixRow.setOrder(i);
            missingMatrixRows.add(decisionMatrixRow);
        }
        trainingPhase.getDecisionMatrix().addAll(fromOrder, missingMatrixRows);
        trainingPhase.getDecisionMatrix().subList(toOrder, trainingPhase.getDecisionMatrix().size()).forEach(row -> row.incrementOrder(toOrder - fromOrder));
    }

    public void alignDecisionMatrixAfterDelete(Long trainingDefinitionId) {
        List<TrainingPhase> trainingPhases = trainingPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        int currentPhaseOrder = 0;
        int deletedPhaseOrder = 0;
        for (TrainingPhase trainingPhase : trainingPhases) {
            if (trainingPhase.getDecisionMatrix().size() - 1 != currentPhaseOrder) {
                deletedPhaseOrder = currentPhaseOrder;
                break;
            }
            currentPhaseOrder++;
        }
        for (int i = currentPhaseOrder; i < trainingPhases.size(); i++) {
            trainingPhases.get(i).getDecisionMatrix().remove(deletedPhaseOrder);
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
}
