package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PhaseService {

    public static final String PHASE_NOT_FOUND = "Phase not found.";

    private final AbstractPhaseRepository abstractPhaseRepository;
    private final TrainingPhaseService trainingPhaseService;
    private final TrainingDefinitionRepository trainingDefinitionRepository;

    @Autowired
    public PhaseService(AbstractPhaseRepository abstractPhaseRepository,
                        TrainingPhaseService trainingPhaseService,
                        TrainingDefinitionRepository trainingDefinitionRepository) {
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.trainingPhaseService = trainingPhaseService;
        this.trainingDefinitionRepository = trainingDefinitionRepository;
    }

    private TrainingDefinition findDefinitionById(Long id) {
        return trainingDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingDefinition.class, "id", Long.class, id)));
    }

    /**
     * Deletes specific phase based on id
     *
     * @param phaseId - id of phase to be deleted
     * @return ID of the training definition from which the phase has been deleted.
     * @throws EntityNotFoundException training definition or phase is not found.
     * @throws EntityConflictException phase cannot be deleted in released or archived training definition.
     */
    public Long deletePhase(Long phaseId) {
        AbstractPhase phaseToDelete = this.getPhase(phaseId);
        TrainingDefinition trainingDefinition = phaseToDelete.getTrainingDefinition();
        if (!trainingDefinition.getState().equals(TDState.UNRELEASED)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", trainingDefinition.getId().getClass(), trainingDefinition.getId(), TrainingDefinitionService.ARCHIVED_OR_RELEASED));
        }

        abstractPhaseRepository.delete(phaseToDelete);
        int phaseOrder = phaseToDelete.getOrder();
        abstractPhaseRepository.decreaseOrderAfterPhaseWasDeleted(trainingDefinition.getId(), phaseOrder);
        trainingDefinition.setLastEdited(getCurrentTimeInUTC());
        return trainingDefinition.getId();
    }

    public AbstractPhase getPhase(Long phaseId) {
        return abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
    }

    public List<AbstractPhase> getPhases(Long trainingDefinitionId) {
        return abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);
    }

    /**
     * Move phase to the different position and modify orders of phase between moved phase and new position.
     *
     * @param phaseIdFrom - id of the phase to be moved to the new position
     * @param toOrder - position where phase will be moved
     * @throws EntityNotFoundException training definition or one of the phase is not found.
     * @throws EntityConflictException released or archived training definition cannot be modified.
     */
    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int toOrder) {
        AbstractPhase phaseFrom = abstractPhaseRepository.findById(phaseIdFrom)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseIdFrom.getClass(), phaseIdFrom, PHASE_NOT_FOUND)));
        toOrder = getCorrectToOrder(phaseFrom.getTrainingDefinition().getId(), toOrder);
        int fromOrder = phaseFrom.getOrder();
        int trainingPhaseFromOrder = 0;
        int trainingPhaseToOrder = 0;
        if (phaseFrom instanceof TrainingPhase) {
            List<AbstractPhase> abstractPhases = getPhases(phaseFrom.getTrainingDefinition().getId());
            trainingPhaseFromOrder = this.getTrainingPhaseOrder(fromOrder, abstractPhases, false);
            trainingPhaseToOrder = this.getTrainingPhaseOrder(toOrder, abstractPhases, fromOrder < toOrder);
        }

        if (fromOrder == toOrder) {
            return;
        } else if (fromOrder > toOrder) {
            abstractPhaseRepository.increaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinition().getId(), toOrder, fromOrder);
        } else {
            abstractPhaseRepository.decreaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinition().getId(), fromOrder, toOrder);
        }
        phaseFrom.setOrder(toOrder);
        trainingPhaseService.alignDecisionMatrixOfTrainingPhasesAfterMove(phaseFrom.getTrainingDefinition().getId(), trainingPhaseFromOrder, trainingPhaseToOrder);
        abstractPhaseRepository.save(phaseFrom);
    }

    private LocalDateTime getCurrentTimeInUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    private int getCorrectToOrder(Long trainingDefinitionId, int order) {
        Integer maxOrderOfPhase = abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId);
        if (order < 0) {
            order = 0;
        } else if (order > maxOrderOfPhase) {
            order = maxOrderOfPhase;
        }
        return order;
    }

    private int getTrainingPhaseOrder(int phaseOrder, List<AbstractPhase> abstractPhases, boolean lowerToUpper) {
        int trainingPhaseCounter = 0;
        for (AbstractPhase abstractPhase : abstractPhases) {
            if (abstractPhase.getOrder() == phaseOrder) {
                return !lowerToUpper || abstractPhase instanceof TrainingPhase ? trainingPhaseCounter : trainingPhaseCounter - 1;
            }
            if (abstractPhase instanceof TrainingPhase) {
                trainingPhaseCounter++;
            }
        }
        return -1;
    }
}
