package an.dpr.cyclingresultsapi.services.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.exception.CyclingResultsException;
import an.dpr.cyclingresultsapi.util.Contracts;
import an.dpr.cyclingresultsapi.util.DateUtil;
import an.dpr.cyclingresultsapi.util.Utils;

/**
 * REST service for cycling competitions
 * 
 * @author saez url all competitions 2015 road man
 *         http://www.uci.infostradasports.com/
 *         asp/lib/TheASP.asp?PageID=19004&TaalCode
 *         =2&StyleID=0&SportID=102&CompetitionID
 *         =-1&EditionID=-1&CompetitionID=-1&GenderID
 *         =1&ClassID=1&CompetitionPhaseID=0&Phase1ID
 *         =0&Phase2ID=0&CompetitionCodeInv=1
 *         &PhaseStatusCode=262280&DerivedCompetitionPhaseID
 *         =-1&SeasonID=488&StartDateSort
 *         =20150108&EndDateSort=20151225&Detail=1&
 *         DerivedCompetitionID=-1&S00=-3&S01=2&S02=1&PageNr0=-1&Cache=8
 *         
 * Todo el calendario: http://www.uci.ch/road/calendar/
 * TODO LIST:
 * 	-find by gender (male, female)
 * 	-find by class (elite, sub23..)
 * 	-find by competitionClass (WT, hc1, 1.1...)
 * 	-find by category (world, europe, asia ...) 
 */

@Path("/competitions/")
public class CompetitionRS {

    
    private static final Logger log = LoggerFactory.getLogger(CompetitionRS.class);
    
    @Autowired
    private CompetitionDAO dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query/{initDate},{finDate},{genderID},{classID},{competitionClass}")
    public List<Competition> getCompetitions(
	    @PathParam("initDate") String initDate,
	    @PathParam("finDate") String finDate,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID,
	    @PathParam("competitionClass") String competitionClass){
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/next/")
    public List<Competition> getNextCompetitions() {
	return dao.getYearCompetitions(2015);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/year/{year}")
    public List<Competition> getYearCompetitions(@PathParam("year") String year) {
	return dao.getYearCompetitions(Integer.parseInt(year));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/month/{year},{month}")
    public List<Competition> getMonthCompetitions(
	    @PathParam("year") String year,
	    @PathParam("month") String month
	    ) {
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allEditions/{competitionID}")
    public List<Competition> getAllEditions(@PathParam("competitionID") String competitionID){
	try{
	    return dao.getCompetitionAllEditions(Long.parseLong(competitionID));
	} catch(Exception e){
	    log.error("Error obteniendo ediciones de la competicion "+competitionID, e);
	    return new ArrayList<Competition>();
	}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stageRaceCompetitions/{competitionID},{eventID},{genderID},{classID}")
    public List<Competition> getStageRaceCompetitionsService(
	    @PathParam("competitionID") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID
	    ) {
	Competition comp = new Competition.Builder()
		.setCompetitionID(Long.parseLong(competitionID)) 
		.setEventID(Long.parseLong(eventID))
		.setGenderID(Long.parseLong(genderID)) 
		.setClassID(Long.parseLong(classID))
		.build();
	List<Competition> list = new ArrayList();
	list.addAll(dao.getCompetitionClassifications(comp));
	list.addAll(dao.getCompetitionStages(comp));
	if (list.size() == 0){//esto no deberia ocurrir, pero por si aca
	    list = getStageRaceCompetitions(Long.parseLong(competitionID), Long.parseLong(eventID),
		    Long.parseLong(genderID), Long.parseLong(classID));
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
	    Long eventID, Long genderID, Long classID ) {
	StringBuilder ret = new StringBuilder();
	Competition competition = dao.getCompetition(competitionID,
		eventID,genderID, classID, (long)-1);
	try {
	    HttpClient client = new DefaultHttpClient();
	    URI url = getURLStageEvents(competition);
	    HttpGet get = new HttpGet(url);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity()
		    .getContent(), "cp1252");
//	    String file = "C:/Users/saez/workspace/andpr/CyclingResultsApi/html/stageRaceCompetitions.htm";
//	    FileReader isr = new FileReader(new File(file));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
		ret.append(line);
	    }
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info ", e);
	} catch (URISyntaxException e) {
	    log.error("error leyendo info ", e);
	} catch (IOException e) {
	    log.error("error leyendo info ", e);
	}
	log.debug(ret.toString());
	return tratarXmlStageRaceCompetitions(ret.toString(), competition);
    }

    private URI getURLStageEvents(Competition competition) throws URISyntaxException {
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
	
	return new URI(sb.toString());
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
    
    private URI getURLClassifications(Competition comp) throws URISyntaxException{
	StringBuilder sb = new StringBuilder();
	sb.append(Contracts.URL_STAGE_1)
	.append(Contracts.URL_STAGE_EVENT_DATA
		.replace(Contracts.SPORT_ID, String.valueOf(comp.getSportID()))
		.replace(Contracts.COMPETITION_ID, String.valueOf(comp.getCompetitionID()))
		.replace(Contracts.EDITION_ID, String.valueOf(comp.getEditionID()))
		.replace(Contracts.SEASON_ID, String.valueOf(comp.getSeasonID()))
		.replace(Contracts.EVENT_ID, String.valueOf(comp.getEventID()))
		.replace(Contracts.GENDER_ID, String.valueOf(comp.getGenderID()))
		.replace(Contracts.CLASS_ID, String.valueOf(comp.getClassID()))
		)
	.append(Contracts.URL_STAGE_DATA.replace(Contracts.PHASE1_ID,Contracts.PHASE_1_ID_GENERAL_CLASSIFICATIONS.toString()));
	return new URI(sb.toString());
    }

    /**
     * Last competitions finished or in course.
     * TODO posibilidad de filtrar la carga tambien por fechas
     * @param initDate and finishDate in format yyyymmdd
     * @return List<Competition>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loadCompetitions/{genderID},{classID},{initDate},{finishDate}")
    public Boolean loadCompetitionsService(
	    @PathParam("genderID") String  genderID,
	    @PathParam("classID") String classID,
	    @PathParam("initDate") String initDate,
	    @PathParam("finishDate") String finishDate
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
    
    //ojo al dato, necesitamos saber si una competicion esta acabada o no para actualizar si eso.
    private void loadAndSaveStageCompetitions(String initDate, String finishDate) throws ParseException, CyclingResultsException {
	Date init = DateUtil.parse(initDate, Contracts.DATE_FORMAT_SEARCH_COMPS);
	Calendar cal = Calendar.getInstance();
	cal.setTime(DateUtil.parse(finishDate, Contracts.DATE_FORMAT_SEARCH_COMPS));
	cal.add(Calendar.DAY_OF_YEAR,1);
	Date fin = cal.getTime(); 
	List<Competition> competitions = dao.getCompetitions(init, fin,
		CompetitionType.STAGES);
	for (Competition comp : competitions) {
	    if(isNeedLoadStagesAndClassifications(comp)){
		log.info("la competicion "+comp.getName()+" no esta cargada o finalizada, cargamos info");
		List<Competition> stages = getStageRaceCompetitions(
			comp.getCompetitionID(), comp.getEventID(),
			comp.getGenderID(), comp.getClassID());
		if (stages.size() > 0) {
		    persistClassifications(stages.get(0));
		    for (Competition stage : stages) {
			// comprobamos que no volvemos a guardar la general
			if (!stage.getPhase1ID().equals(Contracts.PHASE_1_ID_GENERAL_CLASSIFICATIONS)) {
			    stage.setCompetitionType(CompetitionType.STAGE_STAGES);
			    persistCompetition(stage);
			}
		    }
		}
	    } else {
		log.info("la competicion "+comp.getName()+" SI esta cargada y finalizada, no realizamos la carga");
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
	    } else if (!competitionFinalizada(comp)){
		ret = true;
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
	    return finishDate.after(new Date());
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
	Document doc = Jsoup.parse(readClassificationFromUCIWebResults(competition));
	Elements elements = doc.select("div.menu_item_tekst");
	for(int i = 0;i<elements.size();i++){
	    Element classification = elements.get(i);
	    String name = competition.getName();
	    if (!classification.text().trim().equals("General")){
		name = name+" "+classification.text();
	    }
	    Competition compClass = new Competition.Builder()
		.setInitDate(competition.getInitDate())
		.setFinishDate(competition.getFinishDate())
		.setName(name )
		.setPhase1ID(competition.getPhase1ID())
		.setPhaseClassificationID(Utils.getKeyId(classification.toString(), Contracts.PHASE_CLASSIFICATION_ID_KEY))
		.setSportID(competition.getSportID())
		.setCompetitionID(competition.getCompetitionID())
		.setEventID(competition.getEventID())
		.setEditionID(competition.getEditionID())
		.setSeasonID(competition.getSeasonID())
		.setCompetitionID(competition.getCompetitionID())
		.setGenderID(competition.getGenderID())
		.setClassID(competition.getClassID())
		.setCompetitionType(CompetitionType.CLASSIFICATION_STAGES)
		.build();
	    classifications.add(compClass);
	}
	return classifications;
    }
    
    
    private String readClassificationFromUCIWebResults(Competition comp){
	StringBuilder ret = new StringBuilder();
	try {
	    HttpClient client = new DefaultHttpClient();// TODO DEPRECATED!
	    URI url = getURLClassifications(comp);
	    HttpGet get = new HttpGet(url);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), "cp1252");
//	    String file = "C:/Users/saez/workspace/andpr/CyclingResultsApi/html/tour2015General.htm";
//	    FileReader isr = new FileReader(new File(file));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
		ret.append(line);
	    }
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info", e);
	} catch (IOException e) {
	    log.error("error leyendo info", e);
	} catch (URISyntaxException e) {
	    log.error("error leyendo info", e);
	}
	return ret.toString();
    }

    private void loadCompetitions(String genderID, String classID, String initDate, String finishDate) {
	StringBuilder ret = new StringBuilder();
	try {
	    HttpClient client = new DefaultHttpClient();
	    String url = getURLCompetitions(genderID, classID, initDate, finishDate);
	    log.debug("URL:"+url);
	    HttpGet get = new HttpGet(url);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity()
		    .getContent(), "cp1252");
//	    String file = "C:/Users/saez/workspace/andpr/CyclingResultsApi/html/RoadResults.htm";
//	    FileReader isr = new FileReader(new File(file));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
		ret.append(line);
	    }
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info ", e);
	} catch (IOException e) {
	    log.error("error leyendo info ", e);
	}
	//log.debug(ret.toString());
	List<Competition> list = tratarXmlCompetitions(ret.toString());
	log.info(list.size()+" competitions load, genderID="+genderID+", classID="+classID+", initDate="+initDate+",finDate="+finishDate);
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
	    Competition saveComp= dao.getCompetition(comp.getCompetitionID(), comp.getEventID(), 
		    comp.getGenderID(), comp.getClassID(), comp.getPhase1ID(), comp.getPhaseClassificationID());
	    if (saveComp == null){
		dao.save(comp);
	    } else if (!saveComp.getName().equals(comp.getName())){
		saveComp.setName(comp.getName());
		dao.save(saveComp);
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
