package an.dpr.cyclingresultsapi.dao.springdatajpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.dao.BasicDAO;
import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.repository.CompetitionRepo;
import an.dpr.cyclingresultsapi.util.Contracts;

/**
 * Implementacion Spring Data JPA para la persistencia de eventos
 * 
 * @author saez
 *
 */
public class CompetitionDAOSDJPA extends BasicDAO implements CompetitionDAO {

    private static final Logger log = LoggerFactory.getLogger(CompetitionDAOSDJPA.class);
    @Autowired
    private CompetitionRepo repo;

    @Override
    public Competition save(Competition comp) {
	return repo.save(comp);
    }

    @Override
    public void delete(Long id) {
	repo.delete(id);
    }

    @Override
    public Competition findById(Long id) {
	return repo.findOne(id);
    }
    
    @Override
    public Competition getCompetition(Long competitionId, Long eventID, Long genderID, Long classID, Long phase1ID) {
	List<Competition> list = repo.findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndPhase1ID(competitionId, 
		eventID, genderID, classID, phase1ID);
	if (list.size()>0){
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Competition getCompetition(Long competitionId, Long eventID, Long genderID, Long classID, Long phase1ID,
	    Long phaseClassificationID) {
	return repo.findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndPhase1IDAndPhaseClassificationID(
		competitionId, eventID, genderID, classID, phase1ID, phaseClassificationID);
    }

    @Override
    public List<Competition> getCompetitionsBetweenDates(Date init, Date fin) {
	return repo.findByInitDateBetween(init, fin);
    }

    @Override
    public List<Competition> getYearCompetitions(Integer year) {
	Calendar cal = Calendar.getInstance();
	Date date = new Date(0);
	cal.setTime(date);
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.YEAR, year);
	return repo.findByInitDateGreaterThan(cal.getTime());
    }

    @Override
    public List<Competition> getYearByClass(Integer year) {
	// TODO Auto-generated method stub
	throw new RuntimeException("Por implementar");
    }

    @Override
    public List<Competition> getCompetitions(Date time, Long genderID, Long classID, CompetitionClass cc) {
	if (CompetitionClass.ALL.equals(cc)){
	    log.debug("todos los tipso de competi");
	    return repo.findByInitDateGreaterThanAndGenderIDAndClassIDAndCompetitionClassIsNotNull(time, genderID, classID);
	} else {
	    log.debug("competis tipo "+cc);
	    return repo.findByInitDateGreaterThanAndGenderIDAndClassIDAndCompetitionClass(time, genderID, classID, cc);
	}
    }

    @Override
    public List<Competition> getCompetitions(Date init, Date fin, Long genderID, Long classID, CompetitionClass cc) {
	if (CompetitionClass.ALL.equals(cc)){
	    log.debug("todos los tipso de competi");
	    return repo.findByInitDateBetweenAndGenderIDAndClassID(init, fin, genderID, classID);
	} else {
	    log.debug("competis tipo "+cc);
	    return repo.findByInitDateBetweenAndGenderIDAndClassIDAndCompetitionClass(init, fin, genderID, classID, cc);
	}
    }

    @Override
    public List<Competition> getCompetitions(Date init, Date fin, CompetitionType type) {
	return repo.findByInitDateBetweenAndCompetitionType(init, fin, type);
    }

    @Override
    public List<Competition> getCompetitionStages(Competition competition) {
	return repo.findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndCompetitionType(competition.getCompetitionID(),
		competition.getEventID(), competition.getGenderID(), competition.getClassID(), 
		CompetitionType.STAGE_STAGES);
    }

    @Override
    public List<Competition> getCompetitionClassifications(Competition competition) {
	return repo.findByCompetitionIDAndEventIDAndGenderIDAndClassIDAndCompetitionType(competition.getCompetitionID(),
		competition.getEventID(), competition.getGenderID(), competition.getClassID(), 
		CompetitionType.CLASSIFICATION_STAGES);
    }

}
