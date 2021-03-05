package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantTaskAssignmentRepository extends JpaRepository<ParticipantTaskAssignment, Long>, QuerydslPredicateExecutor<ParticipantTaskAssignment> {

    @Query("SELECT DISTINCT(p.task) FROM ParticipantTaskAssignment p")
    List<Task> findAllVisitedTasks();

    //TODO provide a SQL commands that returns all the relations between task transitions

}