package an.dpr.cyclingresultsapi.dao.springdatajpa;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.cyclingresultsapi.dao.BasicDAO;
import an.dpr.cyclingresultsapi.dao.ResultRowDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;
import an.dpr.cyclingresultsapi.repository.ResultRowRepo;

public class ResultRowDAOSDJPA extends BasicDAO implements ResultRowDAO {

    private static final Logger log = LoggerFactory
	    .getLogger(ResultRowDAOSDJPA.class);
    @Autowired
    private ResultRowRepo repo;

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
	return repo.findByCompetition(competition);
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
    
    

}
