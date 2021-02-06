package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnairePhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnaireUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseUpdateDto;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.service.InfoPhaseService;
import cz.muni.ics.kypo.training.adaptive.service.PhaseService;
import cz.muni.ics.kypo.training.adaptive.service.QuestionnairePhaseService;
import cz.muni.ics.kypo.training.adaptive.service.TrainingPhaseService;
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
            abstractPhaseDto = questionnairePhaseService.createDefaultQuestionnairePhase(trainingDefinitionId, phaseCreateDTO);
        }

        abstractPhaseDto.setPhaseType(phaseCreateDTO.getPhaseType());

        return abstractPhaseDto;
    }


    @Transactional
    public List<AbstractPhaseDto> deletePhase(Long definitionId, Long phaseId) {

        phaseService.deletePhase(definitionId, phaseId);

        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(definitionId);

        return getPhases(definitionId);
    }

    public AbstractPhaseDto getPhase(Long definitionId, Long phaseId) {
        AbstractPhaseDto phase = phaseService.getPhase(definitionId, phaseId);

        if (phase instanceof QuestionnairePhaseDto) {
            QuestionnairePhaseDto questionnairePhaseDto = (QuestionnairePhaseDto) phase;
            if (QuestionnaireType.ADAPTIVE.equals(questionnairePhaseDto.getQuestionnaireType())) {
                phase.setPhaseType(PhaseType.QUESTIONNAIRE_ADAPTIVE);
            } else {
                phase.setPhaseType(PhaseType.QUESTIONNAIRE_GENERAL);
            }
        }

        return phase;
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
