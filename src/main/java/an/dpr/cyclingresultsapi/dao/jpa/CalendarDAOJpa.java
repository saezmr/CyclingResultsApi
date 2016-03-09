package an.dpr.cyclingresultsapi.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.cdi.interceptor.Logged;
import an.dpr.cyclingresultsapi.dao.CalendarDAO;
import an.dpr.cyclingresultsapi.domain.CalendarEvent;
import an.dpr.cyclingresultsapi.domain.CalendarEvent_;


@Logged
public class CalendarDAOJpa extends AbstractDaoJpa<CalendarEvent> implements CalendarDAO {
    
    @Inject private Logger log;
    @Inject private EntityManager em;

    public CalendarDAOJpa() {
	super(CalendarEvent.class);
    }

    @Override
    protected EntityManager getEntityManager() {
	return em;
    }

    @Override
    public CalendarEvent find(Long id) {
	return find(id);
    }

    @Override
    public List<CalendarEvent> query(CalendarEvent event) {
	if (event == null)
	    throw new IllegalArgumentException("The filter must be informed");
	
	CriteriaBuilder builder = em.getCriteriaBuilder();
	CriteriaQuery<CalendarEvent> query = builder.createQuery(CalendarEvent.class);
	Root<CalendarEvent> from = query.from(CalendarEvent.class);
	
	List<Predicate> filters = new ArrayList<Predicate>();
	
	Predicate initDate =builder.between(from.<Date>get(CalendarEvent_.initDate), event.getInitDate(), event.getFinishDate()); 
	Predicate finishDate =builder.between(from.<Date>get(CalendarEvent_.finishDate), event.getInitDate(), event.getFinishDate());
	filters.add(builder.or(initDate, finishDate));

	if(event.getName()!=null)
	    filters.add(builder.like(from.<String>get(CalendarEvent_.name), "%"+event.getName()+"%"));
	if(event.getCategory()!=null)
	    filters.add(builder.equal(from.<String>get(CalendarEvent_.category), event.getCategory()));
	if(event.getCountry()!=null)
	    filters.add(builder.equal(from.<String>get(CalendarEvent_.country), event.getCountry()));
	if(event.getEventClass()!=null)
	    filters.add(builder.equal(from.<String>get(CalendarEvent_.eventClass), event.getEventClass()));
	
	Predicate[] filtersA = new Predicate[filters.size()];
	filters.toArray(filtersA);
	query.where(builder.and(filtersA));
	
	TypedQuery<CalendarEvent> tq = em.createQuery(query);
	try{
	    List<CalendarEvent> list = tq.getResultList();
	    return list;
	} catch(NoResultException e){
	    return null;
	}
    }
    
    @Override
    public boolean exists(CalendarEvent event){
	CriteriaBuilder builder = em.getCriteriaBuilder();
	CriteriaQuery<CalendarEvent> query = builder.createQuery(CalendarEvent.class);
	Root<CalendarEvent> from = query.from(CalendarEvent.class);
	
	Predicate initDate = builder.equal(from.<Date>get(CalendarEvent_.initDate), event.getInitDate());
	Predicate finishDate = builder.equal(from.<Date>get(CalendarEvent_.finishDate), event.getFinishDate());
	Predicate name = builder.equal(from.<String>get(CalendarEvent_.name), event.getName());
	Predicate category = builder.equal(from.<String>get(CalendarEvent_.category), event.getCategory());
	Predicate country = builder.equal(from.<String>get(CalendarEvent_.country), event.getCountry());
	Predicate eventClass = builder.equal(from.<String>get(CalendarEvent_.eventClass), event.getEventClass());
	
	query.where(builder.and(initDate, finishDate, name, category, country, eventClass));
	
	TypedQuery<CalendarEvent> tq = em.createQuery(query);
	try{
	    CalendarEvent c = tq.getSingleResult();
	    return true;
	} catch(NonUniqueResultException e){
	    List<CalendarEvent> list = tq.getResultList();
	    log.debug("filtro:"+event);
	    log.debug(list.toString());
	    return true;
	} catch(NoResultException e){
	    return false;
	}
    }

}
