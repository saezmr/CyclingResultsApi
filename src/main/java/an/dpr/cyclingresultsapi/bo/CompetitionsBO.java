package an.dpr.cyclingresultsapi.bo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.exception.CyclingResultsException;
import an.dpr.cyclingresultsapi.util.Contracts;
import an.dpr.cyclingresultsapi.util.DateUtil;
import an.dpr.cyclingresultsapi.util.NetworkUtils;
import an.dpr.cyclingresultsapi.util.Utils;

/**
 * Business Object for competitions
 * @author saez
 *
 */
public class CompetitionsBO {


    private static final Logger log = LoggerFactory.getLogger(CompetitionsBO.class);
    
    @Autowired
    private CompetitionDAO dao;

    public List<Competition> getCompetitions(String initDate,String finDate,String genderID,String classID,String competitionClass){
	List<Competition> list = null;
	try {
	    Date id = getDate(initDate, false);
	    Date fd = getDate(finDate, true);
	    if (fd == null){
		list = dao.getCompetitions(id, getGenderID(genderID), getClassID(classID), getCompetitionClass(competitionClass));
	    } else {
		list = dao.getCompetitions(id, fd, getGenderID(genderID), getClassID(classID), getCompetitionClass(competitionClass));
	    }
	} catch (ParseException e) {
	    log.error("Error obteniendo competiciones", e);
	}
	return list;
    }
    
    private CompetitionClass getCompetitionClass(String competitionClass) {
	CompetitionClass ret = CompetitionClass.get(competitionClass);
	if (ret == null){
	    ret = CompetitionClass.ALL;
	}
	return ret;
    }

    private Long getClassID(String classID) {
	try{
	    return Long.parseLong(classID);
	} catch(NumberFormatException e){
	    return Contracts.DEFAULT_CLASS_ID;
	}
    }

    private Long getGenderID(String genderID) {
	try{
	    return Long.parseLong(genderID);
	} catch(NumberFormatException e){
	    return Contracts.DEFAULT_GENDER_ID;
	}
    }

    private Date getDate(String cadena, boolean nullable) throws ParseException {
	Date ret = null;
	if (cadena == null || cadena.isEmpty()) {
	    //fecha por defecto 1/1 del año actual
	    if (!nullable){
		ret = DateUtil.firstDayOfYear(new Date());
	    }
	    
	} else {
	    try {
		ret = DateUtil.parse(cadena, Contracts.DATE_FORMAT_SEARCH_COMPS);
	    } catch (CyclingResultsException e) {
		log.error("",e);
	    }
	}
	return ret;
    }

    public List<Competition> getNextCompetitions() {
	return dao.getYearCompetitions(2015);
    }

    public List<Competition> getYearCompetitions(String year) {
	return dao.getYearCompetitions(Integer.parseInt(year));
    }

    public List<Competition> getMonthCompetitions(String year,String month) {
	Calendar cal = Calendar.getInstance();
	Date date = new Date(0);
	cal.setTime(date);
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.YEAR, Integer.parseInt(year));
	cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
	Date init = cal.getTime();
	
	cal.add(Calendar.MONTH, 1);
	Date fin= cal.getTime();
	
	return dao.getCompetitionsBetweenDates(init, fin);
    }
    
    public List<Competition> getAllEditions(String competitionID){
	try{
	    return dao.getCompetitionAllEditions(Long.parseLong(competitionID));
	} catch(Exception e){
	    log.error("Error obteniendo ediciones de la competicion "+competitionID, e);
	    return new ArrayList<Competition>();
	}
    }
    
    public List<Competition> getStageRaceCompetitionsService(
	    String competitionID,
	    String eventID,
	    String editionID,
	    String genderID,
	    String classID
	    ) {
	Competition comp = new Competition.Builder()
		.setCompetitionID(Long.parseLong(competitionID)) 
		.setEventID(Long.parseLong(eventID))
		.setEditionID(Long.parseLong(editionID))
		.setGenderID(Long.parseLong(genderID)) 
		.setClassID(Long.parseLong(classID))
		.build();
	List<Competition> list = new ArrayList<Competition>();
	list.addAll(dao.getCompetitionClassifications(comp));
	list.addAll(dao.getCompetitionStages(comp));
	if (list.size() == 0){//esto no deberia ocurrir, pero por si aca
	    list = getStageRaceCompetitions(Long.parseLong(competitionID), Long.parseLong(eventID),
		    Long.parseLong(editionID), Long.parseLong(genderID), Long.parseLong(classID));
	    comp = dao.getCompetition(Long.parseLong(competitionID), 
			Long.parseLong(eventID), Long.parseLong(editionID), 
			Long.parseLong(genderID), Long.parseLong(classID), Long.valueOf(-1));
	    persisistStagesCompetitionDetails(comp, list);
	}
	return list;
	
    }
	
    /**
     * busca y obtiene de la web UCI las "competitions" o clasificaciones
     * @param competitionID
     * @param eventID
     * @param genderID
     * @param classID
     * @return
     */
    public List<Competition> getStageRaceCompetitions(Long competitionID,
	    Long eventID, Long editionID, Long genderID, Long classID ) {
	String htmlString = null;
	Competition competition = dao.getCompetition(competitionID,
		eventID, editionID, genderID, classID, (long)-1);
	try {
	    String url = getURLStageEvents(competition);
	    htmlString = NetworkUtils.getRequest(Contracts.BASE_URL_UCI, url);
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info ", e);
	} catch (IOException e) {
	    log.error("error leyendo info ", e);
	}
	log.debug(htmlString);
	if (htmlString != null){
	    return tratarXmlStageRaceCompetitions(htmlString, competition);
	} else {
	    return new ArrayList<Competition>();
	}
	 
    }

    private String getURLStageEvents(Competition competition) {
	StringBuilder sb = new StringBuilder();
	sb.append(Contracts.URL_STAGE_EVENTS_1);
	sb.append(Contracts.URL_STAGE_EVENT_DATA
		.replace(Contracts.SPORT_ID,competition.getSportID().toString())
		.replace(Contracts.COMPETITION_ID,competition.getCompetitionID().toString())
		.replace(Contracts.EDITION_ID,competition.getEditionID().toString())
		.replace(Contracts.SEASON_ID,competition.getSeasonID().toString())
		.replace(Contracts.CLASS_ID,competition.getClassID().toString())
		.replace(Contracts.GENDER_ID,competition.getGenderID().toString())
		.replace(Contracts.EVENT_ID,competition.getEventID().toString())
		.replace(Contracts.EVENT_PHASE_ID,competition.getEventPhaseID() !=null ? competition.getEventPhaseID().toString() : "-1")
		);
	
	return sb.toString();
    }

    private List<Competition> tratarXmlStageRaceCompetitions(String html, Competition competition) {
	Document doc = Jsoup.parse(html);
	Elements tableElements = doc.select("table.datatable");
	List<Competition> list = new ArrayList<Competition>();

	Elements tableRowElements = tableElements.select(":not(thead) tr");

	// Date Competition Nat. Class Winner
	for (int i = 0; i < tableRowElements.size(); i++) {
	    Element row = tableRowElements.get(i);
	    if (row.attr("valign").equals("top")) {// los que no tienen
						   // valing=top son headers
		Elements rowItems = row.select("td");
		Competition stage = new Competition.Builder()
			.setDates(rowItems.get(0).text().replace("\u00a0", "").trim())
			.setName(rowItems.get(1).text())
			.setWinner(rowItems.get(2).text())
			.setLeader(rowItems.get(3).text())
			.setPhase1ID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE1_ID_KEY))
			.setSportID(competition.getSportID())
			.setCompetitionID(competition.getCompetitionID())
			.setEventID(competition.getEventID())
			.setEventPhaseID(competition.getEventPhaseID())
			.setEditionID(competition.getEditionID())
			.setSeasonID(competition.getSeasonID())
			.setCompetitionID(competition.getCompetitionID())
			.setGenderID(competition.getGenderID())
			.setClassID(competition.getClassID())
			.build();
		list.add(stage);
	    }
	}
	return list;
    }
    
    private String getURLClassifications(Competition comp) {
   	StringBuilder sb = new StringBuilder();
   	sb.append(Contracts.CLASSIFICATIONS_URL
   		.replace(Contracts.COMPETITION_ID,
   			String.valueOf(comp.getCompetitionID()))
   		.replace(Contracts.EDITION_ID,
   			String.valueOf(comp.getEditionID()))
   		.replace(Contracts.EVENT_ID,
   			String.valueOf(comp.getEventID()))
   		.replace(Contracts.GENDER_ID,
   			String.valueOf(comp.getGenderID()))
   		.replace(Contracts.CLASS_ID,
   			String.valueOf(comp.getClassID()))
   		.replace(Contracts.PHASE_CLASSIFICATION_ID,
   			String.valueOf(-1))
   		.replace(Contracts.EVENT_PHASE_ID,
   			String.valueOf(comp.getEventPhaseID()))
   		);
   	return sb.toString();
    }

    /**
     * Last competitions finished or in course.
     * @param initDate and finishDate in format yyyymmdd
     * @return List<Competition>
     */
    public Boolean loadCompetitionsService(
	    String  genderID,
	    String classID,
	    String initDate,
	    String finishDate
	    ) {
	Boolean ret;
	try{
	    if ((genderID != null && !genderID.isEmpty()) && (classID != null && !classID.isEmpty())){
		loadCompetitions(genderID, classID, initDate, finishDate);
		log.info("competiciones genderID="+genderID+", classID="+classID+" cargadas con exito");
		ret = Boolean.TRUE;
	    } else if ((genderID == null || genderID.isEmpty()) && (classID == null || classID.isEmpty())){
		loadCompetitions(Contracts.MEN_GENDER_ID, Contracts.ELITE_CLASS_ID, initDate, finishDate);
		loadCompetitions(Contracts.WOMEN_GENDER_ID, Contracts.ELITE_CLASS_ID, initDate, finishDate);
		loadCompetitions(Contracts.MEN_GENDER_ID, Contracts.UNDER23_CLASS_ID, initDate, finishDate);
		loadCompetitions(Contracts.MEN_GENDER_ID, Contracts.JUNIOR_CLASS_ID, initDate, finishDate);
		loadCompetitions(Contracts.WOMEN_GENDER_ID, Contracts.JUNIOR_CLASS_ID, initDate, finishDate);
		ret = Boolean.TRUE;
	    } else {
		ret = Boolean.FALSE;
	    }
	    if (ret){
		//ahora cargaremos todas las "stage competitions" o clasificaicones internas de una prueba
		loadAndSaveStageCompetitions(initDate, finishDate);
	    }
	    log.info("competitions load is finished "+genderID+","+classID+","+initDate+","+finishDate);
	} catch(Exception e){
	    log.error("Error durante la carga de datos", e);
	    ret = Boolean.FALSE;
	}
	return ret;
    }
    
    /**
     * Last competitions finished or in course.
     * @param initDate and finishDate in format yyyymmdd
     * @return List<Competition>
     */
    public Boolean loadCompetitionService(
	    String competitionID,
	    String eventID,
	    String editionID,
	    String  genderID,
	    String classID
	    ) {
	Boolean ret = false;
	Competition comp = dao.getCompetition(Long.parseLong(competitionID), 
		Long.parseLong(eventID), Long.parseLong(editionID), 
		Long.parseLong(genderID), Long.parseLong(classID), Long.valueOf(-1));
	if(isNeedLoadStagesAndClassifications(comp)){
	    log.info("la competicion "+comp.getName()+" no esta cargada o finalizada, cargamos info");
	    List<Competition> stages = getStageRaceCompetitions(comp.getCompetitionID(), comp.getEventID(),
		    comp.getEditionID(), comp.getGenderID(), comp.getClassID());
	    persisistStagesCompetitionDetails(comp, stages);
	    ret = true;
	} else {
	    log.info("la competicion "+comp.getName()+" SI esta cargada y finalizada, no realizamos la carga");
	    ret = true;
	}
	return ret;
    }
    
    /*
     */
    private void loadAndSaveStageCompetitions(String initDate, String finishDate) throws ParseException, CyclingResultsException {
	Date init = DateUtil.parse(initDate, Contracts.DATE_FORMAT_SEARCH_COMPS);
	Calendar cal = Calendar.getInstance();
	cal.setTime(DateUtil.parse(finishDate, Contracts.DATE_FORMAT_SEARCH_COMPS));
	Date fin = cal.getTime(); 
	List<Competition> competitions = dao.getCompetitions(init, fin,
		CompetitionType.STAGES);
	for (Competition comp : competitions) {
	    if(isNeedLoadStagesAndClassifications(comp)){
		log.info("la competicion "+comp.getName()+" no esta cargada o finalizada, cargamos info");
		List<Competition> stages = getStageRaceCompetitions(comp.getCompetitionID(), comp.getEventID(),
			comp.getEditionID(), comp.getGenderID(), comp.getClassID());
		persisistStagesCompetitionDetails(comp, stages);
	    } else {
		log.info("la competicion "+comp.getName()+" SI esta cargada y finalizada, no realizamos la carga");
	    }
	}
    }
    
    private void persisistStagesCompetitionDetails(Competition comp, List<Competition> stages) {
	if (stages.size() > 0) {
	    persistClassifications(comp);
	    for (Competition stage : stages) {
		// comprobamos que no volvemos a guardar la general
		if (!stage.getPhase1ID().equals(Contracts.PHASE_1_ID_GENERAL_CLASSIFICATIONS)) {
		    stage.setCompetitionType(CompetitionType.STAGE_STAGES);
		    persistCompetition(stage);
		}
	    }
	}
    }

    /**
     *  
     * @return 
     * 	false if exists stages/classifications for the competition and the current date is greather than competition's finish date
     * 	true in other cases
     */
    private boolean isNeedLoadStagesAndClassifications(Competition comp) {
	boolean ret = false;
	if (CompetitionType.STAGES.equals(comp.getCompetitionType())){
	    if (dao.getCompetitionClassifications(comp).size()==0 || dao.getCompetitionStages(comp).size()==0){
		ret = true;
		log.debug("needLoad "+comp);
	    } else if (!competitionFinalizada(comp)){
		ret = true;
		log.debug("needReLoad "+comp);
	    } else {
		log.debug("not load "+comp);
	    }
	    
	}
	return ret;
    }

    private boolean competitionFinalizada(Competition comp) {
	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(DateUtil.dateWithoutHour(comp.getFinishDate()));
	    cal.add(Calendar.DAY_OF_YEAR, 1);
	    Date finishDate = cal.getTime(); 
	    return !finishDate.after(new Date());
	} catch (ParseException e) {
	    log.error("Error calculando fecha", e);
	    return false;
	}
    }

    private void persistClassifications(Competition stage) {
	List<Competition> classifications = getClassifications(stage);
	for(Competition classification : classifications){
	    persistCompetition(classification);
	}
    }

    /**
     * 
     * @param html
     * @param competition ->sera la clasficicion general a partir d ela cual se sacan las demas
     */
    private List<Competition> getClassifications(Competition competition) {
	List<Competition> classifications = new ArrayList<Competition>();
	String html = readClassificationFromUCIWebResults(competition);
	if (html != null) {
	    Document doc = Jsoup.parse(html);
	    Elements elements = doc.select("div.menu_item_tekst");
	    for (int i = 0; i < elements.size(); i++) {
		Element classification = elements.get(i);
		String name = competition.getName();
		if (!classification.text().trim().equals("General")) {
		    name = name + " " + classification.text();
		}
		Competition compClass = new Competition.Builder()
			.setInitDate(competition.getInitDate())
			.setFinishDate(competition.getFinishDate())
			.setName(name)
			.setPhase1ID((long)0)
			.setEventPhaseID(competition.getEventPhaseID())
			.setPhaseClassificationID(
				Utils.getKeyId(classification.toString(), Contracts.PHASE_CLASSIFICATION_ID_KEY))
			.setSportID(competition.getSportID()).setCompetitionID(competition.getCompetitionID())
			.setEventID(competition.getEventID()).setEditionID(competition.getEditionID())
			.setSeasonID(competition.getSeasonID()).setCompetitionID(competition.getCompetitionID())
			.setGenderID(competition.getGenderID()).setClassID(competition.getClassID())
			.setCompetitionType(CompetitionType.CLASSIFICATION_STAGES).build();
		classifications.add(compClass);
	    }
	}
	return classifications;
    }
    
    
    private String readClassificationFromUCIWebResults(Competition comp){
	String ret = null;
	try {
	    String url = getURLClassifications(comp);
	    ret = NetworkUtils.getRequest(Contracts.BASE_URL_UCI, url);
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info", e);
	} catch (IOException e) {
	    log.error("error leyendo info", e);
	}
	return ret;
    }

    private void loadCompetitions(String genderID, String classID, String initDate, String finishDate) {
	String htmlString = null;
	try {
	    String url = getURLCompetitions(genderID, classID, initDate, finishDate);
	    htmlString = NetworkUtils.getRequest(Contracts.BASE_URL_UCI, url);
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info ", e);
	} catch (IOException e) {
	    log.error("error leyendo info ", e);
	}
	//log.debug(ret.toString());
	if (htmlString != null){
	    List<Competition> list = tratarXmlCompetitions(htmlString);
	    log.info(list.size()+" competitions load, genderID="+genderID+", classID="+classID+", initDate="+initDate+",finDate="+finishDate);
	} else {
	    log.info("no se encontro informacion");
	}
    }
    
    private String getURLCompetitions(String genderID, String classID, String initDate, String finishDate) {
	String url = Contracts.ALL_COMPS
		.replace(Contracts.GENDER_ID, genderID)
		.replace(Contracts.CLASS_ID, classID)
		.replace(Contracts.INIT_DATE, initDate)
		.replace(Contracts.FINISH_DATE, finishDate)
		;
	log.debug(url);
	return url;
    }


    private List<Competition> tratarXmlCompetitions(String html) {
	Document doc = Jsoup.parse(html);
	Elements tableElements = doc.select("table.datatable");
	List<Competition> list = new ArrayList<Competition>();

	Elements tableRowElements = tableElements.select(":not(thead) tr");

	// Date Competition Nat. Class Winner
	for (int i = 0; i < tableRowElements.size(); i++) {
	    Element row = tableRowElements.get(i);
	    if (row.attr("valign").equals("top")) {// los que no tienen
						   // valing=top son headers
		Elements rowItems = row.select("td");
		Competition competition = new Competition.Builder()
			.setEventID(getKeyId(rowItems.get(1).toString(),Contracts.EVENT_ID_KEY))
			.setSeasonID(getKeyId(rowItems.get(1).toString(),Contracts.SEASON_ID_KEY))
			.setCompetitionID(getKeyId(rowItems.get(1).toString(),Contracts.COMPETITION_ID_KEY))
			.setEventPhaseID(getKeyId(rowItems.get(1).toString(), Contracts.EVENT_PHASE_ID_KEY))
			.setPhaseClassificationID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE_CLASSIFICATION_ID_KEY))
			.setEditionID(getKeyId(rowItems.get(1).toString(), Contracts.EDITION_ID_KEY))
			.setGenderID(getKeyId(rowItems.get(1).toString(), Contracts.GENDER_ID_KEY))
			.setClassID(getKeyId(rowItems.get(1).toString(), Contracts.CLASS_ID_KEY))
			.setPageID(getKeyId(rowItems.get(1).toString(), Contracts.PAGE_ID_KEY))
			.setSportID(getKeyId(rowItems.get(1).toString(), Contracts.SPORT_ID_KEY))
			.setPhase1ID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE1_ID_KEY))
			.setPhase2ID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE2_ID_KEY))
			.setPhase3ID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE3_ID_KEY))
			
			.setDates(
				rowItems.get(0).text().replace("\u00a0", "")
					.trim())
			.setName(rowItems.get(1).text())
			.setNationality(rowItems.get(2).text())	
			.setCompetitionClass(CompetitionClass.get(rowItems.get(3).text())).build();
		competition.calculateCompetitionType();
		log.debug(competition.toString());
		persistCompetition(competition);
		list.add(competition);
	    }
	}
	return list;
    }

    private void persistCompetition(Competition comp) {
	if (comp!= null && comp.getCompetitionID() != null ){
	    Competition saveComp= dao.getCompetition(comp.getCompetitionID(), comp.getEventID(), comp.getEditionID(),
		    comp.getGenderID(), comp.getClassID(), comp.getPhase1ID(), comp.getPhaseClassificationID());
	    if (saveComp == null){
		dao.save(comp);
		log.info("persistido "+comp);
	    } else if (!saveComp.getName().equals(comp.getName())){
		saveComp.setName(comp.getName());
		dao.save(saveComp);
		log.info("persistido cambio nombre"+comp);
	    }
	}
    }

    /**
     * 
     * @param string
     * @return
     */
    private Long getKeyId(String string, String key) {
	return Utils.getKeyId(string, key);
    }
}

