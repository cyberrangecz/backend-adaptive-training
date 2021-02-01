package com.example.demo.facade;

import com.example.demo.dto.AbstractPhaseDto;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.dto.QuestionnairePhaseDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TrainingPhaseDto;
import com.example.demo.dto.TrainingPhaseUpdateDto;
import com.example.demo.enums.PhaseType;
import com.example.demo.service.InfoPhaseService;
import com.example.demo.service.PhaseService;
import com.example.demo.service.QuestionnairePhaseService;
import com.example.demo.service.TrainingPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingPhaseFacade {

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private InfoPhaseService infoPhaseService;

    @Autowired
    private QuestionnairePhaseService questionnairePhaseService;

    @Autowired
    private TrainingPhaseService trainingPhaseService;

    public AbstractPhaseDto createPhase(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {
        AbstractPhaseDto abstractPhaseDto;
        if (PhaseType.INFO.equals(phaseCreateDTO.getPhaseType())) {
            abstractPhaseDto = infoPhaseService.createDefaultInfoPhase(trainingDefinitionId);
        } else if (PhaseType.TRAINING.equals(phaseCreateDTO.getPhaseType())) {
            abstractPhaseDto = trainingPhaseService.createDefaultTrainingPhase(trainingDefinitionId);
        } else {
            abstractPhaseDto = questionnairePhaseService.createDefaultQuestionnairePhase(trainingDefinitionId);
        }

        abstractPhaseDto.setPhaseType(phaseCreateDTO.getPhaseType());

        return abstractPhaseDto;
    }


    @Transactional
    public List<AbstractPhaseDto> deletePhase(Long definitionId, Long phaseId) {

        phaseService.deletePhase(definitionId, phaseId);

        return getPhases(definitionId);
    }

    public AbstractPhaseDto getPhase(Long definitionId, Long phaseId) {
        return phaseService.getPhase(definitionId, phaseId);
    }

    public List<AbstractPhaseDto> getPhases(Long definitionId) {

        return phaseService.getPhases(definitionId);
    }

    public InfoPhaseDto updateInfoPhase(Long definitionId, Long phaseId, InfoPhaseUpdateDto infoPhaseUpdateDto) {
        return infoPhaseService.updateInfoPhase(definitionId, phaseId, infoPhaseUpdateDto);
    }

    public TrainingPhaseDto updateTrainingPhase(Long definitionId, Long phaseId, TrainingPhaseUpdateDto trainingPhaseUpdate) {
        return trainingPhaseService.updateTrainingPhase(definitionId, phaseId, trainingPhaseUpdate);
    }

    public QuestionnairePhaseDto updateQuestionnairePhase(Long definitionId, Long phaseId, QuestionnaireUpdateDto questionnaireUpdateDto) {
        return questionnairePhaseService.updateQuestionnairePhase(definitionId, phaseId, questionnaireUpdateDto);
    }

    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        phaseService.moveLevelToSpecifiedOrder(phaseIdFrom, newPosition);
    }


}
