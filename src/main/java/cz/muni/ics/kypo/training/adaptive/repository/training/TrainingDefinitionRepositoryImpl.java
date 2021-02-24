package cz.muni.ics.kypo.training.adaptive.repository.training;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.muni.ics.kypo.training.adaptive.domain.QUser;
import cz.muni.ics.kypo.training.adaptive.domain.training.QTrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

/**
 * The type Training definition repository.
 */
@Repository
public class TrainingDefinitionRepositoryImpl extends QuerydslRepositorySupport implements TrainingDefinitionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new Training definition repository.
     */
    public TrainingDefinitionRepositoryImpl() {
        super(TrainingDefinition.class);
    }

    @Override
    @Transactional
    public Page<TrainingDefinition> findAll(Predicate predicate, Pageable pageable, Long loggedInUserId) {
        Objects.requireNonNull(loggedInUserId, "Input logged in user ID must not be null.");
        QTrainingDefinition trainingDefinition = QTrainingDefinition.trainingDefinition;
        QUser authors = new QUser("authors");

        JPQLQuery<TrainingDefinition> query = new JPAQueryFactory(entityManager).selectFrom(trainingDefinition).distinct()
                .leftJoin(trainingDefinition.authors, authors)
                .where(authors.userRefId.eq(loggedInUserId));

        if (predicate != null) {
            query.where(predicate);
        }
        return getPage(query, pageable);
    }

    private <T> Page getPage(JPQLQuery<T> query, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 20);
        }
        query = getQuerydsl().applyPagination(pageable, query);
        long count = query.fetchCount();
        return new PageImpl<>(query.fetch(), pageable, count);
    }
}
