package cz.cyberrange.platform.training.adaptive.service.phases;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityConflictException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.AbstractPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.AccessPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingDefinitionRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingInstanceRepository;
import cz.cyberrange.platform.training.adaptive.startup.DefaultPhasesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cz.cyberrange.platform.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

@Service
public class AccessPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessPhaseService.class);

    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final AccessPhaseRepository accessPhaseRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;
    private final DefaultPhasesLoader defaultPhasesLoader;

    @Autowired
    public AccessPhaseService(TrainingDefinitionRepository trainingDefinitionRepository,
                              TrainingInstanceRepository trainingInstanceRepository,
                              AccessPhaseRepository accessPhaseRepository,
                              AbstractPhaseRepository abstractPhaseRepository,
                              DefaultPhasesLoader defaultPhasesLoader) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.accessPhaseRepository = accessPhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
        this.defaultPhasesLoader = defaultPhasesLoader;
    }

    public AccessPhase createAccessPhase(TrainingDefinition trainingDefinition) {
        checkIfCanBeUpdated(trainingDefinition);

        AccessPhase accessPhase = new AccessPhase();
        accessPhase.setCloudContent(defaultPhasesLoader.getDefaultAccessPhase().getCloudContent());
        accessPhase.setLocalContent(defaultPhasesLoader.getDefaultAccessPhase().getLocalContent());
        accessPhase.setPasskey(defaultPhasesLoader.getDefaultAccessPhase().getPasskey());
        accessPhase.setTitle(defaultPhasesLoader.getDefaultAccessPhase().getTitle());
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
