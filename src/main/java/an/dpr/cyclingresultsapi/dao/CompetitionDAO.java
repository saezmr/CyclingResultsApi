package an.dpr.cyclingresultsapi.dao;

import java.util.Date;
import java.util.List;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.domain.Competition;

public interface CompetitionDAO {

    List<Competition> getCompetitions(Date init, Long genderID, Long classID, CompetitionClass cc);

    List<Competition> getCompetitions(Date init, Date fin, Long genderID, Long classID, CompetitionClass cc);

    List<Competition> getCompetitionsBetweenDates(Date init, Date fin);
    
    List<Competition> getYearCompetitions(Integer year);
    
    List<Competition> getYearByClass(Integer year);

    Competition save(Competition Competition);

    void delete(Long CompetitionId);

    Competition findById(Long id);
    
    /**
     * En el caso de las carreras de un dia, devuelve el competitiono unico.
     * En el caso de las carreras por etapas, devuelve el elemento principal.
     * @param competitionId
     * @return
     */
    Competition getCompetition(Long competitionID, Long phase1ID);
    
    /**
     * Devuelve el listado de competitionos etapa y clasificacion general
     * TODO -> CLASIFICAICONES GENERALES ALTERNATIVAS (PUNTOS, MONTAñA...)
     * @param competitionId
     * @return
     */
    List<Competition> getStageCompetitions(Long competitionId);
    
}
