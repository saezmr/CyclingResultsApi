package an.dpr.cyclingresultsapi.services.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import an.dpr.cyclingresultsapi.bo.ResultsBO;
import an.dpr.cyclingresultsapi.domain.ResultRow;

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
 */
@Path("/results/")
public class ResultsRS {
    private static final Logger log = LoggerFactory.getLogger(ResultsRS.class);

    @Autowired
    private ResultsBO bo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/oneDay/{competitionID},{eventID},{editionID},{genderID},{classID}")
    public List<ResultRow> getOneDayResult(
	    @PathParam("competitionID") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("editionID") String editionID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID
	    ) {
	return bo.getOneDayResult(competitionID, eventID, editionID, genderID, classID);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stage/{competitionId},{eventID},{editionID},{genderID},{classID},{phase1ID}")
    public List<ResultRow> getStageResult(
	    @PathParam("competitionId") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("editionID") String editionID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID,
	    @PathParam("phase1ID") String phase1ID) {
	return bo.getStageResult(competitionID, eventID, editionID, genderID, classID, phase1ID);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/classification/{competitionId},{eventID},{editionID},{genderID},{classID},{phase1ID},{phaseClassificationID}")
    public List<ResultRow> getClassificationResult(
	    @PathParam("competitionId") String competitionID,
	    @PathParam("eventID") String eventID,
	    @PathParam("editionID") String editionID,
	    @PathParam("genderID") String genderID,
	    @PathParam("classID") String classID,
	    @PathParam("phase1ID") String phase1ID,
	    @PathParam("phaseClassificationID") String phaseClassificationID
	    ) {
	return bo.getClassificationResult(competitionID, eventID, editionID, genderID, classID, phase1ID, phaseClassificationID);
    }
    
}
