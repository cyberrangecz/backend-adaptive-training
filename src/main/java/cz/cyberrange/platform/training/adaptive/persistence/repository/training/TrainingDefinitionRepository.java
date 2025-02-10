package cz.cyberrange.platform.training.adaptive.persistence.repository.training;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.QTrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TDState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The JPA repository interface to manage {@link TrainingDefinition} instances.
 */
@Repository
public interface TrainingDefinitionRepository
        extends JpaRepository<TrainingDefinition, Long>, TrainingDefinitionRepositoryCustom, QuerydslPredicateExecutor<TrainingDefinition>, QuerydslBinderCustomizer<QTrainingDefinition> {

    /**
     * That method is used to make the query dsl string values case insensitive and also it supports partial matches in the database.
     *
     * @param querydslBindings
     * @param qTrainingDefinition
     */
    @Override
    default void customize(QuerydslBindings querydslBindings, QTrainingDefinition qTrainingDefinition) {
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            values.forEach(value -> predicate.and(path.containsIgnoreCase(value)));
            return Optional.ofNullable(predicate);
        });
    }

    /**
     * Find all training definitions
     *
     * @param predicate the predicate
     * @param pageable  the pageable
     * @return page of all {@link TrainingDefinition}
     */
    Page<TrainingDefinition> findAll(Predicate predicate, Pageable pageable);

    /**
     * Find all training definitions
     *
     * @param pageable the pageable
     * @return page of all {@link TrainingDefinition}
     */
    Page<TrainingDefinition> findAll(Pageable pageable);

    /**
     * Find all training definitions
     *
     * @param state    the state of training definition
     * @param pageable the pageable
     * @return page of all {@link TrainingDefinition}
     */
    Page<TrainingDefinition> findAllByState(@Param("state") TDState state, Pageable pageable);

    /**
     * Find all for designers and organizers unreleased page.
     *
     * @param userRefId the user ref id
     * @param pageable  the pageable
     * @return the page
     */
    Page<TrainingDefinition> findAllForDesigner(@Param("userRefId") Long userRefId, Pageable pageable);

    /**
     * Find training definition by id
     *
     * @param id the id of training definition
     * @return {@link TrainingDefinition}
     */
    Optional<TrainingDefinition> findById(Long id);

    @Query("SELECT td FROM AbstractPhase ap INNER JOIN ap.trainingDefinition td WHERE ap.id = :phaseId")
    Optional<TrainingDefinition> findByPhaseId(@Param("phaseId") Long phaseId);

    @Query("SELECT td FROM Task t INNER JOIN t.trainingPhase tp INNER JOIN tp.trainingDefinition td WHERE t.id = :taskId")
    Optional<TrainingDefinition> findByTaskId(@Param("taskId") Long taskId);

    /**
     * Find all definition played by user.
     *
     * @param userRefId the user ref id
     * @return the list of training definitions
     */
    List<TrainingDefinition> findAllPlayedByUser(@Param("userRefId") Long userRefId);

}
