package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;


public interface UserRefRepositoryCustom {

    /**
     * Insert user reference if it does not exist in the database.
     *
     * @param userRefId the user reference id
     * @return the number of rows affected
     */
    @Modifying
    @Transactional
    User createOrGet(@Param("userRefId") Long userRefId);

}
