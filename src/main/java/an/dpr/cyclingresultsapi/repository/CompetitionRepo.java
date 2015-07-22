package an.dpr.cyclingresultsapi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.domain.Competition;

/**
 *  * TODO LIST:
 * 	-find by gender (male, female)
 * 	-find by class (elite, sub23..)
 * 	-find by competitionClass (WT, hc1, 1.1...) 
 * @author saez
 *
 */
public interface CompetitionRepo  extends CrudRepository<Competition, Long> {

    
    List<Competition> findByInitDateGreaterThanAndGenderIDAndClassID(Date time, Long genderID, Long classID);

    List<Competition> findByInitDateBetweenAndGenderIDAndClassID(Date init, Date fin, Long genderID, Long classID);

    List<Competition> findByInitDateGreaterThanAndGenderIDAndClassIDAndCompetitionClass(Date time, Long genderID, Long classID, CompetitionClass cc);
    
    List<Competition> findByInitDateBetweenAndGenderIDAndClassIDAndCompetitionClass(Date init, Date fin, Long genderID, Long classID, CompetitionClass cc);

    List<Competition> findByInitDateGreaterThan(Date time);
    
    List<Competition> findByInitDateBetween(Date init, Date fin);

    Competition findByCompetitionIDAndPhase1ID(Long competitionID, Long phase1ID);

    List<Competition> findByCompetitionID(Long competitionID);
    
}
