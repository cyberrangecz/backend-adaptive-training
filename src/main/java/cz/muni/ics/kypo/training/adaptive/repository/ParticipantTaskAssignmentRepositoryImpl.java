package cz.muni.ics.kypo.training.adaptive.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Coalesce;
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
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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
                                                   tuple -> tuple.get(1, Long.class) ));
    }
}
