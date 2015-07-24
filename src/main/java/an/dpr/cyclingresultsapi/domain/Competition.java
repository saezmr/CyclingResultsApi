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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;
import an.dpr.cyclingresultsapi.util.DateUtil;

/*PAHSE Y PHASE CLASSIFICATION FORMAN UN "todo".
 * Para etapas, eventPhaseId=0 and phaseClassificationID=-1 es el resultado por defecto de la etapa,
 * que coincide con la clasificaicon de la etapa.
 * phase1ID en los casos de la etapa suele indicar el id de esa etapa en concreto
 */
/**
 * PageId
 * 19006 -> one day result (one day race or single classification in stage race)
 * 16004 -> stage race
 */
/**
 *  Si 0->indica que es una clasificaicon parcial dentro de una stage race. 
 */
/**
 * Event Bean
 * 
 * @author saez
 *
 */
@Entity
@Table
public class Competition {

    private static final Logger log = LoggerFactory.getLogger(Competition.class);

    private Long id;
    private Long eventID;
    private Long editionID;//Id de la edicion para un mismo evento (tour 2012, 2013...)
    private Long seasonID;
    private Long competitionID;
    private Long eventPhaseID;
    private Long phaseClassificationID;//nos indica con -1 si es general 
    private Long phase1ID;//si eventphaseId = 0, y phase1id = 0, indica general.
    private Long phase2ID;
    private Long phase3ID;
    private Long genderID;// 1 men 2 women
    private Long classID;// elite 2 junior 101 sub23
    private Long pageID;
    private Long sportID;//102 road
    private String name;
    private Date initDate;
    private Date finishDate;
    private String nationality;// TODO ENUM!!
    private CompetitionClass competitionClass;// TODO ENUM!!
    private String category;// TODO ENUM!!
    private String classificationName;//general, puntos, etapa 3...
    private CompetitionType competitionType;

    //stages race list info
    private String winner;
    private String leader;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    public Long getId(){
	return this.id;
    }
    
    public void setId(Long id){
	this.id = id;
    }
    
    @Column
    public Long getEventID() {
	return eventID;
    }

    public void setEventID(Long eventID) {
	this.eventID = eventID;
    }

    @Column
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setDates(String dates) {
	String year = dates.substring(dates.length() - 4, dates.length());
	String[] split = dates.split("-");
	if (split.length > 1) {
	    initDate = DateUtil.parseUCIDate(split[0] + "" + year);
	    finishDate = DateUtil.parseUCIDate(split[1]);
	} else {
	    initDate = DateUtil.parseUCIDate(split[0]);
	    finishDate = initDate;
	}
    }


    @Column
    public String getNationality() {
	return nationality;
    }

    public void setNationality(String nationality) {
	this.nationality = nationality;
    }

    @Column
    public CompetitionClass getCompetitionClass() {
	return competitionClass;
    }

    public void setCompetitionClass(CompetitionClass competitionClass) {
	this.competitionClass = competitionClass;
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
    public Long getEditionID() {
	return editionID;
    }

    public void setEditionID(Long editionID) {
	this.editionID = editionID;
    }

    @Column
    public Long getPhaseClassificationID() {
	return phaseClassificationID;
    }

    public void setPhaseClassificationID(Long phaseClassificationID) {
	this.phaseClassificationID = phaseClassificationID;
    }

    @Column
    public Long getEventPhaseID() {
	return eventPhaseID;
    }

    public void setEventPhaseID(Long eventPhaseID) {
	this.eventPhaseID = eventPhaseID;
    }

    @Column
    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    @Column
    public Long getGenderID() {
	return genderID;
    }

    public void setGenderID(Long genderID) {
	this.genderID = genderID;
    }

    @Column
    public Long getClassID() {
	return classID;
    }

    public void setClassID(Long classID) {
	this.classID = classID;
    }


    @Column
    public Long getPhase1ID() {
        return phase1ID;
    }

    public void setPhase1ID(Long phase1id) {
        phase1ID = phase1id;
    }

    @Column
    public Long getPhase2ID() {
        return phase2ID;
    }

    public void setPhase2ID(Long phase2id) {
        phase2ID = phase2id;
    }

    @Column
    public Long getPhase3ID() {
        return phase3ID;
    }

    public void setPhase3ID(Long phase3id) {
        phase3ID = phase3id;
    }

    @Column
    public Long getPageID() {
        return pageID;
    }

    public void setPageID(Long pageID) {
        this.pageID = pageID;
    }

    @Column
    public Long getSportID() {
        return sportID;
    }

    public void setSportID(Long sportID) {
        this.sportID = sportID;
    }

    @Override
    public String toString() {
	return "Competition [id=" + id + ", eventID=" + eventID + ", editionID=" + editionID + ", seasonID=" + seasonID
		+ ", competitionID=" + competitionID + ", eventPhaseID=" + eventPhaseID + ", phaseClassificationID="
		+ phaseClassificationID + ", phase1ID=" + phase1ID + ", phase2ID=" + phase2ID + ", phase3ID="
		+ phase3ID + ", genderID=" + genderID + ", classID=" + classID + ", pageID=" + pageID + ", sportID="
		+ sportID + ", name=" + name + ", initDate=" + initDate + ", finishDate=" + finishDate
		+ ", nationality=" + nationality + ", competitionClass=" + competitionClass + ", category=" + category
		+ ", classificationName=" + classificationName + ", eventType=" + competitionType + ", winner=" + winner
		+ ", leader=" + leader + "]";
    }

    @Column
    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }
    
    public void calculateCompetitionType(){
        if (initDate.equals(finishDate) || CompetitionClass.CN.equals(competitionClass)){
            setCompetitionType(CompetitionType.ONE_DAY);
        } else if (!initDate.equals(finishDate)){
            setCompetitionType(CompetitionType.STAGES);
        }
    }
    
    @Column
    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public void setCompetitionType(CompetitionType competitionType) {
        this.competitionType = competitionType;
    }

    @Column
    public Long getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Long seasonID) {
        this.seasonID = seasonID;
    }

    @Column
    public Long getCompetitionID() {
        return competitionID;
    }

    public void setCompetitionID(Long competitionID) {
        this.competitionID = competitionID;
    }


    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
    
    

    // builder
    public static class Builder {
	private Long eventID;
	private Long editionID;
	private Long phaseClassificationID;
	private Long eventPhaseID;
	private String name;
	private Date initDate;
	private Date finishDate;
	private String dates;
	private String nationality;
	private CompetitionClass competitionClass;
	private String category;
	private Long genderID;// 1 men 2 women
	private Long classID;// elite 2 junior 101 sub23
	private Long phase1ID;
	private Long phase2ID;
	private Long phase3ID;
	private Long pageID;
	private Long sportID;
	private Long seasonID;
	private Long competitionID;
	private String winner;
	private String leader;
	private CompetitionType competitionType;

	public Builder() {
	}

	public Competition build() {
	    Competition comp = new Competition();
	    comp.setEventID(eventID);
	    comp.setEventPhaseID(eventPhaseID);
	    comp.setEditionID(editionID);
	    comp.setPhaseClassificationID(phaseClassificationID);
	    comp.setName(name);
	    if (dates != null)
		comp.setDates(dates);
	    comp.setNationality(nationality);
	    comp.setCompetitionClass(competitionClass);
	    comp.setCategory(category);
	    comp.setGenderID(genderID);
	    comp.setClassID(classID);
	    comp.setPhase1ID(phase1ID);
	    comp.setPhase2ID(phase2ID);
	    comp.setPhase3ID(phase3ID);
	    comp.setPageID(pageID);
	    comp.setSportID(sportID);
	    comp.setSeasonID(seasonID);
	    comp.setCompetitionID(competitionID);
	    comp.setLeader(leader);
	    comp.setWinner(winner);
	    if (initDate != null)
		comp.setInitDate(initDate);
	    if (finishDate != null)
		comp.setFinishDate(finishDate);
	    comp.setCompetitionType(competitionType);
	    return comp;
	}

	public Builder setEventID(Long eventID) {
	    this.eventID = eventID;
	    return this;
	}
	public Builder setGenderID(Long genderID) {
	    this.genderID = genderID;
	    return this;
	}
	public Builder setClassID(Long classID) {
	    this.classID = classID;
	    return this;
	}

	public Builder setEditionID(Long editionID) {
	    this.editionID = editionID;
	    return this;
	}
	
	public Builder setSportID(Long sportID) {
	    this.sportID = sportID;
	    return this;
	}
	
	public Builder setPageID(Long pageID) {
	    this.pageID = pageID;
	    return this;
	}

	public Builder setPhaseClassificationID(Long phaseClassificationID) {
	    this.phaseClassificationID = phaseClassificationID;
	    return this;
	}
	
	public Builder setEventPhaseID(Long eventPhaseID) {
	    this.eventPhaseID = eventPhaseID;
	    return this;
	}

	public Builder setPhase1ID(Long phase1ID) {
	    this.phase1ID = phase1ID;
	    return this;
	}
	
	public Builder setPhase2ID(Long phase2ID) {
	    this.phase2ID = phase2ID;
	    return this;
	}
	
	public Builder setPhase3ID(Long phase3ID) {
	    this.phase3ID = phase3ID;
	    return this;
	}

	public Builder setName(String name) {
	    this.name = name;
	    return this;
	}

	public Builder setDates(String dates) {
	    this.dates = dates;
	    return this;
	}

	public Builder setNationality(String nat) {
	    this.nationality = nat;
	    return this;
	}

	public Builder setCompetitionClass(CompetitionClass competitionClass) {
	    this.competitionClass = competitionClass;
	    return this;
	}
	
	public Builder setCompetitionType(CompetitionType competitionType) {
	    this.competitionType = competitionType;
	    return this;
	}

	public Builder setCategory(String category) {
	    this.category = category;
	    return this;
	}
	
	public Builder setSeasonID(Long seasonID) {
	    this.seasonID = seasonID;
	    return this;
	}
	
	public Builder setCompetitionID(Long competitionID) {
	    this.competitionID = competitionID;
	    return this;
	}
	public Builder setWinner(String winner) {
	    this.winner = winner;
	    return this;
	}

	public Builder setLeader(String leader) {
	    this.leader = leader;
	    return this;
	}
	
	public Builder setInitDate(Date date){
	    initDate = date;
	    return this;
	}

	public Builder setFinishDate(Date date){
	    finishDate = date;
	    return this;
	}
    }


}
