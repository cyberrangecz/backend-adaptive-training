package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * The JPA repository interface to manage {@link User} instances.
 */
@Repository
public interface UserRefRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, UserRefRepositoryCustom {

    /**
     * Find all users by userRefIds.
     *
     * @param userRefId the user ref id
     * @return the set of {@link User}
     */
    Set<User> findUsers(@Param("userRefId") Set<Long> userRefId);

    /**
     * Find user by user ref id.
     *
     * @param userRefId the user id
     * @return the {@link User}
     */
    Optional<User> findUserByUserRefId(@Param("userRefId") Long userRefId);

    /**
     * Find all participants of given training instance.
     *
     * @param trainingInstanceId id of the training instance
     * @return the {@link User}
     */
    Set<Long> findParticipantsRefByTrainingInstanceId(@Param("trainingInstanceId") Long trainingInstanceId);

}
