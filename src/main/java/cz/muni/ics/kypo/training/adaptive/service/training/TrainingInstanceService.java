package cz.muni.ics.kypo.training.adaptive.service.training;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.domain.AccessToken;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.AccessTokenRepository;
import cz.muni.ics.kypo.training.adaptive.repository.UserRefRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingInstanceRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingRunRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The type Training instance service.
 */
@Service
public class TrainingInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingInstanceService.class);
    private final Random random = new Random();
    private final TrainingInstanceRepository trainingInstanceRepository;
    private final TrainingRunRepository trainingRunRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final UserRefRepository organizerRefRepository;
    private final UserManagementServiceApi userManagementServiceApi;

    /**
     * Instantiates a new Training instance service.
     *
     * @param trainingInstanceRepository the training instance repository
     * @param accessTokenRepository      the access token repository
     * @param trainingRunRepository      the training run repository
     * @param organizerRefRepository     the organizer ref repository
     * @param userManagementServiceApi   the user management service
     */
    @Autowired

    public TrainingInstanceService(TrainingInstanceRepository trainingInstanceRepository,
                                   AccessTokenRepository accessTokenRepository,
                                   TrainingRunRepository trainingRunRepository,
                                   UserRefRepository organizerRefRepository,
                                   UserManagementServiceApi userManagementServiceApi) {
        this.trainingInstanceRepository = trainingInstanceRepository;
        this.trainingRunRepository = trainingRunRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.organizerRefRepository = organizerRefRepository;
        this.userManagementServiceApi = userManagementServiceApi;
    }

    /**
     * Finds basic info about Training Instance by id
     *
     * @param instanceId of a Training Instance that would be returned
     * @return specific {@link TrainingInstance} by id
     * @throws EntityNotFoundException training instance is not found.
     */
    public TrainingInstance findById(Long instanceId) {
        return trainingInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "id", instanceId.getClass(), instanceId)));
    }

    /**
     * Find specific Training instance by id including its associated Training definition.
     *
     * @param instanceId the instance id
     * @return the {@link TrainingInstance}
     */
    public TrainingInstance findByIdIncludingDefinition(Long instanceId) {
        return trainingInstanceRepository.findByIdIncludingDefinition(instanceId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "id", instanceId.getClass(), instanceId)));
    }

    /**
     * Find all Training Instances.
     *
     * @param predicate represents a predicate (boolean-valued function) of one argument.
     * @param pageable  pageable parameter with information about pagination.
     * @return all {@link TrainingInstance}s
     */
    public Page<TrainingInstance> findAll(Predicate predicate, Pageable pageable) {
        return trainingInstanceRepository.findAll(predicate, pageable);
    }

    /**
     * Find all training instances based on the logged in user.
     *
     * @param predicate      the predicate
     * @param pageable       the pageable
     * @param loggedInUserId the logged in user id
     * @return the page
     */
    public Page<TrainingInstance> findAll(Predicate predicate, Pageable pageable, Long loggedInUserId) {
        return trainingInstanceRepository.findAll(predicate, pageable, loggedInUserId);
    }

    /**
     * Creates new training instance
     *
     * @param trainingInstance to be created
     * @return created {@link TrainingInstance}
     */
    public TrainingInstance create(TrainingInstance trainingInstance) {
        trainingInstance.setAccessToken(generateAccessToken(trainingInstance.getAccessToken().trim()));
        if (trainingInstance.getStartTime().isAfter(trainingInstance.getEndTime())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", trainingInstance.getId().getClass(), trainingInstance.getId(),
                    "End time must be later than start time."));
        }
        addLoggedInUserAsOrganizerToTrainingInstance(trainingInstance);
        return this.auditAndSave(trainingInstance);
    }

    /**
     * updates training instance
     *
     * @param trainingInstanceToUpdate to be updated
     * @return new access token if it was changed
     * @throws EntityNotFoundException training instance is not found.
     * @throws EntityConflictException cannot be updated for some reason.
     */
    public String update(TrainingInstance trainingInstanceToUpdate) {
        validateStartAndEndTime(trainingInstanceToUpdate);
        TrainingInstance trainingInstance = findById(trainingInstanceToUpdate.getId());
        checkNotRevivingAnExpiredInstance(trainingInstanceToUpdate, trainingInstance);
        //add original organizers to update
        trainingInstanceToUpdate.setOrganizers(new HashSet<>(trainingInstance.getOrganizers()));
        addLoggedInUserAsOrganizerToTrainingInstance(trainingInstanceToUpdate);
        //check if TI is running, true - only title can be changed, false - any field can be changed
        if (trainingInstance.notStarted()) {
            //check if new access token should be generated, if not original is kept
            if (isAccessTokenChanged(trainingInstance.getAccessToken(), trainingInstanceToUpdate.getAccessToken())) {
                trainingInstanceToUpdate.setAccessToken(generateAccessToken(trainingInstanceToUpdate.getAccessToken()));
            } else {
                trainingInstanceToUpdate.setAccessToken(trainingInstance.getAccessToken());
            }
        } else {
            this.checkChangedFieldsOfTrainingInstance(trainingInstanceToUpdate, trainingInstance);
            trainingInstanceToUpdate.setAccessToken(trainingInstance.getAccessToken());
        }
        return this.auditAndSave(trainingInstanceToUpdate).getAccessToken();
    }

    /**
     * Sets audit attributes to training instance and save.
     *
     * @param trainingInstance the training instance to be saved.
     */
    public TrainingInstance auditAndSave(TrainingInstance trainingInstance) {
        trainingInstance.setLastEdited(getCurrentTimeInUTC());
        trainingInstance.setLastEditedBy(userManagementServiceApi.getUserRefDTO().getUserRefFullName());
        return trainingInstanceRepository.save(trainingInstance);
    }

    private LocalDateTime getCurrentTimeInUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    private void validateStartAndEndTime(TrainingInstance trainingInstance) {
        if (trainingInstance.getStartTime().isAfter(trainingInstance.getEndTime())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id",
                    trainingInstance.getId().getClass(), trainingInstance.getId(),
                    "End time must be later than start time."));
        }
    }

    private void checkNotRevivingAnExpiredInstance(TrainingInstance trainingInstanceToUpdate, TrainingInstance currentTrainingInstance) {
        if (currentTrainingInstance.finished() && !trainingInstanceToUpdate.finished()) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id",
                    trainingInstanceToUpdate.getId().getClass(), trainingInstanceToUpdate.getId(),
                    "End time of an expired instance cannot be set to the future."));
        }
    }

    private void checkChangedFieldsOfTrainingInstance(TrainingInstance trainingInstanceToUpdate, TrainingInstance currentTrainingInstance) {
        if (!currentTrainingInstance.getStartTime().equals(trainingInstanceToUpdate.getStartTime())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", Long.class, trainingInstanceToUpdate.getId(),
                    "The start time of the running or finished training instance cannot be changed. Only title and end time can be updated."));
        } else if (isAccessTokenChanged(currentTrainingInstance.getAccessToken(), trainingInstanceToUpdate.getAccessToken())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", Long.class, trainingInstanceToUpdate.getId(),
                    "The access token of the running or finished training instance cannot be changed. Only title and end time can be updated."));
        } else if (!Objects.equals(currentTrainingInstance.getPoolId(), trainingInstanceToUpdate.getPoolId())) {
            throw new EntityConflictException(new EntityErrorDetail(TrainingInstance.class, "id", Long.class, currentTrainingInstance.getId(),
                    "The pool in the running or finished instance cannot be changed. Only title and end time can be updated."));
        }
    }

    private boolean isAccessTokenChanged(String originalToken, String newToken) {
        //new token should not be generated if token in update equals original token without PIN
        String originalTokenWithoutPin = originalToken.substring(0, originalToken.length() - 5);
        return !newToken.equals(originalTokenWithoutPin);
    }

    private String generateAccessToken(String accessToken) {
        String newPass = "";
        boolean generated = false;
        while (!generated) {
            int firstNumber = this.random.nextInt(5) + 5;
            String pin = firstNumber + RandomStringUtils.random(3, false, true);
            newPass = accessToken + "-" + pin;
            Optional<AccessToken> pW = accessTokenRepository.findOneByAccessToken(newPass);
            if (!pW.isPresent()) {
                generated = true;
            }
        }
        AccessToken newTokenInstance = new AccessToken();
        newTokenInstance.setAccessToken(newPass);
        accessTokenRepository.saveAndFlush(newTokenInstance);
        return newPass;
    }

    private void addLoggedInUserAsOrganizerToTrainingInstance(TrainingInstance trainingInstance) {
        User userRef = organizerRefRepository.createOrGet(userManagementServiceApi.getLoggedInUserRefId());
        trainingInstance.addOrganizer(userRef);
    }

    /**
     * deletes training instance
     *
     * @param trainingInstance the training instance to be deleted.
     * @throws EntityNotFoundException training instance is not found.
     * @throws EntityConflictException cannot be deleted for some reason.
     */
    public void delete(TrainingInstance trainingInstance) {
        trainingInstanceRepository.delete(trainingInstance);
        LOG.debug("Training instance with id: {} deleted.", trainingInstance.getId());
    }

    /**
     * Deletes training instance
     *
     * @param id the training instance to be deleted.
     * @throws EntityNotFoundException training instance is not found.
     * @throws EntityConflictException cannot be deleted for some reason.
     */
    public void deleteById(Long id) {
        trainingInstanceRepository.deleteById(id);
        LOG.debug("Training instance with id: {} deleted.", id);
    }

    /**
     * Finds all Training Runs of specific Training Instance.
     *
     * @param instanceId id of Training Instance whose Training Runs would be returned.
     * @param isActive   if isActive attribute is True, only active runs are returned
     * @param pageable   pageable parameter with information about pagination.
     * @return {@link TrainingRun}s of specific {@link TrainingInstance}
     */
    public Page<TrainingRun> findTrainingRunsByTrainingInstance(Long instanceId, Boolean isActive, Pageable pageable) {
        // check if instance exists
        this.findById(instanceId);
        if (isActive == null) {
            return trainingRunRepository.findAllByTrainingInstanceId(instanceId, pageable);
        } else if (isActive) {
            return trainingRunRepository.findAllActiveByTrainingInstanceId(instanceId, pageable);
        } else {
            return trainingRunRepository.findAllInactiveByTrainingInstanceId(instanceId, pageable);
        }
    }

    /**
     * Find UserRefs by userRefId
     *
     * @param usersRefId of wanted UserRefs
     * @return {@link User}s with corresponding userRefIds
     */
    public Set<User> findUserRefsByUserRefIds(Set<Long> usersRefId) {
        return organizerRefRepository.findUsers(usersRefId);
    }

    /**
     * Check if instance is finished.
     *
     * @param trainingInstanceId the training instance id
     * @return true if instance is finished, false if not
     */
    public boolean checkIfInstanceIsFinished(Long trainingInstanceId) {
        return trainingInstanceRepository.isFinished(trainingInstanceId, LocalDateTime.now(Clock.systemUTC()));
    }

    /**
     * Find specific Training instance by its access token and with start time before current time and ending time after current time
     *
     * @param accessToken of Training instance
     * @return Training instance
     */
    public TrainingInstance findByStartTimeAfterAndEndTimeBeforeAndAccessToken(String accessToken) {
        return trainingInstanceRepository.findByStartTimeAfterAndEndTimeBeforeAndAccessToken(LocalDateTime.now(Clock.systemUTC()), accessToken)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(TrainingInstance.class, "accessToken", accessToken.getClass(), accessToken,
                        "There is no active training session matching access token.")));
    }
}
