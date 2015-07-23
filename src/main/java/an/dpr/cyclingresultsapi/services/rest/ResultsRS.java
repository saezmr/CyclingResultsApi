package an.dpr.cyclingresultsapi.services.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import an.dpr.cyclingresultsapi.dao.CompetitionDAO;
import an.dpr.cyclingresultsapi.dao.ResultRowDAO;
import an.dpr.cyclingresultsapi.domain.Competition;
import an.dpr.cyclingresultsapi.domain.ResultRow;
import an.dpr.cyclingresultsapi.util.Contracts;
import an.dpr.cyclingresultsapi.util.Utils;

/**
 * REST service for cycling results
 * 
 * @author saez
 *
 *         http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&
 *         TaalCode
 *         =2&StyleID=0&SportID=102&CompetitionID=27200&EditionID=1002783
 *         &SeasonID
 *         =488&EventID=10635&GenderID=1&ClassID=1&PhaseStatusCode=262280
 *         &EventPhaseID
 *         =1003239&Phase1ID=0&Phase2ID=0&Phase3ID=0&PhaseClassificationID
 *         =1069239
 *         &Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0
 *         =-1&Cache=8
 *         
 *   //TODO FALTARIAN LAS CLASIFICACIONES GENERALES "COMPLEMENTARIAS", MONTAÑA, PUNTOS...
 */
@Path("results")
public class ResultsRS {
    private static final Logger log = LoggerFactory.getLogger(ResultsRS.class);

    @Autowired
    private CompetitionDAO dao;
    @Autowired
    private ResultRowDAO rDao;

    private static final String ROAD_MEN_SPORT_ID = "102";
    private static final String ONE_DAY_RESULTS_URL = "http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0"
	    + "&SportID="
	    + ROAD_MEN_SPORT_ID
	    + "&CompetitionID="
	    + Contracts.COMPETITION_ID
	    + "&EditionID="
	    + Contracts.EDITION_ID
	    + "&EventPhaseID="
	    + Contracts.EVENT_PHASE_ID
	    + "&PhaseClassificationID=-1&Phase1ID=0&Phase2ID=0&Phase3ID=0"
	    + "&SeasonID=488&EventID=10635&GenderID=1&ClassID=1&PhaseStatusCode=262280"
	    + "&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1&Cache=8";

    /*
     * competitionId EditionID EventPhaseID PhaseClassificationID
     */
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oneDay/{competition_id},{eventID},{genderID}, {classID}")
    public List<ResultRow> getOneDayResult(
	    @PathParam("competition_id") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID
	    ) {
	Competition comp = dao.getCompetition(Long.parseLong(competitionID),
		Long.parseLong(eventID),Long.parseLong(genderID), Long.parseLong(classID),(long)-1);
	List<ResultRow> list = rDao.getResults(comp);
	if (list == null || list.size() == 0){
	    String url = getURLOneDayResults(comp);
	    list = readStageFromUCIWebResults(comp, url);
	}
	return list;
    }
    
    private List<ResultRow> readStageFromUCIWebResults(Competition comp, String url){
	StringBuilder ret = new StringBuilder();
	try {
	    HttpClient client = new DefaultHttpClient();// TODO DEPRECATED!

	    HttpGet get = new HttpGet(url);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), "cp1252");
//	    String file = "C:/Users/saez/workspace/andpr/CyclingResultsApi/html/OneDayResult.htm";
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
	}
	log.info(ret.toString());
	return tratarXmlStageResult(ret.toString(), comp);
    }

    /**
     * TODO no funciona para TTT. No existe columna team, y ademas van tras cada
     * equipo (con su puesto) todos los ciclistas que tomaron parte (sin puesto)
     * Habria que pasar de los que no tienen rank
     * 
     * @param html
     * @return
     */
    private List<ResultRow> tratarXmlStageResult(String html, Competition comp) {
	Document doc = Jsoup.parse(html);
	Elements tableElements = doc.select("table.datatable");
	List<ResultRow> list = new ArrayList<ResultRow>();

	Elements tableRowElements = tableElements.select(":not(thead) tr");

	boolean isTeamTimeTrial = true;
	// headers
	for (int i = 0; i < tableRowElements.size(); i++) {
	    Element row = tableRowElements.get(i);
	    if (!row.attr("valign").equals("top")) {// los que no tienen
						    // valing=top son headers
		Elements rowItems = row.select("td.caption");
		if (rowItems.get(3).text().equals("Team")) {
		    isTeamTimeTrial = false;
		    break;
		}
	    }
	}

	// Date Event Nat. Class Winner
	for (int i = 0; i < tableRowElements.size(); i++) {
	    Element row = tableRowElements.get(i);
	    if (row.attr("valign").equals("top")) {// los que no tienen
						   // valing=top son headers
		Elements rowItems = row.select("td");
		int idx = 0;
		ResultRow.Builder builder;
		if (isTeamTimeTrial) {
		    builder = new ResultRow.Builder()
			    .setRank(rowItems.get(idx++).text())
			    .setName(rowItems.get(idx++).text())
			    .setNat(rowItems.get(idx++).text())
			    .setAge(rowItems.get(idx++).text())
			    .setResult(rowItems.get(idx++).text());
		} else {
		    builder = new ResultRow.Builder()
			    .setRank(rowItems.get(idx++).text())
			    .setName(rowItems.get(idx++).text())
			    .setNat(rowItems.get(idx++).text())
			    .setTeam(rowItems.get(idx++).text())
			    .setAge(rowItems.get(idx++).text())
			    .setResult(rowItems.get(idx++).text());
		}

		if (rowItems.size() > 6) {
		    builder.setPaR(rowItems.get(idx++).text())
		    	.setPcR(rowItems.get(idx++).text());
		}

		ResultRow odr = builder.build();
		odr.setCompetition(comp);
		persistResultRow(odr);
		log.debug(odr.toString());
		if (odr.getRank() != null && !odr.getRank().isEmpty()) {
		    list.add(odr);
		}
	    }
	}
	return list;
    }

    private void persistResultRow(ResultRow rr) {
	if (rr!= null && !rDao.resultRowExists(rr)){
	    rDao.save(rr);
	    log.debug("salvado con exito");
	}
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stage/{competitionId},{eventID},{genderID},{classID},{phase1ID}")
    public List<ResultRow> getStageResult(
	    @PathParam("competitionId") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID,
	    @PathParam("phase1ID") String phase1ID) {
	Competition comp = dao.getCompetition(Long.parseLong(competitionID),
		Long.parseLong(eventID),
		Long.parseLong(genderID), Long.parseLong(classID),
		Long.parseLong(phase1ID));
	List<ResultRow> list = rDao.getResults(comp);
	if (list == null || list.size() == 0){
	    String url = getURLStageResults(comp);
	    list = readStageFromUCIWebResults(comp, url);
	}
	return list;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/classification/{competitionId},{eventID},{genderID},{classID},{phase1ID},{phaseClassificationID}")
    public List<ResultRow> getClassificationResult(
	    @PathParam("competitionId") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID,
	    @PathParam("phase1ID") String phase1ID,
	    @PathParam("phaseClassificationID") String phaseClassificationID
	    ) {
	Competition comp = dao.getCompetition(Long.parseLong(competitionID),
		Long.parseLong(eventID),
		Long.parseLong(genderID), Long.parseLong(classID),
		Long.parseLong(phase1ID), Long.parseLong(phaseClassificationID));
	List<ResultRow> list = rDao.getResults(comp);
	if (list == null || list.size() == 0){
	    String url = getURLStageResults(comp);
	    list = readStageFromUCIWebResults(comp, url);
	}
	return list;
    }
    
    private String getURLOneDayResults(Competition comp) {
	String url = ONE_DAY_RESULTS_URL
		.replace(Contracts.COMPETITION_ID, comp.getCompetitionID().toString())
		.replace(Contracts.EDITION_ID, comp.getEditionID().toString())
		.replace(Contracts.EVENT_PHASE_ID,
			comp.getEventPhaseID().toString());
	return url;
    }

    private String getURLStageResults(Competition comp) {
	StringBuilder sb = new StringBuilder();
	sb.append(Contracts.URL_STAGE_1)
		.append(Contracts.URL_STAGE_EVENT_DATA
			.replace(Contracts.SPORT_ID,
				String.valueOf(comp.getSportID()))
			.replace(Contracts.COMPETITION_ID,
				String.valueOf(comp.getCompetitionID()))
			.replace(Contracts.EDITION_ID,
				String.valueOf(comp.getEditionID()))
			.replace(Contracts.SEASON_ID,
				String.valueOf(comp.getSeasonID()))
			.replace(Contracts.EVENT_ID,
				String.valueOf(comp.getEventID()))
			.replace(Contracts.GENDER_ID,
				String.valueOf(comp.getGenderID()))
			.replace(Contracts.CLASS_ID,
				String.valueOf(comp.getClassID())))
		.append(Contracts.URL_STAGE_DATA.replace(Contracts.PHASE1_ID,
			String.valueOf(comp.getPhase1ID())));
	return sb.toString();
    }
    
    private List<ResultRow> tratarXmlClassificationResult(String html, Competition comp) {
	Document doc = Jsoup.parse(html);
	Elements tableElements = doc.select("table.datatable");
	List<ResultRow> list = new ArrayList<ResultRow>();
//
//	Elements tableRowElements = tableElements.select(":not(thead) tr");
//
//	boolean isTeamTimeTrial = true;
//	// headers
//	for (int i = 0; i < tableRowElements.size(); i++) {
//	    Element row = tableRowElements.get(i);
//	    if (!row.attr("valign").equals("top")) {// los que no tienen
//						    // valing=top son headers
//		Elements rowItems = row.select("td.caption");
//		if (rowItems.get(3).text().equals("Team")) {
//		    isTeamTimeTrial = false;
//		    break;
//		}
//	    }
//	}
//
//	// Date Event Nat. Class Winner
//	for (int i = 0; i < tableRowElements.size(); i++) {
//	    Element row = tableRowElements.get(i);
//	    if (row.attr("valign").equals("top")) {// los que no tienen
//						   // valing=top son headers
//		Elements rowItems = row.select("td");
//		int idx = 0;
//		ResultRow.Builder builder;
//		if (isTeamTimeTrial) {
//		    builder = new ResultRow.Builder()
//			    .setRank(rowItems.get(idx++).text())
//			    .setName(rowItems.get(idx++).text())
//			    .setNat(rowItems.get(idx++).text())
//			    .setAge(rowItems.get(idx++).text())
//			    .setResult(rowItems.get(idx++).text());
//		} else {
//		    builder = new ResultRow.Builder()
//			    .setRank(rowItems.get(idx++).text())
//			    .setName(rowItems.get(idx++).text())
//			    .setNat(rowItems.get(idx++).text())
//			    .setTeam(rowItems.get(idx++).text())
//			    .setAge(rowItems.get(idx++).text())
//			    .setResult(rowItems.get(idx++).text());
//		}
//
//		if (rowItems.size() > 6) {
//		    builder.setPaR(rowItems.get(idx++).text())
//		    	.setPcR(rowItems.get(idx++).text());
//		}
//
//		ResultRow odr = builder.build();
//		odr.setCompetition(comp);
//		persistResultRow(odr);
//		log.debug(odr.toString());
//		if (odr.getRank() != null && !odr.getRank().isEmpty()) {
//		    list.add(odr);
//		}
//	    }
//	}
	return list;
    }
}
