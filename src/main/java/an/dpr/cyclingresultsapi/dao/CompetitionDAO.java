package an.dpr.cyclingresultsapi.dao;

import java.util.Date;
import java.util.List;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.domain.Competition;

public interface CompetitionDAO {

    List<Competition> getCompetitions(Date init, Long genderID, Long classID, CompetitionClass cc);

    List<Competition> getCompetitions(Date init, Date fin, Long genderID, Long classID, CompetitionClass cc);
    
    List<Competition> getCompetitions(Date init, Date fin, CompetitionType type);

    List<Competition> getCompetitionsBetweenDates(Date init, Date fin);
    
    List<Competition> getYearCompetitions(Integer year);
    
    Competition save(Competition Competition);

    void delete(Long CompetitionId);

    Competition findById(Long id);
    
    /**
     * En el caso de las carreras de un dia, devuelve el competitiono unico.
     * En el caso de las carreras por etapas, devuelve el elemento principal.
     * @param competitionId
     * @param eventID ->si hay varios tipos. Por ej TT y ruta.
     * @return
     */
    Competition getCompetition(Long competitionID, Long eventID, Long genderID, Long classID, Long phase1ID);
    
    /**
     * Para clasificaciones, que añaden el phaseClassificationID
     * @param competitionID
     * @param eventID
     * @param genderID
     * @param classID
     * @param phase1ID
     * @param phaseClassificationID
     * @param long1 
     * @return
     */
    Competition getCompetition(Long competitionID, Long eventID, Long editionId, Long genderID, Long classID, Long phase1ID, Long phaseClassificationID);
    
    /**
     * Devuelve el listado de competitionos etapa de una competi 
     * @param competitionId
     * @return
     */
    List<Competition> getCompetitionStages(Competition competition);
    
    /**
     * Devuelve el listado de clasificacion generales de una competi 
     * @param competition
     * 	Busca todas las clasificaciones. Buscando por competitionid, eventid, genderid, classid y phase1ID = 0
     * @return
     */
    List<Competition> getCompetitionClassifications(Competition competition);

    List<Competition> getCompetitionAllEditions(long parseLong);

    
}
