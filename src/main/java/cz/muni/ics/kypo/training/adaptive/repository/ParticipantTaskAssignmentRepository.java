package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantTaskAssignmentRepository extends JpaRepository<ParticipantTaskAssignment, Long>, QuerydslPredicateExecutor<ParticipantTaskAssignment> {
    //TODO provide a SQL commands that returns all the relations between task transitions
}