package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseUpdateDTO;
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

    public AbstractPhaseDTO createPhase(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {
        AbstractPhaseDTO abstractPhaseDto;
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
    public List<AbstractPhaseDTO> deletePhase(Long definitionId, Long phaseId) {

        phaseService.deletePhase(definitionId, phaseId);

        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(definitionId);

        return getPhases(definitionId);
    }

    public AbstractPhaseDTO getPhase(Long definitionId, Long phaseId) {
        AbstractPhaseDTO phase = phaseService.getPhase(definitionId, phaseId);

        if (phase instanceof QuestionnairePhaseDTO) {
            QuestionnairePhaseDTO questionnairePhaseDto = (QuestionnairePhaseDTO) phase;
            if (QuestionnaireType.ADAPTIVE.equals(questionnairePhaseDto.getQuestionnaireType())) {
                phase.setPhaseType(PhaseType.QUESTIONNAIRE_ADAPTIVE);
            } else {
                phase.setPhaseType(PhaseType.QUESTIONNAIRE_GENERAL);
            }
        }

        return phase;
    }

    public List<AbstractPhaseDTO> getPhases(Long definitionId) {

        return phaseService.getPhases(definitionId);
    }

    public InfoPhaseDTO updateInfoPhase(Long definitionId, Long phaseId, InfoPhaseUpdateDTO infoPhaseUpdateDto) {
        return infoPhaseService.updateInfoPhase(definitionId, phaseId, infoPhaseUpdateDto);
    }

    public TrainingPhaseDTO updateTrainingPhase(Long definitionId, Long phaseId, TrainingPhaseUpdateDTO trainingPhaseUpdate) {
        return trainingPhaseService.updateTrainingPhase(definitionId, phaseId, trainingPhaseUpdate);
    }

    public QuestionnairePhaseDTO updateQuestionnairePhase(Long definitionId, Long phaseId, QuestionnaireUpdateDTO questionnaireUpdateDto) {
        return questionnairePhaseService.updateQuestionnairePhase(definitionId, phaseId, questionnaireUpdateDto);
    }

    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        phaseService.moveLevelToSpecifiedOrder(phaseIdFrom, newPosition);
    }


}
