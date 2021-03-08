package cz.muni.ics.kypo.training.adaptive.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.QParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QAbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QTask;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QTrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.QTrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.QTrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.QTrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.LinkDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.PreProcessLink;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Training definition repository.
 */
@Repository
public class ParticipantTaskAssignmentRepositoryImpl extends QuerydslRepositorySupport implements ParticipantTaskAssignmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new Training definition repository.
     */
    public ParticipantTaskAssignmentRepositoryImpl() {
        super(ParticipantTaskAssignment.class);
    }

    @Override
    @Transactional
    public Set<LinkDTO> findAllTaskTransitions(Long trainingDefinitionId, Long trainingInstanceId) {
        Objects.requireNonNull(trainingInstanceId, "Input logged in user ID must not be null.");
        QParticipantTaskAssignment qParticipantTaskAssignment1 = new QParticipantTaskAssignment("participantTaskAssignment1");
        QTask task1 = new QTask("task1");
        QAbstractPhase abstractPhase1 = new QAbstractPhase("abstractPhase1");
        QTrainingRun trainingRun1 = new QTrainingRun("trainingRun1");
        QTrainingInstance trainingInstance1 = new QTrainingInstance("trainingInstance1");

        QParticipantTaskAssignment qParticipantTaskAssignment2 = new QParticipantTaskAssignment("participantTaskAssignment2");
        QTask task2 = new QTask("task2");
        QAbstractPhase abstractPhase2 = new QAbstractPhase("abstractPhase2");
        QTrainingRun trainingRun2 = new QTrainingRun("trainingRun2");
        QTrainingInstance trainingInstance2 = new QTrainingInstance("trainingInstance2");

        QTask task = new QTask("task");
        QTrainingPhase trainingPhase = new QTrainingPhase("trainingPhase");
        QTrainingDefinition trainingDefinition = new QTrainingDefinition("trainingDefinition");

        JPQLQuery<PreProcessLink> query = new JPAQueryFactory(entityManager)
                .select(Projections.constructor(PreProcessLink.class, task1.id, task2.id, Wildcard.count))
                .from(qParticipantTaskAssignment1)
                .join(qParticipantTaskAssignment1.task, task1)
                .join(qParticipantTaskAssignment1.abstractPhase, abstractPhase1)
                .join(qParticipantTaskAssignment1.trainingRun, trainingRun1)
                .join(trainingRun1.trainingInstance, trainingInstance1)
                .from(qParticipantTaskAssignment2)
                .join(qParticipantTaskAssignment2.task, task2)
                .join(qParticipantTaskAssignment2.abstractPhase, abstractPhase2)
                .join(qParticipantTaskAssignment2.trainingRun, trainingRun2)
                .join(trainingRun2.trainingInstance, trainingInstance2)
                .where(trainingInstance1.id.eq(trainingInstanceId)
                        .and(trainingInstance2.id.eq(trainingInstanceId))
                        .and(trainingRun1.id.eq(trainingRun2.id))
                        .and(abstractPhase1.order.add(1).eq(abstractPhase2.order)))
                .groupBy(abstractPhase1.id, abstractPhase2.id, task1.id, task2.id);

        AtomicInteger index = new AtomicInteger();
        Map<Long, Integer> taskOrderMap = new JPAQueryFactory(entityManager)
                .select(task.id)
                .from(task)
                .join(task.trainingPhase, trainingPhase)
                .join(trainingPhase.trainingDefinition, trainingDefinition)
                .where(trainingDefinition.id.eq(trainingDefinitionId))
                .orderBy(trainingPhase.order.asc(), task.order.asc())
                .fetch()
                .stream()
                .collect(Collectors.toMap(Function.identity(), taskId -> index.getAndIncrement()));

        return query.fetch().stream()
                .map(link -> new LinkDTO(taskOrderMap.get(link.getSourceTaskId()),
                                         taskOrderMap.get(link.getTargetTaskId()),
                                         link.getValue()))
                .collect(Collectors.toSet());
    }
}
