package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.enums.TDState;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.InfoPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

import static cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService.PHASE_NOT_FOUND;
import static cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService.ARCHIVED_OR_RELEASED;

@Service
public class InfoPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoPhaseService.class);

    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final InfoPhaseRepository infoPhaseRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;

    @Autowired
    public InfoPhaseService(TrainingDefinitionRepository trainingDefinitionRepository,
                            TrainingInstanceRepository trainingInstanceRepository,
                            InfoPhaseRepository infoPhaseRepository,
                            AbstractPhaseRepository abstractPhaseRepository) {
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.infoPhaseRepository = infoPhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
    }

    public InfoPhase createDefaultInfoPhase(TrainingDefinition trainingDefinition) {
        checkIfCanBeUpdated(trainingDefinition);

        InfoPhase infoPhase = new InfoPhase();
        infoPhase.setContent("Content of info phase");
        infoPhase.setTitle("Title of info phase");
        infoPhase.setTrainingDefinition(trainingDefinition);
        infoPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinition.getId()) + 1);
        return infoPhaseRepository.save(infoPhase);
    }

    public InfoPhase updateInfoPhase(InfoPhase persistedInfoPhase, InfoPhase updatedInfoPhase) {
        updatedInfoPhase.setId(persistedInfoPhase.getId());
        updatedInfoPhase.setTrainingDefinition(persistedInfoPhase.getTrainingDefinition());
        updatedInfoPhase.setOrder(persistedInfoPhase.getOrder());
        return infoPhaseRepository.save(updatedInfoPhase);
    }

    public InfoPhase findInfoPhaseById(Long phaseId) {
        return infoPhaseRepository.findById(phaseId)
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
