package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseUpdateDto;
import cz.muni.ics.kypo.training.adaptive.mapper.BeanMapper;
import cz.muni.ics.kypo.training.adaptive.repository.AbstractPhaseRepository;
import cz.muni.ics.kypo.training.adaptive.repository.InfoPhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoPhaseService.class);

    private final InfoPhaseRepository infoPhaseRepository;
    private final AbstractPhaseRepository abstractPhaseRepository;

    @Autowired
    public InfoPhaseService(InfoPhaseRepository infoPhaseRepository, AbstractPhaseRepository abstractPhaseRepository) {
        this.infoPhaseRepository = infoPhaseRepository;
        this.abstractPhaseRepository = abstractPhaseRepository;
    }

    public InfoPhaseDto createDefaultInfoPhase(Long trainingDefinitionId) {
        InfoPhase infoPhase = new InfoPhase();
        infoPhase.setContent("Content of info level");
        infoPhase.setTitle("Title of info level");
        infoPhase.setTrainingDefinitionId(trainingDefinitionId);
        infoPhase.setOrder(abstractPhaseRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        InfoPhase persistedEntity = infoPhaseRepository.save(infoPhase);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public InfoPhaseDto updateInfoPhase(Long definitionId, Long phaseId, InfoPhaseUpdateDto infoPhaseUpdateDto) {
        InfoPhase infoPhaseUpdate = BeanMapper.INSTANCE.toEntity(infoPhaseUpdateDto);
        infoPhaseUpdate.setId(phaseId);

        InfoPhase persistedInfoPhase = infoPhaseRepository.findById(infoPhaseUpdate.getId())
                .orElseThrow(() -> new RuntimeException("Info phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        infoPhaseUpdate.setTrainingDefinitionId(persistedInfoPhase.getTrainingDefinitionId());
        infoPhaseUpdate.setOrder(persistedInfoPhase.getOrder());

        InfoPhase savedEntity = infoPhaseRepository.save(infoPhaseUpdate);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

}
