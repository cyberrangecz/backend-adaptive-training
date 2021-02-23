package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionPhaseRelationDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.exceptions.BadRequestException;
import cz.muni.ics.kypo.training.adaptive.mapping.mapstruct.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.mapstruct.QuestionPhaseRelationMapper;
import cz.muni.ics.kypo.training.adaptive.service.phases.InfoPhaseService;
import cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService;
import cz.muni.ics.kypo.training.adaptive.service.phases.QuestionnairePhaseService;
import cz.muni.ics.kypo.training.adaptive.service.phases.TrainingPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhaseFacade {

    private final PhaseService phaseService;
    private final InfoPhaseService infoPhaseService;
    private final QuestionnairePhaseService questionnairePhaseService;
    private final TrainingPhaseService trainingPhaseService;
    private PhaseMapper phaseMapper;
    private QuestionPhaseRelationMapper questionPhaseRelationMapper;

    @Autowired
    public PhaseFacade(PhaseService phaseService,
                       InfoPhaseService infoPhaseService,
                       QuestionnairePhaseService questionnairePhaseService,
                       TrainingPhaseService trainingPhaseService,
                       PhaseMapper phaseMapper,
                       QuestionPhaseRelationMapper questionPhaseRelationMapper) {
        this.phaseService = phaseService;
        this.infoPhaseService = infoPhaseService;
        this.questionnairePhaseService = questionnairePhaseService;
        this.trainingPhaseService = trainingPhaseService;
        this.phaseMapper = phaseMapper;
        this.questionPhaseRelationMapper = questionPhaseRelationMapper;
    }

    /**
     * Creates new phase in training definition
     *
     * @param definitionId - id of definition in which phase will be created
     * @return {@link BasicPhaseInfoDTO} of new questionnaire phase
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#definitionId)")
    @TransactionalWO
    public AbstractPhaseDTO createPhase(Long definitionId, PhaseCreateDTO phaseCreateDTO) {
        if (phaseCreateDTO.getPhaseType() == PhaseType.INFO) {
            return phaseMapper.mapToDTO(infoPhaseService.createDefaultInfoPhase(definitionId));
        } else if (phaseCreateDTO.getPhaseType() == PhaseType.TRAINING) {
            return phaseMapper.mapToDTO(trainingPhaseService.createDefaultTrainingPhase(definitionId));
        } else {
            return phaseMapper.mapToDTO(questionnairePhaseService.createDefaultQuestionnairePhase(definitionId, phaseCreateDTO.getQuestionnaireType()));
        }
    }

    /**
     * Deletes specific phase by id
     *
     * @param phaseId - id of phase to be deleted
     * @return the list of {@link AbstractPhaseDTO} all phase from given definition
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public List<AbstractPhaseDTO> deletePhase(Long phaseId) {
        Long definitionId = phaseService.deletePhase(phaseId);
        trainingPhaseService.alignDecisionMatrixForPhasesInTrainingDefinition(definitionId);
        return this.getPhases(definitionId);
    }

    /**
     * Finds specific phase by id
     *
     * @param phaseId - id of wanted phase
     * @return wanted {@link AbstractPhaseDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalRO
    public AbstractPhaseDTO getPhase(Long phaseId) {
        return phaseMapper.mapToDTO(phaseService.getPhase(phaseId));
    }

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#definitionId)")
    @TransactionalRO
    public List<AbstractPhaseDTO> getPhases(Long definitionId) {
        return phaseMapper.mapToListDTO(phaseService.getPhases(definitionId));
    }

    /**
     * Updates info phase from training definition
     *
     * @param phaseId            - id of the phase to be updated
     * @param infoPhaseUpdateDTO to be updated
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public InfoPhaseDTO updateInfoPhase(Long phaseId, InfoPhaseUpdateDTO infoPhaseUpdateDTO) {
        return this.phaseMapper.mapToInfoPhaseDTO(infoPhaseService.updateInfoPhase(phaseId, phaseMapper.mapToEntity(infoPhaseUpdateDTO)));
    }

    /**
     * Updates training phase from training definition
     *
     * @param phaseId                - id of the phase to be updated
     * @param trainingPhaseUpdateDTO training phase with the updated data
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public TrainingPhaseDTO updateTrainingPhase(Long phaseId, TrainingPhaseUpdateDTO trainingPhaseUpdateDTO) {
        return this.phaseMapper.mapToTrainingPhaseDTO(trainingPhaseService.updateTrainingPhase(phaseId, phaseMapper.mapToEntity(trainingPhaseUpdateDTO)));
    }

    /**
     * updates questionnaire phase from training definition
     *
     * @param phaseId                - id of the phase to be updated
     * @param questionnaireUpdateDTO questionnaire phase with the updated data
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public QuestionnairePhaseDTO updateQuestionnairePhase(Long phaseId, QuestionnaireUpdateDTO questionnaireUpdateDTO) {
        this.checkOrderOfPhaseRelations(questionnaireUpdateDTO.getPhaseRelations());
        QuestionnairePhase questionnairePhaseToUpdate = questionnairePhaseService.updateQuestionnairePhase(phaseId, this.phaseMapper.mapToEntity(questionnaireUpdateDTO));

        List<QuestionPhaseRelation> updatedQuestionPhaseRelations = questionnaireUpdateDTO.getPhaseRelations()
                .stream()
                .map(phaseRelationDTO -> this.questionnairePhaseService.updateQuestionPhaseRelation(
                        questionnairePhaseToUpdate,
                        this.questionPhaseRelationMapper.mapToEntity(phaseRelationDTO),
                        phaseRelationDTO.getQuestionIds(),
                        phaseRelationDTO.getPhaseId())
                )
                .collect(Collectors.toList());
        questionnairePhaseToUpdate.getQuestionPhaseRelations().clear();
        questionnairePhaseToUpdate.getQuestionPhaseRelations().addAll(updatedQuestionPhaseRelations);
        return phaseMapper.mapToQuestionnairePhaseDTO(questionnairePhaseToUpdate);
    }

    private void checkOrderOfPhaseRelations(List<QuestionPhaseRelationDTO> questionPhaseRelationDTOS) {
        questionPhaseRelationDTOS.sort(Comparator.comparing(QuestionPhaseRelationDTO::getOrder));
        Integer order = 0;
        for (QuestionPhaseRelationDTO questionPhaseRelationDTO : questionPhaseRelationDTOS) {
            if (!questionPhaseRelationDTO.getOrder().equals(order)) {
                throw new BadRequestException("The question phase relation has wrong order. The current order is " + questionPhaseRelationDTO.getOrder() +
                        " but should be " + order);
            }
            order++;
        }
    }

    /**
     * Move phase to the different position and modify orders of phase between moved phase and new position.
     *
     * @param phaseIdFrom - id of the phase to be moved to the new position
     * @param newPosition - position where phase will be moved
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseIdFrom)")
    @TransactionalWO
    public void movePhaseToSpecifiedOrder(Long phaseIdFrom, int newPosition) {
        phaseService.movePhaseToSpecifiedOrder(phaseIdFrom, newPosition);
    }
}
