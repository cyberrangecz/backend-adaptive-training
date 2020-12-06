package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.repository.BaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelOperationsService {

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    public void swapLevelsOrder(Long trainingDefinitionId, Long levelIdFrom, Long levelIdTo) {
        Optional<BaseLevel> levelFrom = baseLevelRepository.findById(levelIdFrom);
        Optional<BaseLevel> levelTo = baseLevelRepository.findById(levelIdTo);

        if (levelFrom.isEmpty() || levelTo.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int fromOrder = levelFrom.get().getOrderInTrainingDefinition();
        int toOrder = levelTo.get().getOrderInTrainingDefinition();

        levelFrom.get().setOrderInTrainingDefinition(toOrder);
        levelTo.get().setOrderInTrainingDefinition(fromOrder);

        baseLevelRepository.save(levelFrom.get());
        baseLevelRepository.save(levelTo.get());
    }
}
