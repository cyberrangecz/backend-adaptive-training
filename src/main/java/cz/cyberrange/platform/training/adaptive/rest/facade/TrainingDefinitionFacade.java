package cz.cyberrange.platform.training.adaptive.rest.facade;

import com.querydsl.core.types.Predicate;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security.IsDesignerOrAdmin;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security.IsDesignerOrOrganizerOrAdmin;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.security.IsOrganizerOrAdmin;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalRO;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalWO;
import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.PageResultResource;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionCreateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionInfoDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionUpdateDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.RoleType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.InternalServerErrorException;
import cz.cyberrange.platform.training.adaptive.api.mapping.PhaseMapper;
import cz.cyberrange.platform.training.adaptive.api.mapping.TrainingDefinitionMapper;
import cz.cyberrange.platform.training.adaptive.service.SecurityService;
import cz.cyberrange.platform.training.adaptive.service.UserService;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import cz.cyberrange.platform.training.adaptive.service.phases.PhaseService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;

/**
 * The type Training definition facade.
 */
@Service
@Transactional
public class TrainingDefinitionFacade {

    private final TrainingDefinitionService trainingDefinitionService;
    private final PhaseService phaseService;
    private final UserService userService;
    private final UserManagementServiceApi userManagementServiceApi;
    private final SecurityService securityService;
    private final TrainingDefinitionMapper trainingDefinitionMapper;
    private final PhaseMapper phaseMapper;

    /**
     * Instantiates a new Training definition facade.
     *
     * @param trainingDefinitionService the training definition service
     * @param trainingDefMapper         the training def mapper
     * @param phaseMapper               the phase mapper
     * @param userService               the user service
     * @param securityService           the security service
     */
    @Autowired
    public TrainingDefinitionFacade(TrainingDefinitionService trainingDefinitionService,
                                    PhaseService phaseService,
                                    UserService userService,
                                    UserManagementServiceApi userManagementServiceApi,
                                    SecurityService securityService,
                                    TrainingDefinitionMapper trainingDefMapper,
                                    PhaseMapper phaseMapper) {
        this.trainingDefinitionService = trainingDefinitionService;
        this.phaseService = phaseService;
        this.userService = userService;
        this.userManagementServiceApi = userManagementServiceApi;
        this.securityService = securityService;
        this.trainingDefinitionMapper = trainingDefMapper;
        this.phaseMapper = phaseMapper;
    }

    /**
     * Finds specific Training Definition by id
     *
     * @param id of a Training Definition that would be returned
     * @return specific {@link TrainingDefinitionByIdDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#id)")
    @TransactionalRO
    public TrainingDefinitionByIdDTO findById(Long id) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(id);
        TrainingDefinitionByIdDTO trainingDefinitionByIdDTO = trainingDefinitionMapper.mapToDTOById(trainingDefinition);
        trainingDefinitionByIdDTO.setPhases(gatherPhases(id));
        return trainingDefinitionByIdDTO;
    }

    private List<AbstractPhaseDTO> gatherPhases(Long definitionId) {
        List<AbstractPhase> phases = trainingDefinitionService.findAllPhasesFromDefinition(definitionId);
        Map<Long, Set<Question>> relatedQuestions = phases.stream()
                .filter(questionnaire -> questionnaire instanceof QuestionnairePhase && ((QuestionnairePhase) questionnaire).getQuestionnaireType() == QuestionnaireType.ADAPTIVE)
                .flatMap(questionnaire -> ((QuestionnairePhase) questionnaire).getQuestions().stream())
                .flatMap(question -> question.getQuestionPhaseRelations().stream())
                .collect(groupingBy(phaseRelation -> phaseRelation.getRelatedTrainingPhase().getId(),
                        flatMapping(phaseRelation -> phaseRelation.getQuestions().stream(), Collectors.toSet()))
                );
        List<AbstractPhaseDTO> phaseDTOs = phases.stream()
                .map(this.phaseMapper::mapToDTO)
                .collect(Collectors.toList());
        phaseDTOs.forEach(phase -> {
            if (phase.getPhaseType() == PhaseType.TRAINING) {
                ((TrainingPhaseDTO) phase).setRelatedQuestions(relatedQuestions.getOrDefault(phase.getId(), Collections.emptySet()).stream()
                        .map(this::mapToQuestionDTO)
                        .collect(Collectors.toList()));
            }
        });
        return phaseDTOs;
    }

    private QuestionDTO mapToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestionType(question.getQuestionType());
        questionDTO.setOrder(question.getOrder());
        questionDTO.setText(question.getText());
        return questionDTO;
    }

    /**
     * Find all Training Definitions.
     *
     * @param predicate represents a predicate (boolean-valued function) of one argument.
     * @param pageable  pageable parameter with information about pagination.
     * @return page of all {@link TrainingDefinitionDTO}
     */
    @IsDesignerOrAdmin
    @TransactionalRO
    public PageResultResource<TrainingDefinitionDTO> findAll(Predicate predicate, Pageable pageable) {
        if (securityService.hasRole(RoleTypeSecurity.ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)) {
            return mapToDtoAndAddArchivingInfo(trainingDefinitionService.findAll(predicate, pageable));
        } else {
            Long loggedInUserId = userManagementServiceApi.getLoggedInUserRefId();
            return mapToDtoAndAddArchivingInfo(trainingDefinitionService.findAll(predicate, pageable, loggedInUserId));
        }
    }

    private PageResultResource<TrainingDefinitionDTO> mapToDtoAndAddArchivingInfo(Page<TrainingDefinition> trainingDefinitionPage) {
        PageResultResource<TrainingDefinitionDTO> resource = trainingDefinitionMapper.mapToPageResultResource(trainingDefinitionPage);
        for (TrainingDefinitionDTO trainingDefinitionDTO : resource.getContent()) {
            trainingDefinitionDTO.setCanBeArchived(checkIfCanBeArchived(trainingDefinitionDTO.getId()));
        }
        return resource;
    }

    /**
     * Find all Training Definitions.
     *
     * @param state    represents a string if the training definitions should be relased or not.
     * @param pageable pageable parameter with information about pagination.
     * @return page of all {@link TrainingDefinitionInfoDTO} accessible for organizers
     */
    @IsOrganizerOrAdmin
    @TransactionalRO
    public PageResultResource<TrainingDefinitionInfoDTO> findAllForOrganizers(TDState state, Pageable pageable) {
        Long loggedInUserId = userManagementServiceApi.getLoggedInUserRefId();
        if (state == TDState.RELEASED) {
            return trainingDefinitionMapper.mapToPageResultResourceInfoDTO(
                    trainingDefinitionService.findAllByState(TDState.RELEASED, pageable));
        } else if (state == TDState.UNRELEASED) {
            if (securityService.hasRole(RoleTypeSecurity.ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)) {
                return trainingDefinitionMapper.mapToPageResultResourceInfoDTO(
                        trainingDefinitionService.findAllByState(TDState.UNRELEASED, pageable));
            } else if (securityService.hasRole(RoleTypeSecurity.ROLE_ADAPTIVE_TRAINING_DESIGNER) && securityService.hasRole(RoleTypeSecurity.ROLE_ADAPTIVE_TRAINING_ORGANIZER)) {
                return trainingDefinitionMapper.mapToPageResultResourceInfoDTO(
                        trainingDefinitionService.findAllForDesigner(loggedInUserId, pageable));
            } else {
                return new PageResultResource<>(new ArrayList<>(), new PageResultResource.Pagination());
            }
        }
        throw new InternalServerErrorException("It is required to provide training definition state that is RELEASED or UNRELEASED");
    }

    /**
     * Creates new training definition
     *
     * @param trainingDefinition to be created
     * @return DTO of created definition, {@link TrainingDefinitionCreateDTO}
     */
    @IsDesignerOrAdmin
    @TransactionalWO
    public TrainingDefinitionByIdDTO create(TrainingDefinitionCreateDTO trainingDefinition) {
        TrainingDefinition newTrainingDefinition = trainingDefinitionMapper.mapCreateToEntity(trainingDefinition);
        TrainingDefinition createdTrainingDefinition = trainingDefinitionService.create(newTrainingDefinition);
        if (trainingDefinition.isDefaultContent()) {
            phaseService.createPredefinedPhases(newTrainingDefinition.getId());
        }
        return trainingDefinitionMapper.mapToDTOById(createdTrainingDefinition);
    }

    /**
     * Updates training definition
     *
     * @param trainingDefinitionUpdateDTO to be updated
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#trainingDefinitionUpdateDTO.getId())")
    @TransactionalWO
    public void update(TrainingDefinitionUpdateDTO trainingDefinitionUpdateDTO) {
        TrainingDefinition mappedTrainingDefinition = trainingDefinitionMapper.mapUpdateToEntity(trainingDefinitionUpdateDTO);
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(trainingDefinitionUpdateDTO.getId());
        mappedTrainingDefinition.setAuthors(new HashSet<>(trainingDefinition.getAuthors()));
        mappedTrainingDefinition.setCreatedAt(trainingDefinition.getCreatedAt());
        trainingDefinitionService.update(mappedTrainingDefinition);
    }

    /**
     * Clones Training Definition by id
     *
     * @param id    of definition to be cloned
     * @param title the title of cloned definition
     * @return DTO of cloned definition, {@link TrainingDefinitionByIdDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#id)")
    @TransactionalWO
    public TrainingDefinitionByIdDTO clone(Long id, String title) {
        TrainingDefinitionByIdDTO clonedDefinition = trainingDefinitionMapper.mapToDTOById(trainingDefinitionService.clone(id, title));
        clonedDefinition.setPhases(gatherPhases(clonedDefinition.getId()));
        return clonedDefinition;
    }

    /**
     * Deletes specific training instance based on id
     *
     * @param id of definition to be deleted
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#id)")
    @TransactionalWO
    public void delete(Long id) {
        trainingDefinitionService.delete(id);
    }

    /**
     * Get users with given role
     *
     * @param roleType   the wanted role type
     * @param pageable   pageable parameter with information about pagination.
     * @param givenName  the given name
     * @param familyName the family name
     * @return list of users {@link UserRefDTO}
     */
    @IsDesignerOrAdmin
    @TransactionalRO
    public PageResultResource<UserRefDTO> getUsersWithGivenRole(RoleType roleType, Pageable pageable, String givenName, String familyName) {
        return userManagementServiceApi.getUserRefsByRole(roleType, pageable, givenName, familyName);
    }

    /**
     * Switch state of definition to unreleased
     *
     * @param definitionId - id of training definition
     * @param state        - new state of TD
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#definitionId)")
    public void switchState(Long definitionId, TDState state) {
        trainingDefinitionService.switchState(definitionId, state);
    }

    private boolean checkIfCanBeArchived(Long definitionId) {
        List<TrainingInstance> instances = trainingDefinitionService.findAllTrainingInstancesByTrainingDefinitionId(definitionId);
        for (TrainingInstance trainingInstance : instances) {
            if (trainingInstance.getEndTime().isAfter(LocalDateTime.now(Clock.systemUTC()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieve all authors for given training definition.
     *
     * @param trainingDefinitionId id of the training definition for which to get the authors
     * @param pageable             pageable parameter with information about pagination.
     * @param givenName            optional parameter used for filtration
     * @param familyName           optional parameter used for filtration
     * @return returns all authors in given training definition.
     */
    @IsDesignerOrOrganizerOrAdmin
    public PageResultResource<UserRefDTO> getAuthors(Long trainingDefinitionId, Pageable pageable, String givenName, String familyName) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(trainingDefinitionId);
        return userManagementServiceApi.getUserRefDTOsByUserIds(trainingDefinition.getAuthors().stream()
                        .map(User::getUserRefId)
                        .toList(),
                pageable, givenName, familyName);
    }

    /**
     * Retrieve all designers not in the given training definition.
     *
     * @param trainingDefinitionId id of the training definition which users should be excluded from the result list.
     * @param pageable             pageable parameter with information about pagination.
     * @param givenName            optional parameter used for filtration
     * @param familyName           optional parameter used for filtration
     * @return returns all designers not in the given training definition.
     */
    @IsDesignerOrOrganizerOrAdmin
    @TransactionalRO
    public PageResultResource<UserRefDTO> getDesignersNotInGivenTrainingDefinition(Long trainingDefinitionId, Pageable pageable, String givenName, String familyName) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(trainingDefinitionId);
        Set<Long> excludedUsers = trainingDefinition.getAuthors().stream()
                .map(User::getUserRefId)
                .collect(Collectors.toSet());
        return userManagementServiceApi.getUserRefsByRoleAndNotWithIds(RoleType.ROLE_TRAINING_DESIGNER, excludedUsers, pageable, givenName, familyName);
    }

    /**
     * Concurrently add authors to the given training definition and remove authors from the training definition.
     *
     * @param trainingDefinitionId if of the training definition to be updated
     * @param authorsAddition      ids of the authors to be added to the training definition
     * @param authorsRemoval       ids of the authors to be removed from the training definition.
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTrainingDefinition(#trainingDefinitionId)")
    @TransactionalWO
    public void editAuthors(Long trainingDefinitionId, Set<Long> authorsAddition, Set<Long> authorsRemoval) {
        TrainingDefinition trainingDefinition = trainingDefinitionService.findById(trainingDefinitionId);
        Long loggedInUserRefId = userManagementServiceApi.getLoggedInUserRefId();
        if (authorsRemoval != null && !authorsRemoval.isEmpty()) {
            authorsRemoval.remove(loggedInUserRefId);
            trainingDefinition.removeAuthorsByUserRefIds(authorsRemoval);
        }
        if (authorsAddition != null && !authorsAddition.isEmpty()) {
            addAuthorsToTrainingDefinition(trainingDefinition, authorsAddition);
        }
        trainingDefinitionService.auditAndSave(trainingDefinition);
    }

    private void addAuthorsToTrainingDefinition(TrainingDefinition trainingDefinition, Set<Long> userRefIds) {
        List<UserRefDTO> authors = getAllUsersRefsByGivenUsersIds(new ArrayList<>(userRefIds));
        Set<Long> actualAuthorsIds = trainingDefinition.getAuthors().stream()
                .map(User::getUserRefId)
                .collect(Collectors.toSet());
        for (UserRefDTO author : authors) {
            if (actualAuthorsIds.contains(author.getUserRefId())) {
                continue;
            }
            User user = userService.createOrGetUserRef(author.getUserRefId());
            trainingDefinition.addAuthor(user);
        }
    }

    private List<UserRefDTO> getAllUsersRefsByGivenUsersIds(List<Long> participantsRefIds) {
        List<UserRefDTO> users = new ArrayList<>();
        PageResultResource<UserRefDTO> usersPageResultResource;
        int page = 0;
        do {
            usersPageResultResource = userManagementServiceApi.getUserRefDTOsByUserIds(participantsRefIds, PageRequest.of(page, 999), null, null);
            users.addAll(usersPageResultResource.getContent());
            page++;
        }
        while (page < usersPageResultResource.getPagination().getTotalPages());
        return users;
    }
}
