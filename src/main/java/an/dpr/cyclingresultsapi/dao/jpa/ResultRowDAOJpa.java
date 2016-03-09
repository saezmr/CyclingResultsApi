package an.dpr.cyclingresultsapi.dao.jpa;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.cdi.interceptor.Logged;
import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.dao.ResultRowDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.Competition_;
import an.dpr.cyclingresultsapi.domain.ResultRow;
import an.dpr.cyclingresultsapi.domain.ResultRow_;

@Logged
public class ResultRowDAOJpa extends AbstractDaoJpa<ResultRow> implements ResultRowDAO{
    
    @Inject private CompetitionDAO competitionDao;
    
    public ResultRowDAOJpa() {
	super(ResultRow.class);
    }

    @Inject private EntityManager em;
    @Inject private Logger log;

    @Override
    public ResultRow save(ResultRow rr) {
	create(rr);
	return rr;
    }

    @Override
    public ResultRow findById(Long id) {
	return find(id);
    }

    @Override
    public List<ResultRow> getResults(Competition competition) {
	List<ResultRow> results = null;
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<ResultRow> query = cb.createQuery(ResultRow.class);
	Root<ResultRow> from = query.from(ResultRow.class);
	
	query.where(cb.equal(from.<Competition>get(ResultRow_.competition), competition));
	
	TypedQuery<ResultRow> tq = em.createQuery(query);
	results = tq.getResultList();
	Collections.sort(results);
	return results;
    }

    @Override
    public boolean competitionResultsExists(Competition competition) {
	List<ResultRow> list = getResults(competition);
	return list.size()>0;
    }

    @Override
    public boolean resultRowExists(ResultRow rr) {
	//TODO MODIFICAR ESTO, rank no es PK, puede haber varios DNF, DQS...
	//ResultRow result = repo.findByRankAndCompetition(rr.getRank(), rr.getCompetition());
	return false;//result != null;
    }

    @Override
    public void deleteCompetitionRows(Competition comp) {
	//primero buscamos la row  para tener el id
	Competition findCompetition = competitionDao.getCompetition(
		comp.getCompetitionID(), comp.getEventID(), comp.getEditionID(), comp.getGenderID(),
		comp.getClassID(), comp.getPhase1ID(), comp.getPhaseClassificationID());
	//luego borramos todos los resultrow con ese id
	deleteCompetitionRows(findCompetition);
    }
   
    @Override
    protected EntityManager getEntityManager() {
	return em;
    }

}
