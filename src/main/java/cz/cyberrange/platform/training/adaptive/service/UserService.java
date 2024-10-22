package cz.cyberrange.platform.training.adaptive.service;

import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.persistence.repository.UserRefRepository;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRefRepository userRefRepository;
    private final UserManagementServiceApi userManagementServiceApi;

    /**
     * Instantiates a new User service.
     *
     * @param userRefRepository the user ref repository
     */
    public UserService(UserRefRepository userRefRepository,
                       UserManagementServiceApi userManagementServiceApi) {
        this.userRefRepository = userRefRepository;
        this.userManagementServiceApi = userManagementServiceApi;
    }

    /**
     * Finds specific User reference by login
     *
     * @param userRefId of wanted User reference
     * @return {@link User} with corresponding login
     * @throws EntityNotFoundException UserRef was not found
     */
    public User getUserByUserRefId(Long userRefId) {
        return userRefRepository.findUserByUserRefId(userRefId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(User.class, "id", userRefId.getClass(), userRefId)));
    }

    /**
     * If user reference with given user id does not exist, it is created and returned.
     * Otherwise, the existing one is returned.
     *
     * @param userRefId id of the referenced user
     * @return user reference with given referenced id
     */
    public User createOrGetUserRef(Long userRefId) {
        return userRefRepository.createOrGet(userRefId);
    }
    
}
