package com.example.demo.service;

import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.InfoLevelCreateDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import com.example.demo.repository.InfoLevelRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InfoLevelService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoLevelService.class);

    private final InfoLevelRepository infoLevelRepository;
    private final BaseLevelRepository baseLevelRepository;



    @Autowired
    public InfoLevelService(InfoLevelRepository infoLevelRepository, BaseLevelRepository baseLevelRepository) {
        this.infoLevelRepository = infoLevelRepository;
        this.baseLevelRepository = baseLevelRepository;
    }

    public InfoLevelDto createDefaultInfoLevel(Long trainingDefinitionId) {
        InfoLevel infoLevel = new InfoLevel();
        infoLevel.setContent("Content of info level");
        infoLevel.setTitle("Title of info level");
        infoLevel.setTrainingDefinitionId(trainingDefinitionId);
        infoLevel.setOrder(baseLevelRepository.getCurrentMaxOrder(trainingDefinitionId) + 1);

        InfoLevel persistedEntity = infoLevelRepository.save(infoLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public InfoLevelDto createInfoLevel(InfoLevelCreateDto infoLevelCreateDto) {
        InfoLevel infoLevel = BeanMapper.INSTANCE.toEntity(infoLevelCreateDto);

        InfoLevel persistedEntity = infoLevelRepository.save(infoLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public List<InfoLevelDto> findAllInfoLevels() {

        Iterable<InfoLevel> allInfoLevels = infoLevelRepository.findAll();

        List<InfoLevelDto> result = new ArrayList<>();

        if (!IterableUtils.isEmpty(allInfoLevels)) {
            for (InfoLevel infoLevel : allInfoLevels) {
                result.add(BeanMapper.INSTANCE.toDto(infoLevel));
            }
        }

        return result;
    }

    public InfoLevelDto getInfoLevel(Long id) {
        Optional<InfoLevel> infoLevel = infoLevelRepository.findById(id);

        if (infoLevel.isEmpty()) {
            LOG.error("No info level found with ID {}.", id);
            return new InfoLevelDto();
        }

        return BeanMapper.INSTANCE.toDto(infoLevel.get());
    }

    public InfoLevelDto updateInfoLevel(InfoLevel infoLevelUpdate) {
        Optional<InfoLevel> persistedInfoLevel = infoLevelRepository.findById(infoLevelUpdate.getId());

        if (persistedInfoLevel.isEmpty()) {
            // TODO return 404
            LOG.error("No info level found with ID {}.", infoLevelUpdate.getId());
            return new InfoLevelDto();
        }

        infoLevelUpdate.setTrainingDefinitionId(persistedInfoLevel.get().getTrainingDefinitionId());
        infoLevelUpdate.setOrder(persistedInfoLevel.get().getOrder());

        InfoLevel savedEntity = infoLevelRepository.save(infoLevelUpdate);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void removeInfoLevel(Long id) {
        infoLevelRepository.deleteById(id);
    }
}
