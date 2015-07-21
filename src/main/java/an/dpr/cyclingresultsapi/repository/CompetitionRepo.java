package an.dpr.cyclingresultsapi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import an.dpr.cyclingresultsapi.domain.Competition;

public interface CompetitionRepo  extends CrudRepository<Competition, Long> {

    List<Competition> findByInitDateGreaterThan(Date time);

    List<Competition> findByInitDateBetween(Date init, Date fin);

    Competition findByCompetitionIDAndPhase1ID(Long competitionID, Long phase1ID);

    List<Competition> findByCompetitionID(Long competitionID);
    
}
