package an.dpr.cyclingresultsapi.bean;
//package an.dpr.cyclingresultsapi.bean;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Event Bean
// * 
// * @author saez
// *
// */
//@XmlRootElement(name = "event")
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Event {
//
//    private static final Logger log = LoggerFactory.getLogger(Event.class);
//
//    // FOR RESULTS URL
//    @XmlElement
//    private Integer eventId;
//    @XmlElement
//    private Integer editionID;
//    @XmlElement
//    private Integer phaseClassificationID;
//    @XmlElement
//    private Integer eventPhaseID;
//    // END FOR RESULTS URL
//
//    @XmlElement
//    private String name;
//    @XmlElement
//    private Date initDate;
//    @XmlElement
//    private Date finishDate;
//    @XmlElement
//    private String nationality;// TODO ENUM!!
//    @XmlElement
//    private String eventClass;// TODO ENUM!!
//
//    public Integer getEventId() {
//	return eventId;
//    }
//
//    public void setEventId(Integer eventId) {
//	this.eventId = eventId;
//    }
//
//    public String getName() {
//	return name;
//    }
//
//    public void setName(String name) {
//	this.name = name;
//    }
//
//    public void setDates(String dates) {
//	String year = dates.substring(dates.length() - 4, dates.length());
//	String[] split = dates.split("-");
//	if (split.length > 1) {
//	    initDate = parseDate(split[0] + "" + year);
//	    finishDate = parseDate(split[1]);
//	} else {
//	    initDate = parseDate(split[0]);
//	    finishDate = initDate;
//	}
//    }
//
//    public static void main(String... args) {
//	Event event = new Event();
//	event.setDates("03Apr2015");
//	System.out.println(event.getInitDate());
//	System.out.println(event.getFinishDate());
//	event.setDates("07Mar-08Mar2015");
//	System.out.println(event.getInitDate());
//	System.out.println(event.getFinishDate());
//    }
//
//    private Date parseDate(String date) {
//	SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy", new Locale(
//		"en_EN"));
//	try {
//	    return sdf.parse(date);
//	} catch (ParseException e) {
//	    log.error("Error parseando " + date, e);
//	    return null;
//	}
//    }
//
//    public String getNationality() {
//	return nationality;
//    }
//
//    public void setNationality(String nationality) {
//	this.nationality = nationality;
//    }
//
//    public String getEventClass() {
//	return eventClass;
//    }
//
//    public void setEventClass(String eventClass) {
//	this.eventClass = eventClass;
//    }
//
//    // builder
//    public static class Builder {
//	private Integer eventId;
//	private Integer editionID;
//	private Integer phaseClassificationID;
//	private Integer eventPhaseID;
//	private String name;
//	private String dates;
//	private String nationality;
//	private String eventClass;
//
//	public Builder() {
//	}
//
//	public Event build() {
//	    Event event = new Event();
//	    event.setEventId(eventId);
//	    event.setEventPhaseID(eventPhaseID);
//	    event.setEditionID(editionID);
//	    event.setPhaseClassificationID(phaseClassificationID);
//	    event.setName(name);
//	    event.setDates(dates);
//	    event.setNationality(nationality);
//	    event.setEventClass(eventClass);
//	    return event;
//	}
//
//	public Builder setEventId(Integer eventId) {
//	    this.eventId = eventId;
//	    return this;
//	}
//
//	public Builder setEditionID(Integer editionID) {
//	    this.editionID = editionID;
//	    return this;
//	}
//
//	public Builder setPhaseClassificationID(Integer phaseClassificationID) {
//	    this.phaseClassificationID = phaseClassificationID;
//	    return this;
//	}
//
//	public Builder setEventPhaseID(Integer eventPhaseID) {
//	    this.eventPhaseID = eventPhaseID;
//	    return this;
//	}
//
//	public Builder setName(String name) {
//	    this.name = name;
//	    return this;
//	}
//
//	public Builder setDates(String dates) {
//	    this.dates = dates;
//	    return this;
//	}
//
//	public Builder setNationality(String nat) {
//	    this.nationality = nat;
//	    return this;
//	}
//
//	public Builder setEventClass(String eventClass) {
//	    this.eventClass = eventClass;
//	    return this;
//	}
//    }
//
//    @Override
//    public String toString() {
//	return "Event [eventId=" + eventId + ", editionID=" + editionID
//		+ ", phaseClassificationID=" + phaseClassificationID
//		+ ", eventPhaseID=" + eventPhaseID + ", name=" + name
//		+ ", initDate=" + initDate + ", finishDate=" + finishDate
//		+ ", nationality=" + nationality + ", eventClass=" + eventClass
//		+ "]";
//    }
//
//    public Date getInitDate() {
//	return initDate;
//    }
//
//    public void setInitDate(Date initDate) {
//	this.initDate = initDate;
//    }
//
//    public Date getFinishDate() {
//	return finishDate;
//    }
//
//    public void setFinishDate(Date finishDate) {
//	this.finishDate = finishDate;
//    }
//
//    public Integer getEditionID() {
//	return editionID;
//    }
//
//    public void setEditionID(Integer editionID) {
//	this.editionID = editionID;
//    }
//
//    public Integer getPhaseClassificationID() {
//	return phaseClassificationID;
//    }
//
//    public void setPhaseClassificationID(Integer phaseClassificationID) {
//	this.phaseClassificationID = phaseClassificationID;
//    }
//
//    public Integer getEventPhaseID() {
//	return eventPhaseID;
//    }
//
//    public void setEventPhaseID(Integer eventPhaseID) {
//	this.eventPhaseID = eventPhaseID;
//    }
//}
