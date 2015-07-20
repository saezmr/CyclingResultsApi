package an.dpr.cyclingresultsapi.services.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

import an.dpr.cyclingresultsapi.bean.Event;
import an.dpr.cyclingresultsapi.util.Contracts;

/**
 * REST service for cycling events
 * 
 * @author saez url all events 2015 road man
 *         http://www.uci.infostradasports.com/
 *         asp/lib/TheASP.asp?PageID=19004&TaalCode
 *         =2&StyleID=0&SportID=102&CompetitionID
 *         =-1&EditionID=-1&EventID=-1&GenderID
 *         =1&ClassID=1&EventPhaseID=0&Phase1ID
 *         =0&Phase2ID=0&CompetitionCodeInv=1
 *         &PhaseStatusCode=262280&DerivedEventPhaseID
 *         =-1&SeasonID=488&StartDateSort
 *         =20150108&EndDateSort=20151225&Detail=1&
 *         DerivedCompetitionID=-1&S00=-3&S01=2&S02=1&PageNr0=-1&Cache=8
 */
@Path("/events/")
public class EventsRS {

    private static final Logger log = LoggerFactory.getLogger(EventsRS.class);

    private static final String ALL_EVENTS = "http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19004&TaalCode=2&StyleID=0&SportID=102&CompetitionID=-1&EditionID=-1&EventID=-1&GenderID=1&ClassID=1&EventPhaseID=0&Phase1ID=0&Phase2ID=0&CompetitionCodeInv=1&PhaseStatusCode=262280&DerivedEventPhaseID=-1&SeasonID=488&StartDateSort=20150108&EndDateSort=20151225&Detail=1&DerivedCompetitionID=-1&S00=-3&S01=2&S02=1&PageNr0=-1&Cache=8";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_XML)
    @Path("/next/")
    public List<Event> getNextEvents() {
	return null;//TODO PENDIENTE DE IMPLEMENTAR
    }

    /**
     * Last events finished or in course.
     * 
     * @return List<Event>
     * @throws URISyntaxException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/last/")
    public List<Event> getLastEvents() throws URISyntaxException {
	StringBuilder ret = new StringBuilder();
	try {
	    HttpClient client = new DefaultHttpClient();
	    HttpGet get = new HttpGet(ALL_EVENTS);
	    HttpResponse response = client.execute(get);
	    InputStreamReader isr = new InputStreamReader(response.getEntity()
		    .getContent(), "cp1252");
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) {
		ret.append(line);
	    }
	} catch (ClientProtocolException e) {
	    log.error("error leyendo info meteo", e);
	} catch (IOException e) {
	    log.error("error leyendo info meteo", e);
	}
	log.info(ret.toString());
	return tratarXmlEvents(ret.toString());
    }

    private List<Event> tratarXmlEvents(String html) {
	Document doc = Jsoup.parse(html);
	Elements tableElements = doc.select("table.datatable");
	List<Event> list = new ArrayList<Event>();

	Elements tableRowElements = tableElements.select(":not(thead) tr");

	// Date Event Nat. Class Winner
	for (int i = 0; i < tableRowElements.size(); i++) {
	    Element row = tableRowElements.get(i);
	    if (row.attr("valign").equals("top")) {// los que no tienen
						   // valing=top son headers
		Elements rowItems = row.select("td");
		Event event = new Event.Builder()
			.setEventId(getKeyId(rowItems.get(1).toString(),Contracts.COMPETITION_ID_KEY))
			.setEventPhaseID(getKeyId(rowItems.get(1).toString(), Contracts.EVENT_PHASE_ID_KEY))
			.setPhaseClassificationID(getKeyId(rowItems.get(1).toString(), Contracts.PHASE_CLASSIFICATION_ID_KEY))
			.setEditionID(getKeyId(rowItems.get(1).toString(), Contracts.EDITION_ID_KEY))
			.setDates(
				rowItems.get(0).text().replace("\u00a0", "")
					.trim())
			.setName(rowItems.get(1).text())
			.setNationality(rowItems.get(2).text())
			.setEventClass(rowItems.get(3).text()).build();
		log.debug(event.toString());
		list.add(event);
	    }
	}
	return list;
    }

    /**
     * 
     * @param string
     * @return
     */
    private Integer getKeyId(String string, String key) {
	int fromIdx = string.indexOf(key);
	if (fromIdx < 0 ){
	    return null;
	} else {
	    int toIdx = string.indexOf("&", fromIdx);
	    return Integer.parseInt(string.substring(fromIdx + key.length()+1, toIdx));
	}
    }
}
