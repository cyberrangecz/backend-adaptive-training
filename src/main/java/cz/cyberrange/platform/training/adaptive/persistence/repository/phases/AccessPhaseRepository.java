package cz.cyberrange.platform.training.adaptive.persistence.repository.phases;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessPhaseRepository extends JpaRepository<AccessPhase, Long>, QuerydslPredicateExecutor<AccessPhase> {

}
