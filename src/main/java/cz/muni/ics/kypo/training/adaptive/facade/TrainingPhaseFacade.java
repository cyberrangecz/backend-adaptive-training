package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseTypeCreate;
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

    private final PhaseService phaseService;
    private final InfoPhaseService infoPhaseService;
    private final QuestionnairePhaseService questionnairePhaseService;
    private final TrainingPhaseService trainingPhaseService;

    @Autowired
    public TrainingPhaseFacade(PhaseService phaseService, InfoPhaseService infoPhaseService,
                               QuestionnairePhaseService questionnairePhaseService, TrainingPhaseService trainingPhaseService) {
        this.phaseService = phaseService;
        this.infoPhaseService = infoPhaseService;
        this.questionnairePhaseService = questionnairePhaseService;
        this.trainingPhaseService = trainingPhaseService;
    }

    public AbstractPhaseDTO createPhase(Long trainingDefinitionId, PhaseCreateDTO phaseCreateDTO) {
        AbstractPhaseDTO abstractPhaseDto;
        if (PhaseTypeCreate.INFO.equals(phaseCreateDTO.getPhaseType())) {
            abstractPhaseDto = infoPhaseService.createDefaultInfoPhase(trainingDefinitionId);
        } else if (PhaseTypeCreate.TRAINING.equals(phaseCreateDTO.getPhaseType())) {
            abstractPhaseDto = trainingPhaseService.createDefaultTrainingPhase(trainingDefinitionId);
        } else {
            abstractPhaseDto = questionnairePhaseService.createDefaultQuestionnairePhase(trainingDefinitionId, phaseCreateDTO);
        }

        return abstractPhaseDto;
    }


    @Transactional
    public List<AbstractPhaseDTO> deletePhase(Long definitionId, Long phaseId) {

        phaseService.deletePhase(definitionId, phaseId);

        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(definitionId);

        return getPhases(definitionId);
    }

    public AbstractPhaseDTO getPhase(Long definitionId, Long phaseId) {
        return phaseService.getPhase(definitionId, phaseId);
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
        phaseService.movePhaseToSpecifiedOrder(phaseIdFrom, newPosition);
    }


}
