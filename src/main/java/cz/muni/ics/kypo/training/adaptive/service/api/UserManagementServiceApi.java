package cz.muni.ics.kypo.training.adaptive.service.api;

import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.enums.RoleType;
import cz.muni.ics.kypo.training.adaptive.exceptions.CustomWebClientException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.exceptions.MicroserviceApiException;
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
 * The type User Management Api.
 */
@Service
public class UserManagementServiceApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserManagementServiceApi.class);
    private final WebClient userManagementServiceWebClient;

    /**
     * Instantiates a new User Management Api.
     *
     * @param userManagementServiceWebClient the rest template
     */
    public UserManagementServiceApi(@Qualifier("userManagementServiceWebClient") WebClient userManagementServiceWebClient) {
        this.userManagementServiceWebClient = userManagementServiceWebClient;
    }

    /**
     * Finds specific User reference by login
     *
     * @param id of wanted User reference
     * @return {@link User} with corresponding login
     * @throws EntityNotFoundException UserRef was not found
     */
    public UserRefDTO getUserRefDTOByUserRefId(Long id) {
        try {
            return userManagementServiceWebClient
                    .get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .bodyToMono(UserRefDTO.class)
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to obtain info about user(ID: " + id + ").", ex);
        }
    }

    /**
     * Gets users with given user ref ids.
     *
     * @param userRefIds the user ref ids
     * @param pageable   pageable parameter with information about pagination.
     * @param givenName  optional parameter used for filtration
     * @param familyName optional parameter used for filtration
     * @return the users with given user ref ids
     */
    public PageResultResource<UserRefDTO> getUserRefDTOsByUserIds(Set<Long> userRefIds, Pageable pageable, String givenName, String familyName) {
        if (userRefIds.isEmpty()) {
            return new PageResultResource<>(Collections.emptyList(), new PageResultResource.Pagination(0, 0, pageable.getPageSize(), 0, 0));
        }
        try {
            return userManagementServiceWebClient
                    .get()
                    .uri(uriBuilder -> {
                                uriBuilder
                                        .path("/users/ids")
                                        .queryParam("ids", StringUtils.collectionToDelimitedString(userRefIds, ","));
                                this.setCommonParams(givenName, familyName, pageable, uriBuilder);
                                return uriBuilder.build();
                            }
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<PageResultResource<UserRefDTO>>() {
                    })
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to obtain users by IDs: " + userRefIds + ".", ex);
        }
    }

    /**
     * Finds all logins of users that have role of designer
     *
     * @param roleType   the role type
     * @param pageable   pageable parameter with information about pagination.
     * @param givenName  optional parameter used for filtration
     * @param familyName optional parameter used for filtration
     * @return list of users with given role
     */
    public PageResultResource<UserRefDTO> getUserRefsByRole(RoleType roleType, Pageable pageable, String givenName, String familyName) {
        try {
            return userManagementServiceWebClient
                    .get()
                    .uri(uriBuilder -> {
                                uriBuilder
                                        .path("/roles/users")
                                        .queryParam("roleType", roleType.name());
                                this.setCommonParams(givenName, familyName, pageable, uriBuilder);
                                return uriBuilder.build();
                            }
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<PageResultResource<UserRefDTO>>() {
                    })
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to obtain users with role " + roleType.name() + ".", ex);
        }
    }

    /**
     * Finds all logins of users that have role of designer
     *
     * @param roleType   the role type
     * @param userRefIds ids of the users who should be excluded from the result set.
     * @param pageable   the pageable
     * @param givenName  optional parameter used for filtration
     * @param familyName optional parameter used for filtration
     * @return list of users with given role
     */
    public PageResultResource<UserRefDTO> getUserRefsByRoleAndNotWithIds(RoleType roleType, Set<Long> userRefIds, Pageable pageable, String givenName, String familyName) {
        try {
            return userManagementServiceWebClient
                    .get()
                    .uri(uriBuilder -> {
                                uriBuilder
                                        .path("/roles/users-not-with-ids")
                                        .queryParam("roleType", roleType.name())
                                        .queryParam("ids", StringUtils.collectionToDelimitedString(userRefIds, ","));
                                this.setCommonParams(givenName, familyName, pageable, uriBuilder);
                                return uriBuilder.build();
                            }
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<PageResultResource<UserRefDTO>>() {
                    })
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to obtain users with role " + roleType.name() + " and IDs: " + userRefIds + ".", ex);
        }
    }

    /**
     * Gets user ref id from user and group.
     *
     * @return the user ref id from user and group
     */
    public Long getLoggedInUserRefId() {
        try {
            UserRefDTO userRefDTO = userManagementServiceWebClient
                    .get()
                    .uri("/users/info")
                    .retrieve()
                    .bodyToMono(UserRefDTO.class)
                    .block();
            return userRefDTO.getUserRefId();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to get info about logged in user.", ex);
        }
    }

    /**
     * Gets user ref id from user and group.
     *
     * @return the user ref id from user and group
     */
    public UserRefDTO getUserRefDTO() {
        try {
            UserRefDTO userRefDTO = userManagementServiceWebClient
                    .get()
                    .uri("/users/info")
                    .retrieve()
                    .bodyToMono(UserRefDTO.class)
                    .block();
            return userRefDTO;
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Error when calling user management service API to get info about logged in user.", ex);
        }
    }

    private void setCommonParams(String givenName, String familyName, Pageable pageable, UriBuilder builder) {
        if (givenName != null) {
            builder.queryParam("givenName", givenName);
        }
        if (familyName != null) {
            builder.queryParam("familyName", familyName);
        }
        builder.queryParam("page", pageable.getPageNumber());
        builder.queryParam("size", pageable.getPageSize());
    }
}
