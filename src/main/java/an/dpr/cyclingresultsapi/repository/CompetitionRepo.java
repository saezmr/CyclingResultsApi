package an.dpr.cyclingresultsapi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.domain.Competition;

/**
 * @author saez
 *
 */
public interface CompetitionRepo  extends CrudRepository<Competition, Long> {

    
    List<Competition> findByInitDateGreaterThanAndGenderIDAndClassID(Date time, Long genderID, Long classID);

    List<Competition> findByInitDateBetweenAndGenderIDAndClassIDAndPhase1IDAndCompetitionClassIsNotNullOrderByInitDateDesc(Date init, Date fin, Long genderID, Long classID, Long phase1ID);

    List<Competition> findByInitDateGreaterThanAndGenderIDAndClassIDAndCompetitionClass(Date time, Long genderID, Long classID, CompetitionClass cc);
    
    List<Competition> findByInitDateBetweenAndGenderIDAndClassIDAndCompetitionClass(Date init, Date fin, Long genderID, Long classID, CompetitionClass cc);

    List<Competition> findByInitDateGreaterThan(Date time);
    
    List<Competition> findByInitDateBetween(Date init, Date fin);

    List<Competition> findByCompetitionID(Long competitionID);

    List<Competition> findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndPhase1ID(
	    Long competitionId, Long eventID, Long genderID, Long classID,
	    Long phase1id);

    List<Competition> findByInitDateBetweenAndCompetitionType(Date init, Date fin, CompetitionType type);

    Competition findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndPhase1IDAndPhaseClassificationID(
	    Long competitionId, Long eventID, Long genderID, Long classID, Long phase1id, Long phaseClassificationID);

    List<Competition> findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndCompetitionType(Long competitionID,
	    Long eventID, Long genderID, Long classID, CompetitionType type);

    List<Competition> findByInitDateGreaterThanAndGenderIDAndClassIDAndPhase1IDAndCompetitionClassIsNotNullOrderByInitDateDesc(Date time,
	    Long genderID, Long classID, Long phase1ID);

    Competition findByCompetitionIDAndEventIDAndEditionIDAndGenderIDAndClassIDAndPhase1IDAndPhaseClassificationID(
	    Long competitionId, Long eventID, Long editionID, Long genderID, Long classID, Long phase1id,
	    Long phaseClassificationID);

    List<Competition> findByCompetitionIDAndEventIDAndEditionIDAndGenderIDAndClassIDAndCompetitionType(
	    Long competitionID, Long eventID, Long editionID, Long genderID, Long classID,
	    CompetitionType classificationStages);

    List<Competition> findByCompetitionIDAndEventIDAndEditionIDAndGenderIDAndClassIDAndCompetitionTypeOrderByInitDateDesc(
	    Long competitionID, Long eventID, Long editionID, Long genderID, Long classID,
	    CompetitionType classificationStages);

    List<Competition> findByCompetitionIDAndEventIDAndEditionIDAndGenderIDAndClassIDAndPhase1ID(Long competitionId,
	    Long eventID, Long editionID, Long genderID, Long classID, Long phase1id);
    
}
