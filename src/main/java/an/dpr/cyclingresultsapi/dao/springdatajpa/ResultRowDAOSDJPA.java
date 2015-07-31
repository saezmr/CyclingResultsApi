package an.dpr.cyclingresultsapi.dao.springdatajpa;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.cyclingresultsapi.dao.BasicDAO;
import an.dpr.cyclingresultsapi.dao.ResultRowDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;
import an.dpr.cyclingresultsapi.repository.CompetitionRepo;
import an.dpr.cyclingresultsapi.repository.ResultRowRepo;

public class ResultRowDAOSDJPA extends BasicDAO implements ResultRowDAO {

    private static final Logger log = LoggerFactory
	    .getLogger(ResultRowDAOSDJPA.class);
    @Autowired
    private ResultRowRepo repo;
    @Autowired
    private CompetitionRepo compRepo;

    @Override
    public ResultRow save(ResultRow rr) {
	return repo.save(rr);
    }

    @Override
    public ResultRow findById(Long id) {
	return repo.findOne(id);
    }

    @Override
    public List<ResultRow> getResults(Competition competition) {
	List<ResultRow> list = repo.findByCompetition(competition);
	Collections.sort(list);
	return list;
    }

    @Override
    public boolean competitionResultsExists(Competition competition) {
	List<ResultRow> list = getResults(competition);
	return list.size()>0;
    }

    @Override
    public boolean resultRowExists(ResultRow rr) {
	ResultRow result = repo.findByRankAndCompetition(rr.getRank(), rr.getCompetition());
	return result != null;
    }

    @Override
    public void deleteCompetitionRows(Competition comp) {
	//primero buscamos la row  para tener el id
	Competition findCompetition = compRepo.findByCompetitionIDAndEventIDAndEditionIDAndGenderIDAndClassIDAndPhase1IDAndPhaseClassificationID(
		comp.getCompetitionID(), comp.getEventID(), comp.getEditionID(), comp.getGenderID(),
		comp.getClassID(), comp.getPhase1ID(), comp.getPhaseClassificationID());
	//luego borramos todos los resultrow con ese id
	repo.deleteByCompetition(findCompetition);
    }
    
    

}
