package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AccessPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AccessPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.InfoPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

@Service
public class AccessPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessPhaseService.class);

    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final AccessPhaseRepository accessPhaseRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;

    @Autowired
    public AccessPhaseService(TrainingDefinitionRepository trainingDefinitionRepository,
                              TrainingInstanceRepository trainingInstanceRepository,
                              AccessPhaseRepository accessPhaseRepository,
                              AbstractPhaseRepository abstractPhaseRepository) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.accessPhaseRepository = accessPhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
    }

    public AccessPhase createDefaultAccessPhase(TrainingDefinition trainingDefinition) {
        checkIfCanBeUpdated(trainingDefinition);

        AccessPhase accessPhase = new AccessPhase();
        accessPhase.setCloudContent("Cloud content of access phase");
        accessPhase.setLocalContent("Local content of access phase");
        accessPhase.setPasskey("start-training");
        accessPhase.setTitle("Title of access phase");
        accessPhase.setTrainingDefinition(trainingDefinition);
        accessPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinition.getId()) + 1);
        return accessPhaseRepository.save(accessPhase);
    }

    public AccessPhase updateAccessPhase(AccessPhase persistedAccessPhase, AccessPhase updatedAccessPhase) {
        updatedAccessPhase.setId(persistedAccessPhase.getId());
        updatedAccessPhase.setTrainingDefinition(persistedAccessPhase.getTrainingDefinition());
        updatedAccessPhase.setOrder(persistedAccessPhase.getOrder());
        return accessPhaseRepository.save(updatedAccessPhase);
    }

    public AccessPhase findAccessPhaseById(Long phaseId) {
        return accessPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, PHASE_NOT_FOUND)));
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
