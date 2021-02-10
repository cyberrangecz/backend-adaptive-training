package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.InfoPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoPhaseRepository extends JpaRepository<InfoPhase, Long> {
}
