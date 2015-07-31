package an.dpr.cyclingresultsapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;
/**
 */
public interface ResultRowRepo  extends CrudRepository<ResultRow, Long> {

    @Query("FROM ResultRow r WHERE r.competition=:competition")
    List<ResultRow> findByCompetition(@Param("competition") Competition competition);

    @Query("FROM ResultRow r WHERE r.rank=:rank AND r.competition=:competition")
    ResultRow findByRankAndCompetition(
	    @Param("rank") String rank, 
	    @Param("competition") Competition competition);

    @Modifying  
    @Transactional
    @Query("DELETE FROM ResultRow r WHERE r.competition=:competition")
    void deleteByCompetition(@Param("competition") Competition competition);
}
