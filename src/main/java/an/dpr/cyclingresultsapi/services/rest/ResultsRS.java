package an.dpr.cyclingresultsapi.services.rest;

import java.io.BufferedReader;
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

import an.dpr.cyclingresultsapi.bean.OneDayResult;
import an.dpr.cyclingresultsapi.util.Contracts;

/**
 * REST service for cycling results
 * @author saez
 *
 * http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0&SportID=102&CompetitionID=27200&EditionID=1002783&SeasonID=488&EventID=10635&GenderID=1&ClassID=1&PhaseStatusCode=262280&EventPhaseID=1003239&Phase1ID=0&Phase2ID=0&Phase3ID=0&PhaseClassificationID=1069239&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1&Cache=8
 */
@Path("results")
public class ResultsRS {
    private static final Logger log = LoggerFactory.getLogger(ResultsRS.class);
    
    private static final String ROAD_MEN_SPORT_ID = "102";
    private static final String ONE_DAY_RESULTS_URL = "http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0"
    	+ "&SportID="+ROAD_MEN_SPORT_ID+"&CompetitionID="+Contracts.COMPETITION_ID+"&EditionID="+Contracts.EDITION_ID
    	+ "&EventPhaseID="+Contracts.EVENT_PHASE_ID+ "&PhaseClassificationID="+Contracts.PHASE_CLASSIFICATION_ID
    	+"&Phase1ID=0&Phase2ID=0&Phase3ID=0"
    	+"&SeasonID=488&EventID=10635&GenderID=1&ClassID=1&PhaseStatusCode=262280"
    	+"&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1&Cache=8";
    
    /*
     * competitionId
     * EditionID
     * EventPhaseID
     * PhaseClassificationID
     * 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oneDay/{competition_id},{edition_id},{event_phase_id},{phase_classification_id}")
    public List<OneDayResult> getOneDayResult(
	    @PathParam("competition_id") String competition_id,
	    @PathParam("edition_id") String edition_id,
	    @PathParam("event_phase_id") String event_phase_id,
	    @PathParam("phase_classification_id") String phase_classification_id
	    ) {
  	StringBuilder ret = new StringBuilder();
  	try {
	    HttpClient client = new DefaultHttpClient();//TODO DEPRECATED!
	    String url = ONE_DAY_RESULTS_URL
		    .replace(Contracts.COMPETITION_ID, competition_id)
		    .replace(Contracts.EDITION_ID, edition_id)
		    .replace(Contracts.EVENT_PHASE_ID, event_phase_id)
		    .replace(Contracts.PHASE_CLASSIFICATION_ID, phase_classification_id);
	    
	    HttpGet get = new HttpGet(url);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity()
		    .getContent(), "cp1252");
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
  	return tratarXmlEvents(ret.toString());
      }
    
      private List<OneDayResult> tratarXmlEvents(String html) {
  	Document doc = Jsoup.parse(html);
  	Elements tableElements = doc.select("table.datatable");
  	List<OneDayResult> list = new ArrayList<OneDayResult>();

  	Elements tableRowElements = tableElements.select(":not(thead) tr");

  	//Date	Event	Nat.	Class	Winner
  	for (int i = 0; i < tableRowElements.size(); i++) {
  	    Element row = tableRowElements.get(i);
  	    if (row.attr("valign").equals("top")){//los que no tienen valing=top son headers
  		Elements rowItems = row.select("td");
  		OneDayResult event = new OneDayResult.Builder()
  		.setRank(rowItems.get(0).text())
  		.setName(rowItems.get(1).text())
  		.setNat(rowItems.get(2).text())
  		.setTeam(rowItems.get(3).text())
  		.setAge(rowItems.get(4).text())
  		.setResult(rowItems.get(5).text())
  		.setPaR(rowItems.get(6).text())
  		.setPcR(rowItems.get(7).text())
  		.build();
  		log.debug(event.toString());
  		list.add(event);
  	    }
  	}
  	return list;
      }
}
