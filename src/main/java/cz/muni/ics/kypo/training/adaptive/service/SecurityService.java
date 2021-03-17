package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity;
import cz.muni.ics.kypo.training.adaptive.exceptions.*;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingDefinitionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingRunRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;


/**
 * The type Security service.
 */
@Service
@TransactionalRO(propagation = Propagation.REQUIRES_NEW)
public class SecurityService {

    private final UserManagementServiceApi userManagementServiceApi;
    private final TrainingRunRepository trainingRunRepository;
    private final TrainingDefinitionRepository trainingDefinitionRepository;
    private final TrainingInstanceRepository trainingInstanceRepository;

    /**
     * Instantiates a new Security service.
     *
     * @param trainingInstanceRepository   the training instance repository
     * @param trainingDefinitionRepository the training definition repository
     * @param trainingRunRepository        the training run repository
     */
    @Autowired
    public SecurityService(UserManagementServiceApi userManagementServiceApi,
                           TrainingInstanceRepository trainingInstanceRepository,
                           TrainingDefinitionRepository trainingDefinitionRepository,
                           TrainingRunRepository trainingRunRepository) {
        this.userManagementServiceApi = userManagementServiceApi;
        this.trainingDefinitionRepository = trainingDefinitionRepository;
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.trainingRunRepository = trainingRunRepository;
    }

    /**
     * Is trainee of given training run boolean.
     *
     * @param trainingRunId the training run id
     * @return the boolean
     */
    public boolean isTraineeOfGivenTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = trainingRunRepository.findByIdWithPhase(trainingRunId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(),
                        trainingRunId, "The necessary permissions are required for a resource.")));
        return trainingRun.getParticipantRef().getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId());
    }

    /**
     * Is organizer of given training instance boolean.
     *
     * @param instanceId the instance id
     * @return the boolean
     */
    public boolean isOrganizerOfGivenTrainingInstance(Long instanceId) {
        TrainingInstance trainingInstance = trainingInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "id", instanceId.getClass(),
                        instanceId, "The necessary permissions are required for a resource.")));
        return trainingInstance.getOrganizers().stream()
                .anyMatch(o -> o.getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId()));
    }

    /**
     * Is organizer of given training run.
     *
     * @param trainingRunId the run id
     * @return the boolean
     */
    public boolean isOrganizerOfGivenTrainingRun(Long trainingRunId) {
        TrainingRun trainingRun = trainingRunRepository.findById(trainingRunId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingRun.class, "id", trainingRunId.getClass(),
                        trainingRunId, "The necessary permissions are required for a resource.")));
        return trainingRun.getTrainingInstance().getOrganizers().stream()
                .anyMatch(o -> o.getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId()));
    }

    /**
     * Is designer of given training definition boolean.
     *
     * @param definitionId the definition id
     * @return the boolean
     */
    public boolean isDesignerOfGivenTrainingDefinition(Long definitionId) {
        TrainingDefinition trainingDefinition = trainingDefinitionRepository.findById(definitionId)
                .orElseThrow(() -> new ForbiddenException("The necessary permissions are required for a resource."));
        return trainingDefinition.getAuthors().stream()
                .anyMatch(a -> a.getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId()));
    }

    /**
     * Is designer of given phase boolean.
     *
     * @param phaseId the phase id
     * @return the boolean
     */
    public boolean isDesignerOfGivenPhase(Long phaseId) {
        TrainingDefinition trainingDefinition = trainingDefinitionRepository.findByPhaseId(phaseId)
                .orElseThrow(() -> new ForbiddenException("The necessary permissions are required for a resource."));
        return trainingDefinition.getAuthors().stream()
                .anyMatch(a -> a.getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId()));
    }

    /**
     * Is designer of given task boolean.
     *
     * @param taskId the task id
     * @return the boolean
     */
    public boolean isDesignerOfGivenTask(Long taskId) {
        TrainingDefinition trainingDefinition = trainingDefinitionRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ForbiddenException("The necessary permissions are required for a resource."));
        return trainingDefinition.getAuthors().stream()
                .anyMatch(a -> a.getUserRefId().equals(userManagementServiceApi.getLoggedInUserRefId()));
    }

    /**
     * Has role boolean.
     *
     * @param roleTypeSecurity the role type security
     * @return the boolean
     */
    public boolean hasRole(RoleTypeSecurity roleTypeSecurity) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority gA : authentication.getUserAuthentication().getAuthorities()) {
            if (gA.getAuthority().equals(roleTypeSecurity.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if response from external API is not null.
     *
     * @param object  object to check
     * @param message exception message if response is null
     * @throws MicroserviceApiException if response is null
     */
    private void checkNonNull(Object object, String message) {
        if (object == null) {
            throw new MicroserviceApiException(message);
        }
    }

}
