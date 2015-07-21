package an.dpr.cyclingresultsapi.dao;

import java.util.List;

import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;

public interface ResultRowDAO {

    ResultRow save(ResultRow rr);
    
    ResultRow findById(Long id);
    
    List<ResultRow> getResults(Competition competition);

    boolean competitionResultsExists(Competition competition);
}
