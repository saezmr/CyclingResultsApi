package an.dpr.cyclingresultsapi.bo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import an.dpr.cyclingresultsapi.dao.CalendarDAO;
import an.dpr.cyclingresultsapi.domain.CalendarEvent;
import an.dpr.cyclingresultsapi.exception.CyclingResultsException;
import an.dpr.cyclingresultsapi.util.Contracts;
import an.dpr.cyclingresultsapi.util.DateUtil;

@Stateless
public class CalendarBO {

    @Inject private Logger log;
    @Inject private CalendarDAO dao;
    
    
    public static void main(String...jargs) throws Exception{
	CalendarBO c = new CalendarBO();
	c.loadCalendar();
    }
    
    /**
     * Carga y devuelve el numero de eventos cargados
     * @return
     * @throws Exception
     */
    public Integer loadCalendar() throws Exception{
	Document doc = Jsoup.parse(CompetitionsBO.class.getResourceAsStream("/RoadCalendar2016.htm"), "UTF8", "");
	Elements elements = doc.select("div.eventListBody");
	Elements events = elements.select("li.event");
	List<CalendarEvent> list = new ArrayList<CalendarEvent>();
	for(int i=0; i<events.size(); i++){
	    Element event = events.get(i);
	    Date[] dates = getEventDate(event);
	    CalendarEvent ce = new CalendarEvent.Builder()
        	    .setCategory(getCategory(event))
        	    .setCountry(getCountry(event))
        	    .setEventClass(getClass(event))
        	    .setFinishDate(dates[1])
        	    .setInitDate(dates[0])
        	    .setName(getNameEvent(event)).build();
	    list.add(ce);
	}
	log.debug(list.toString());
	int cont = 0;
	for(CalendarEvent ce : list){
	    if (!dao.exists(ce)){
		dao.create(ce);
		cont++;
	    }
	}
	return cont;
    }
    
    private String getClass(Element event) {
	Elements el = event.select("span.class");
	return el.get(0).text().substring(5).trim();
    }

    private String getCategory(Element event) {
	Elements el = event.select("span.category");
	return el.get(0).text().substring(8).trim();
    }

    private String getCountry(Element event) {
	Elements el = event.select("span.country");
	return el.get(0).text().substring(7).trim();
    }

    private String getNameEvent(Element event) {
	Elements el = event.select("span.name");
	return el.get(0).text().substring(4).trim();
    }

    private Date[] getEventDate(Element event) throws ParseException {
	Elements el = event.select("span.daterange");
	SimpleDateFormat sdf = new SimpleDateFormat("d MMM", Locale.ENGLISH);
	String[] fechicas = el.get(0).text().split("-");
	Date initDate;
	Date finishDate;
	if (fechicas.length>1){
	    StringBuilder sb = new StringBuilder(fechicas[0]);
	    sb.append(" ");
	    sb.append(fechicas[1].split(" ")[1]);
	    initDate = sdf.parse(sb.toString());
	    
	    finishDate = sdf.parse(fechicas[1]);
	    
	} else {
	    initDate =  sdf.parse(fechicas[0]);
	    finishDate = initDate;
	}
	int currentYear = DateUtil.getCurrentYear();
	Calendar init = Calendar.getInstance();
	init.setTime(initDate);
	init.set(Calendar.YEAR, currentYear);
	Calendar finish = Calendar.getInstance();
	finish.setTime(finishDate);
	finish.set(Calendar.YEAR, currentYear);
	return new Date[]{init.getTime(), finish.getTime()};
    }

    public List<CalendarEvent> findByYear(int year) throws ParseException {
	Calendar c = Calendar.getInstance();
	c.set(Calendar.YEAR, year);
	CalendarEvent filtro = new CalendarEvent();
	filtro.setInitDate(DateUtil.firstDayOfYear(c.getTime()));
	filtro.setFinishDate(DateUtil.lastDayOfYear(c.getTime()));
	return dao.query(filtro);
    }

    public List<CalendarEvent> findByMonth(int year, int month) throws ParseException {
	Calendar c = Calendar.getInstance();
	c.set(Calendar.YEAR, year);
	c.set(Calendar.MONTH, month-1);
	CalendarEvent filtro = new CalendarEvent();
	filtro.setInitDate(DateUtil.firstDayOfMonth(c.getTime()));
	filtro.setFinishDate(DateUtil.lastDayOfMonth(c.getTime()));
	return dao.query(filtro);
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

    public List<CalendarEvent> quey(String initDate, String finishDate, String name, String country, String category,
	    String eventClass) throws CyclingResultsException{
	CalendarEvent ce;
	try {
	    ce = new CalendarEvent.Builder()
        	    .setInitDate(getDate(initDate, false))
        	    .setFinishDate(getDate(finishDate, false))
        	    .setName(name)
        	    .setCountry(country)
        	    .setCategory(category)
        	    .setEventClass(eventClass).build();
	    
	    return dao.query(ce);
	} catch (ParseException e) {
	    throw new CyclingResultsException("incorrect dates");
	}
    }
}
