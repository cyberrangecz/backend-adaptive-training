package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.InfoPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoPhaseRepository extends JpaRepository<InfoPhase, Long>, QuerydslPredicateExecutor<InfoPhase> {
}
