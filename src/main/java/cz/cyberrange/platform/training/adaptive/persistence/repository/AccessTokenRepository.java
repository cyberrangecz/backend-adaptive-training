package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The JPA repository interface to manage {@link AccessToken} instances.
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long>, QuerydslPredicateExecutor<AccessToken> {

    /**
     * Find access token entity by its token string.
     *
     * @param accessToken the token string
     * @return the {@link AccessToken} with corresponding token string
     */
    Optional<AccessToken> findOneByAccessToken(@Param("accessToken") String accessToken);
}
