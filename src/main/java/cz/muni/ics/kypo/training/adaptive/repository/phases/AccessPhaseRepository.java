package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AccessPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessPhaseRepository extends JpaRepository<AccessPhase, Long>, QuerydslPredicateExecutor<AccessPhase> {

}
