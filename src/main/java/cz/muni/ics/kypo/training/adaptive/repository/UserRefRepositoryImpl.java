package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserRefRepositoryImpl implements UserRefRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(UserRefRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createOrGet(Long userRefId) {
        int rowsAffected = entityManager.createNativeQuery("INSERT INTO \"user\" (user_ref_id) VALUES (:userRefId)" +
                        "ON CONFLICT DO NOTHING")
                .setParameter("userRefId", userRefId)
                .executeUpdate();

        if (rowsAffected != 0) {
            LOG.info("User with user_ref_id {} created", userRefId);
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaBuilder.and(criteriaBuilder.equal(root.get("userRefId"), userRefId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }


}
