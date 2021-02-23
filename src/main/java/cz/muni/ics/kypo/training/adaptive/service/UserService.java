package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.UserRef;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.enums.RoleType;
import cz.muni.ics.kypo.training.adaptive.exceptions.CustomWebClientException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.exceptions.MicroserviceApiException;
import cz.muni.ics.kypo.training.adaptive.repository.UserRefRepository;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Collections;
import java.util.Set;

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
     * @return {@link UserRef} with corresponding login
     * @throws EntityNotFoundException UserRef was not found
     */
    public UserRef getUserByUserRefId(Long userRefId) {
        return userRefRepository.findUserByUserRefId(userRefId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(UserRef.class, "id", userRefId.getClass(), userRefId)));
    }

    /**
     * Create new user reference
     *
     * @param userRefToCreate user reference to be created
     * @return created {@link UserRef}
     */
    @TransactionalWO
    public UserRef createUserRef(UserRef userRefToCreate) {
        UserRef userRef = userRefRepository.save(userRefToCreate);
        LOG.info("User ref with user_ref_id: {} created.", userRef.getUserRefId());
        return userRef;
    }
}
