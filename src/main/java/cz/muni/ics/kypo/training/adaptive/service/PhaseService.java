package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.mapper.BeanMapper;
import cz.muni.ics.kypo.training.adaptive.repository.AbstractPhaseRepository;
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


        int phaseOrder = phase.getOrder();
        abstractPhaseRepository.decreaseOrderAfterPhaseWasDeleted(definitionId, phaseOrder);

        abstractPhaseRepository.delete(phase);
    }

    public AbstractPhaseDTO getPhase(Long definitionId, Long phaseId) {
        AbstractPhase phase = abstractPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        return BeanMapper.INSTANCE.toDto(phase);
    }


    public List<AbstractPhaseDTO> getPhases(Long trainingDefinitionId) {
        List<AbstractPhase> phases = abstractPhaseRepository.findAllByTrainingDefinitionIdOrderByOrder(trainingDefinitionId);

        return BeanMapper.INSTANCE.toDtoList(phases);
    }

    @Transactional
    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        AbstractPhase phaseFrom = abstractPhaseRepository.findById(phaseIdFrom)
                .orElseThrow(() -> new RuntimeException("Phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated


        int fromOrder = phaseFrom.getOrder();

        if (fromOrder < newPosition) {
            abstractPhaseRepository.decreaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinitionId(), fromOrder, newPosition);
        } else if (fromOrder > newPosition) {
            abstractPhaseRepository.increaseOrderOfPhasesOnInterval(phaseFrom.getTrainingDefinitionId(), newPosition, fromOrder);
        } else {
            // nothing should be changed, no further actions needed
            return;
        }

        phaseFrom.setOrder(newPosition);
        abstractPhaseRepository.save(phaseFrom);

        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(phaseFrom.getTrainingDefinitionId());
    }

}
