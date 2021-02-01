package com.example.demo.facade;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.dto.TrainingPhaseDto;
import com.example.demo.dto.TrainingPhaseUpdateDto;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.enums.PhaseType;
import com.example.demo.service.InfoPhaseService;
import com.example.demo.service.TrainingPhaseService;
import com.example.demo.service.PhaseService;
import com.example.demo.service.QuestionnaireLevelService;
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
    private QuestionnaireLevelService questionnaireLevelService;

    @Autowired
    private TrainingPhaseService trainingPhaseService;

    public BaseLevelDto createPhase(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {
        BaseLevelDto baseLevelDto;
        if (PhaseType.INFO.equals(phaseCreateDTO.getPhaseType())) {
            baseLevelDto = infoPhaseService.createDefaultInfoPhase(trainingDefinitionId);
        } else if (PhaseType.QUESTIONNAIRE.equals(phaseCreateDTO.getPhaseType())) {
            baseLevelDto = questionnaireLevelService.createDefaultQuestionnaireLevel(trainingDefinitionId);
        } else {
            baseLevelDto = trainingPhaseService.createDefaultTrainingPhase(trainingDefinitionId);
        }

        baseLevelDto.setPhaseType(phaseCreateDTO.getPhaseType());

        return baseLevelDto;
    }


    @Transactional
    public List<BaseLevelDto> deletePhase(Long definitionId, Long phaseId) {

        phaseService.deletePhase(definitionId, phaseId);

        return getPhases(definitionId);
    }

    public BaseLevelDto getPhase(Long definitionId, Long phaseId) {
        return phaseService.getPhase(definitionId, phaseId);
    }

    public List<BaseLevelDto> getPhases(Long definitionId) {

        return phaseService.getPhases(definitionId);
    }

    public InfoPhaseDto updateInfoPhase(Long definitionId, Long phaseId, InfoPhaseUpdateDto infoPhaseUpdateDto) {
        return infoPhaseService.updateInfoPhase(definitionId, phaseId, infoPhaseUpdateDto);
    }

    public TrainingPhaseDto updateTrainingPhase(Long definitionId, Long phaseId, TrainingPhaseUpdateDto trainingPhaseUpdate) {
        return trainingPhaseService.updateTrainingPhase(definitionId, phaseId, trainingPhaseUpdate);
    }

    public QuestionnaireLevelDto updateQuestionnairePhase(Long definitionId, Long phaseId, QuestionnaireUpdateDto questionnaireUpdateDto) {
        return questionnaireLevelService.updateQuestionnairePhase(definitionId, phaseId, questionnaireUpdateDto);
    }

    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        phaseService.moveLevelToSpecifiedOrder(phaseIdFrom, newPosition);
    }


}
