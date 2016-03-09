package an.dpr.cyclingresultsapi.services.rest;

import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.bo.CalendarBO;
import an.dpr.cyclingresultsapi.domain.CalendarEvent;
import an.dpr.cyclingresultsapi.exception.CyclingResultsException;

/**
 * TODO REST service for calendar (future compeititons)
 * @author saez
 *
 */
@Path("calendar")
public class CalendarRS {
    
    @Inject private Logger log;
    @Inject private CalendarBO ejb;

    @GET
    @Path("loadCalendar")
    public String loadCalendar(){
	try{
	    int loaded= ejb.loadCalendar();
	    return loaded +" calendar events loaded";
	} catch(Exception e){
	    return "It had an error in loading task";
	}
    }
    
    @GET
    @Path("get/year/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CalendarEvent> getByYear(@PathParam("year") int year) throws CyclingResultsException{
	try {
	    return ejb.findByYear(year);
	} catch (ParseException e) {
	    throw new CyclingResultsException("Error, incorrect year");
	}
    }

    @GET
    @Path("get/month/{year},{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CalendarEvent> getByMonth(@PathParam("year") int year, @PathParam("month") int month) throws CyclingResultsException{
	try {
	    return ejb.findByMonth(year, month);
	} catch (ParseException e) {
	    throw new CyclingResultsException("Error, incorrect year-month");
	}
    }

    @GET
    @Path("query/{initdate},{finishdate},{name},{country},{category},{class}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CalendarEvent> getByMonth(
	    @PathParam("initdate") String initdate, 
	    @PathParam("finishdate") String finishdate,
	    @PathParam("name") String name,
	    @PathParam("country") String country,
	    @PathParam("category") String category,
	    @PathParam("class") String eventClass
	    ) throws CyclingResultsException{
	return ejb.quey(initdate, finishdate, !name.equals("-")?name:null,!country.equals("-")?country:null, !category.equals("-")?category:null, !eventClass.equals("-")?eventClass:null);
    }
}

