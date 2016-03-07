package an.dpr.cyclingresultsapi.dao.jpa;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.cdi.interceptor.Logged;
import an.dpr.cyclingresultsapi.dao.AbstractDao;
import an.dpr.cyclingresultsapi.dao.ResultRowDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;
import an.dpr.cyclingresultsapi.domain.ResultRow_;

@Logged
public class ResultRowDAOJpa extends AbstractDao<ResultRow> implements ResultRowDAO{
    
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
	// TODO Auto-generated method stub
	log.error("Por implementar!!");
	return false;
    }

    @Override
    public boolean resultRowExists(ResultRow rr) {
	// TODO Auto-generated method stub
	log.error("Por implementar!!");
	return false;
    }

    @Override
    public void deleteCompetitionRows(Competition comp) {
	// TODO Auto-generated method stub
	log.error("Por implementar!!");
	
    }

    @Override
    protected EntityManager getEntityManager() {
	return em;
    }

}
