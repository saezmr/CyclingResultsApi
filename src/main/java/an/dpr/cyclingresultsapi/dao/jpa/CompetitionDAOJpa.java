package an.dpr.cyclingresultsapi.dao.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.Competition_;

public class CompetitionDAOJpa extends AbstractDaoJpa<Competition> implements CompetitionDAO  {
    
    public CompetitionDAOJpa() {
	super(Competition.class);
    }

    @Inject private EntityManager em;
    @Inject private Logger log;

    @Override
    public List<Competition> getCompetitions(Date init, Long sportID, Long genderID, Long classID, CompetitionClass cc) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate pdates = cb.greaterThan(from.<Date>get(Competition_.initDate), init);
	Predicate pSport = cb.equal(from.<Long>get(Competition_.sportID), sportID);
	Predicate pGender = cb.equal(from.<Long>get(Competition_.genderID), genderID);
	Predicate pRiderClass = cb.equal(from.<Long>get(Competition_.classID), classID);
	Predicate pCompetitionClass = cb.equal(from.<CompetitionClass>get(Competition_.competitionClass), cc);
	Predicate where = cb.and(pdates, pSport, pGender, pRiderClass, pCompetitionClass);
	query.where(where);
	Order order = cb.desc(from.get("initDate"));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    public List<Competition> getCompetitions(Date init, Date fin, Long sportID, Long genderID, Long classID,
	    CompetitionClass cc) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate pdates = cb.between(from.<Date>get(Competition_.initDate), init, fin);
	Predicate pSport = cb.equal(from.<Long>get(Competition_.sportID), sportID);
	Predicate pGender = cb.equal(from.<Long>get(Competition_.genderID), genderID);
	Predicate pRiderClass = cb.equal(from.<Long>get(Competition_.classID), classID);
	
	Predicate where;
	if (!CompetitionClass.ALL.equals(cc)){
	    Predicate pCompetitionClass = cb.equal(from.<CompetitionClass>get(Competition_.competitionClass), cc);
	    where = cb.and(pdates, pSport, pGender, pRiderClass, pCompetitionClass);
	} else {
	    Predicate pPhase1ID = cb.equal(from.<Long>get(Competition_.phase1ID), -1l);
	    where = cb.and(pdates, pSport, pGender, pRiderClass, pPhase1ID);
	}
	query.where(where);
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    public List<Competition> getCompetitions(Date init, Date fin, Long sportID, Long genderID, Long classID,
	    CompetitionType type) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate pdates = cb.between(from.<Date>get(Competition_.initDate), init, fin);
	Predicate pSport = cb.equal(from.<Long>get(Competition_.sportID), sportID);
	Predicate pGender = cb.equal(from.<Long>get(Competition_.genderID), genderID);
	Predicate pRiderClass = cb.equal(from.<Long>get(Competition_.classID), classID);
	Predicate pCompetitionType = cb.equal(from.<CompetitionType>get(Competition_.competitionType), type);
	Predicate where = cb.and(pdates, pSport, pGender, pRiderClass, pCompetitionType);
	query.where(where);
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;

    }

    @Override
    public List<Competition> getCompetitionsBetweenDates(Date init, Date fin) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate pDates1 = cb.between(from.<Date>get(Competition_.initDate), init, fin);
	Predicate pDates2 = cb.between(from.<Date>get(Competition_.finishDate), init, fin);
	Predicate where = cb.or(pDates1, pDates2);
	query.where(where);
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    public List<Competition> getYearCompetitions(Integer year) {
	Calendar cal = Calendar.getInstance();
	Date date = new Date(0);
	cal.setTime(date);
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.YEAR, year);
	Date init = cal.getTime();
	cal.add(Calendar.YEAR, 1);
	Date fin = cal.getTime();
	return  getCompetitionsBetweenDates(init, fin);
    }

    @Override
    public Competition save(Competition competition) {
	create(competition);
	return competition;
    }

    @Override
    public void delete(Long competitionId) {
	Competition competition = find(competitionId);
	remove(competition);
    }

    @Override
    public Competition getCompetition(Long competitionID, Long eventID, Long editionID, Long genderID, Long classID,
	    Long phase1ID) {
	Competition competition = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate[] andPredicates = {
		cb.equal(from.<Long>get(Competition_.competitionID), competitionID),
		cb.equal(from.<Long>get(Competition_.eventID), eventID),
		cb.equal(from.<Long>get(Competition_.editionID), editionID),
		cb.equal(from.<Long>get(Competition_.genderID), genderID),
		cb.equal(from.<Long>get(Competition_.classID), classID),
		cb.equal(from.<Long>get(Competition_.phase1ID), phase1ID)
		};
	
	Predicate where = cb.and(andPredicates);
	query.where(where);
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	competition = tq.getSingleResult();
	
	return competition;
    }

    @Override
    public Competition getCompetition(Long competitionID, Long eventID, Long editionID, Long genderID, Long classID,
	    Long phase1ID, Long phaseClassificationID) {
	Competition competition = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate[] andPredicates = {
		cb.equal(from.<Long>get(Competition_.competitionID), competitionID),
		cb.equal(from.<Long>get(Competition_.eventID), eventID),
		cb.equal(from.<Long>get(Competition_.editionID), editionID),
		cb.equal(from.<Long>get(Competition_.genderID), genderID),
		cb.equal(from.<Long>get(Competition_.classID), classID),
		cb.equal(from.<Long>get(Competition_.phase1ID), phase1ID),
		cb.equal(from.<Long>get(Competition_.phaseClassificationID), phaseClassificationID)
		};
	
	Predicate where = cb.and(andPredicates);
	query.where(where);
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	competition = tq.getSingleResult();
	
	return competition;
    }

    @Override
    public List<Competition> getCompetitionStages(Competition competition) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate[] andPredicates = {
		cb.equal(from.<Long>get(Competition_.competitionID), competition.getCompetitionID()),
		cb.equal(from.<Long>get(Competition_.eventID), competition.getEventID()),
		cb.equal(from.<Long>get(Competition_.editionID), competition.getEditionID()),
		cb.equal(from.<Long>get(Competition_.genderID), competition.getGenderID()),
		cb.equal(from.<Long>get(Competition_.classID), competition.getClassID()),
		cb.equal(from.<CompetitionType>get(Competition_.competitionType),CompetitionType.STAGE_STAGES)
		};
	query.where(cb.and(andPredicates));
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    public List<Competition> getCompetitionClassifications(Competition competition) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate[] andPredicates = {
		cb.equal(from.<Long>get(Competition_.competitionID), competition.getCompetitionID()),
		cb.equal(from.<Long>get(Competition_.eventID), competition.getEventID()),
		cb.equal(from.<Long>get(Competition_.editionID), competition.getEditionID()),
		cb.equal(from.<Long>get(Competition_.genderID), competition.getGenderID()),
		cb.equal(from.<Long>get(Competition_.classID), competition.getClassID()),
		cb.equal(from.<CompetitionType>get(Competition_.competitionType), CompetitionType.CLASSIFICATION_STAGES)
		};
	query.where(cb.and(andPredicates));
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    public List<Competition> getCompetitionAllEditions(long competitionID) {
	List<Competition> list = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<Competition> query = cb.createQuery(Competition.class);
	Root<Competition> from = query.from(Competition.class);
	
	query.select(from);
	Predicate[] andPredicates = {
		cb.equal(from.<Long>get(Competition_.competitionID), competitionID),
	};
	query.where(cb.and(andPredicates));
	Order order = cb.desc(from.get(Competition_.initDate));
	query.orderBy(order);
	TypedQuery<Competition> tq = em.createQuery(query);
	list = tq.getResultList();
	
	return list;
    }

    @Override
    protected EntityManager getEntityManager() {
	return em;
    }

}
