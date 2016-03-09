package an.dpr.cyclingresultsapi.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"initDate" , "finishDate", "name", "country", "category", "eventClass"})})
@XmlRootElement
public class CalendarEvent {

    private Long id;
    private Date initDate;
    private Date finishDate;
    private String name;
    private String country;
    private String category;
    private String  eventClass;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInitDate() {
	return initDate;
    }

    public void setInitDate(Date initDate) {
	this.initDate = initDate;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }

    @Column
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column
    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    @Column
    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    @Column
    public String getEventClass() {
	return eventClass;
    }

    public void setEventClass(String eventClass) {
	this.eventClass = eventClass;
    }

    @Override
    public String toString() {
	return "CalendarEvent [id=" + id + ", initDate=" + initDate + ", finishDate=" + finishDate + ", name=" + name
		+ ", Country=" + country + ", Category=" + category + ", eventClass=" + eventClass + "]";
    }

    public static class Builder {
	private Long id;
	private Date initDate;
	private Date finishDate;
	private String name;
	private String country;
	private String category;
	private String eventClass;
	
	public CalendarEvent build(){
	    CalendarEvent ce = new CalendarEvent();
	    ce.setId(id);
	    ce.setCategory(category);
	    ce.setCountry(country);
	    ce.setEventClass(eventClass);
	    ce.setFinishDate(finishDate);
	    ce.setInitDate(initDate);
	    ce.setName(name);
	    return ce;
	}
	
	public Builder setId(Long id){
	    this.id = id;
	    return this;
	}

	public Builder setInitDate(Date initDate){
	    this.initDate = initDate;
	    return this;
	}

	public Builder setFinishDate(Date finishDate){
	    this.finishDate = finishDate;
	    return this;
	}
	
	public Builder setCountry(String country){
	    this.country = country;
	    return this;
	}
	
	public Builder setCategory(String category){
	    this.category = category;
	    return this;
	}

	public Builder setName(String name){
	    this.name = name;
	    return this;
	}
	
	public Builder setEventClass(String eventClass){
	    this.eventClass = eventClass;
	    return this;
	}
    }

}
