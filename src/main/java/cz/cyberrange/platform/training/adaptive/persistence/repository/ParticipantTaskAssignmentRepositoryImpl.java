package cz.cyberrange.platform.training.adaptive.persistence.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.cyberrange.platform.training.adaptive.persistence.entity.ParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.PreProcessLink;
import cz.cyberrange.platform.training.adaptive.persistence.entity.QParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QAbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QTask;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QTrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.QTrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.QTrainingRun;
import cz.cyberrange.platform.training.adaptive.persistence.enums.TRState;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

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
    public List<PreProcessLink> findTaskTransitionsBetweenTwoPhases(Long trainingDefinitionId,
                                                                    Long trainingInstanceId,
                                                                    Long firstPhaseId,
                                                                    Long secondPhaseId) {
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

        JPQLQuery<PreProcessLink> query = new JPAQueryFactory(entityManager)
                .select(Projections.constructor(PreProcessLink.class, task1.id, task2.id, Wildcard.count, abstractPhase1.order, abstractPhase2.order))
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
                        .and(abstractPhase1.id.eq(firstPhaseId))
                        .and(abstractPhase2.id.eq(secondPhaseId)))
                .groupBy(abstractPhase1.id, abstractPhase2.id, task1.id, task2.id);

        return query.fetch();
    }

    @Override
    @Transactional
    public Map<Long, Long> findNumberOfParticipantsInTasksOfPhase(Long phaseId) {
        Objects.requireNonNull(phaseId, "Input logged in user ID must not be null.");
        QTask currentTask = new QTask("currentTask");
        QTrainingPhase trainingPhase = new QTrainingPhase("trainingPhase");
        QTrainingRun trainingRun = new QTrainingRun("trainingRun");

        return new JPAQueryFactory(entityManager)
                .select(currentTask.id, Wildcard.count)
                .from(trainingRun)
                .join(trainingRun.currentTask, currentTask)
                .join(currentTask.trainingPhase, trainingPhase)
                .where(trainingPhase.id.eq(phaseId)
                        .and(trainingRun.state.eq(TRState.RUNNING)))
                .groupBy(currentTask.id)
                .fetch()
                .stream().collect(Collectors.toMap(tuple -> tuple.get(0, Long.class),
                        tuple -> tuple.get(1, Long.class)));
    }

    @Override
    @Transactional
    public Map<Long, List<ParticipantTaskAssignment>> findAllByTrainingInstanceAndGroupedByTrainingRun(Long trainingInstanceId) {
        Objects.requireNonNull(trainingInstanceId, "Input training instance ID must not be null.");
        QParticipantTaskAssignment qParticipantTaskAssignment = new QParticipantTaskAssignment("participantTaskAssignment");
        QTrainingRun qTrainingRun = new QTrainingRun("trainingRun");
        QTrainingInstance qTrainingInstance = new QTrainingInstance("trainingInstance");

        return new JPAQueryFactory(entityManager)
                .select(qTrainingRun.id, qParticipantTaskAssignment)
                .from(qParticipantTaskAssignment)
                .join(qParticipantTaskAssignment.trainingRun, qTrainingRun)
                .join(qTrainingRun.trainingInstance, qTrainingInstance)
                .where(qTrainingInstance.id.eq(trainingInstanceId)
                        .and(qTrainingRun.state.eq(TRState.FINISHED)))
                .transform(groupBy(qTrainingRun.id).as(list(qParticipantTaskAssignment)));
    }

    @Override
    @Transactional
    public List<ParticipantTaskAssignment> findAllByTrainingRun(Long trainingRunId) {
        Objects.requireNonNull(trainingRunId, "Input training run ID must not be null.");
        QParticipantTaskAssignment qParticipantTaskAssignment = new QParticipantTaskAssignment("participantTaskAssignment");
        QTrainingRun qTrainingRun = new QTrainingRun("trainingRun");

        return new JPAQueryFactory(entityManager)
                .select(qParticipantTaskAssignment)
                .from(qParticipantTaskAssignment)
                .join(qParticipantTaskAssignment.trainingRun, qTrainingRun)
                .where(qTrainingRun.id.eq(trainingRunId)
                        .and(qTrainingRun.state.eq(TRState.FINISHED)))
                .fetch();
    }
}
