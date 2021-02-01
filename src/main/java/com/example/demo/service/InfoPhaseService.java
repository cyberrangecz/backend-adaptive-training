package com.example.demo.service;

import com.example.demo.domain.InfoPhase;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.InfoPhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoPhaseService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoPhaseService.class);

    private final InfoPhaseRepository infoPhaseRepository;
    private final BaseLevelRepository baseLevelRepository;

    @Autowired
    public InfoPhaseService(InfoPhaseRepository infoPhaseRepository, BaseLevelRepository baseLevelRepository) {
        this.infoPhaseRepository = infoPhaseRepository;
        this.baseLevelRepository = baseLevelRepository;
    }

    public InfoPhaseDto createDefaultInfoPhase(Long trainingDefinitionId) {
        InfoPhase infoPhase = new InfoPhase();
        infoPhase.setContent("Content of info level");
        infoPhase.setTitle("Title of info level");
        infoPhase.setTrainingDefinitionId(trainingDefinitionId);
        infoPhase.setOrder(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

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
