package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.enums.PhaseType;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.mapping.PhaseMapper;
import cz.muni.ics.kypo.training.adaptive.mapping.QuestionPhaseRelationMapper;
import cz.muni.ics.kypo.training.adaptive.service.phases.*;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhaseFacade {

    private final PhaseService phaseService;
    private final InfoPhaseService infoPhaseService;
    private final QuestionnairePhaseService questionnairePhaseService;
    private final TrainingPhaseService trainingPhaseService;
    private final AccessPhaseService accessPhaseService;
    private final TrainingDefinitionService trainingDefinitionService;
    private final TaskService taskService;
    private final PhaseMapper phaseMapper;
    private final QuestionPhaseRelationMapper questionPhaseRelationMapper;

    @Autowired
    public PhaseFacade(PhaseService phaseService,
                       InfoPhaseService infoPhaseService,
                       QuestionnairePhaseService questionnairePhaseService,
                       TrainingPhaseService trainingPhaseService,
                       AccessPhaseService accessPhaseService,
                       TrainingDefinitionService trainingDefinitionService,
                       TaskService taskService,
                       PhaseMapper phaseMapper,
                       QuestionPhaseRelationMapper questionPhaseRelationMapper) {
        this.phaseService = phaseService;
        this.infoPhaseService = infoPhaseService;
        this.questionnairePhaseService = questionnairePhaseService;
        this.trainingPhaseService = trainingPhaseService;
        this.accessPhaseService = accessPhaseService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.taskService = taskService;
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
        TrainingDefinition trainingDefinition = this.trainingDefinitionService.findById(definitionId);
        AbstractPhase abstractPhase;
        if (phaseCreateDTO.getPhaseType() == PhaseType.INFO) {
            abstractPhase = infoPhaseService.createDefaultInfoPhase(trainingDefinition);
        } else if (phaseCreateDTO.getPhaseType() == PhaseType.TRAINING) {
            abstractPhase = trainingPhaseService.createDefaultTrainingPhase(trainingDefinition);
        } else if (phaseCreateDTO.getPhaseType() == PhaseType.ACCESS) {
            abstractPhase = accessPhaseService.createDefaultAccessPhase(trainingDefinition);
        } else {
            abstractPhase = questionnairePhaseService.createDefaultQuestionnairePhase(trainingDefinition, phaseCreateDTO.getQuestionnaireType());
        }
        this.trainingDefinitionService.auditAndSave(trainingDefinition);
        return phaseMapper.mapToDTO(abstractPhase);
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
        TrainingDefinition trainingDefinition = phaseService.deletePhase(phaseId);
        trainingPhaseService.alignDecisionMatrixAfterDelete(trainingDefinition.getId());
        trainingDefinitionService.auditAndSave(trainingDefinition);
        return this.getPhases(trainingDefinition.getId());
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
     * Updates phases from the given training definition
     *
     * @param definitionId    - id of the training definition
     * @param phaseUpdateDTOS phases to be updated
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#definitionId)")
    @TransactionalWO
    public void updatePhases(Long definitionId, List<AbstractPhaseUpdateDTO> phaseUpdateDTOS) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(definitionId);
        trainingDefinitionService.checkIfCanBeUpdated(trainingDefinition);
        Map<Long, AbstractPhase> persistedPhasesById = phaseService.getPhases(definitionId).stream()
                .collect(Collectors.toMap(AbstractPhase::getId, Function.identity()));
        phaseUpdateDTOS.forEach(updatedPhaseDTO -> {
            AbstractPhase persistedPhase = persistedPhasesById.get(updatedPhaseDTO.getId());
            if (persistedPhase == null) {
                throw new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", Long.class,
                        updatedPhaseDTO.getId(), "Phase was not found in definition (id: " + definitionId + ")."));
            }
            switch (updatedPhaseDTO.getPhaseType()) {
                case TRAINING:
                    TrainingPhase updatedTrainingPhase = phaseMapper.mapToEntity((TrainingPhaseUpdateDTO) updatedPhaseDTO);
                    List<Task> updatedTasks = updatedTrainingPhase.getTasks();
                    trainingPhaseService.updateTrainingPhase((TrainingPhase) persistedPhase, updatedTrainingPhase);
                    taskService.updateTasks(updatedTasks, updatedTrainingPhase.getTasks(), updatedTrainingPhase.getId());
                    break;
                case ACCESS:
                    accessPhaseService.updateAccessPhase((AccessPhase) persistedPhase, phaseMapper.mapToEntity((AccessPhaseUpdateDTO) updatedPhaseDTO));
                    break;
                case INFO:
                    infoPhaseService.updateInfoPhase((InfoPhase) persistedPhase, phaseMapper.mapToEntity((InfoPhaseUpdateDTO) updatedPhaseDTO));
                    break;
                case QUESTIONNAIRE:
                    QuestionnairePhase updatedQuestionnairePhase = questionnairePhaseService.updateQuestionnairePhase((QuestionnairePhase) persistedPhase, phaseMapper.mapToEntity((QuestionnaireUpdateDTO) updatedPhaseDTO));
                    this.updateQuestionnairePhaseRelations((QuestionnaireUpdateDTO) updatedPhaseDTO, updatedQuestionnairePhase);
                    break;
            }
        });
        trainingDefinitionService.auditAndSave(trainingDefinition);
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
        InfoPhase persistedPhase = infoPhaseService.findInfoPhaseById(phaseId);
        TrainingDefinition trainingDefinition = persistedPhase.getTrainingDefinition();
        trainingDefinitionService.checkIfCanBeUpdated(trainingDefinition);
        InfoPhase infoPhase = infoPhaseService.updateInfoPhase(persistedPhase, phaseMapper.mapToEntity(infoPhaseUpdateDTO));
        trainingDefinitionService.auditAndSave(trainingDefinition);
        return this.phaseMapper.mapToInfoPhaseDTO(infoPhase);
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
        TrainingPhase persistedPhase = trainingPhaseService.findPhaseById(trainingPhaseUpdateDTO.getId());
        TrainingDefinition trainingDefinition = persistedPhase.getTrainingDefinition();
        trainingDefinitionService.checkIfCanBeUpdated(trainingDefinition);

        TrainingPhase updatedTrainingPhase = phaseMapper.mapToEntity(trainingPhaseUpdateDTO);
        List<Task> updatedTasks = updatedTrainingPhase.getTasks();
        TrainingPhase trainingPhase = trainingPhaseService.updateTrainingPhase(persistedPhase, updatedTrainingPhase);
        taskService.updateTasks(updatedTasks, updatedTrainingPhase.getTasks(), updatedTrainingPhase.getId());
        trainingDefinitionService.auditAndSave(trainingPhase.getTrainingDefinition());
        return this.phaseMapper.mapToTrainingPhaseDTO(trainingPhase);
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
        QuestionnairePhase persistedPhase = questionnairePhaseService.findQuestionnairePhaseById(phaseId);
        TrainingDefinition trainingDefinition = persistedPhase.getTrainingDefinition();
        trainingDefinitionService.checkIfCanBeUpdated(trainingDefinition);
        QuestionnairePhase questionnairePhaseToUpdate = questionnairePhaseService.updateQuestionnairePhase(persistedPhase, this.phaseMapper.mapToEntity(questionnaireUpdateDTO));
        this.updateQuestionnairePhaseRelations(questionnaireUpdateDTO, questionnairePhaseToUpdate);
        trainingDefinitionService.auditAndSave(questionnairePhaseToUpdate.getTrainingDefinition());
        return phaseMapper.mapToQuestionnairePhaseDTO(questionnairePhaseToUpdate);
    }

    private void updateQuestionnairePhaseRelations(QuestionnaireUpdateDTO questionnaireUpdateDTO, QuestionnairePhase questionnairePhaseToUpdate) {
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
        AbstractPhase abstractPhase = phaseService.movePhaseToSpecifiedOrder(phaseIdFrom, newPosition);
        trainingDefinitionService.auditAndSave(abstractPhase.getTrainingDefinition());
    }
}
