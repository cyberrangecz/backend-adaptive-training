package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.UserRefRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
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
     * @param userRefRepository              the user ref repository
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
     * Create new user reference
     *
     * @param userToCreate user reference to be created
     * @return created {@link User}
     */
    @TransactionalWO
    public User createUserRef(User userToCreate) {
        User user = userRefRepository.save(userToCreate);
        LOG.info("User ref with user_ref_id: {} created.", user.getUserRefId());
        return user;
    }
}
