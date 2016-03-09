package an.dpr.cyclingresultsapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"rank", "name", "paR", "competition_id"})
		})
@XmlRootElement
public class ResultRow implements Comparable<ResultRow> {

    @XmlElement
    private String rank;
    @XmlElement
    private Long id;
    @XmlElement
    private String name;
    @XmlElement
    private String nat;
    @XmlElement
    private String team;
    @XmlElement
    private String age;
    @XmlElement
    private String result;
    @XmlElement
    private String paR;
    @XmlElement
    private String pcR;
    //only for persistence
    @XmlTransient
    private Competition competition;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    public Long getId(){
	return id;
    }
    
    public void setId(Long id){
	this.id=id;
    }
    
    @XmlTransient
    @ManyToOne(fetch=FetchType.EAGER)
    public Competition getCompetition(){
	return competition;
    }
    
    @XmlTransient
    public void setCompetition(Competition competition){
	this.competition=competition;
    }

    @Column
    public String getRank() {
	return rank;
    }

    public void setRank(String rank) {
	this.rank = rank;
    }

    @Column
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column
    public String getNat() {
	return nat;
    }

    public void setNat(String nat) {
	this.nat = nat;
    }

    @Column
    public String getTeam() {
	return team;
    }

    public void setTeam(String team) {
	this.team = team;
    }

    @Column
    public String getAge() {
	return age;
    }

    public void setAge(String age) {
	this.age = age;
    }

    @Column
    public String getResult() {
	return result;
    }

    public void setResult(String result) {
	this.result = result;
    }

    @Column
    public String getPaR() {
	return paR;
    }

    public void setPaR(String paR) {
	this.paR = paR;
    }

    @Column
    public String getPcR() {
	return pcR;
    }

    public void setPcR(String pcR) {
	this.pcR = pcR;
    }

    public static class Builder {
	private String rank;
	private String name;
	private String nat;
	private String team;
	private String age;
	private String result;
	private String paR;
	private String pcR;
	
	public ResultRow build(){
	    ResultRow r = new ResultRow();
	    r.setAge(age);
	    r.setName(name);
	    r.setNat(nat);
	    r.setPaR(paR);
	    r.setPcR(pcR);
	    r.setRank(rank);
	    r.setResult(result);
	    r.setTeam(team);
	    return r;
	}

	public Builder setRank(String rank) {
	    this.rank = rank;
	    return this;
	}

	public Builder setName(String name) {
	    this.name = name;
	    return this;
	}

	public Builder setNat(String nat) {
	    this.nat = nat;
	    return this;
	}

	public Builder setTeam(String team) {
	    this.team = team;
	    return this;
	}

	public Builder setAge(String age) {
	    this.age = age;
	    return this;
	}

	public Builder setResult(String result) {
	    this.result = result;
	    return this;
	}

	public Builder setPaR(String paR) {
	    this.paR = paR;
	    return this;
	}

	public Builder setPcR(String pcR) {
	    this.pcR = pcR;
	    return this;
	}
    }

    @Override
    public String toString() {
	return "OneDayResult [rank=" + rank + ", name=" + name + ", nat=" + nat + ", team=" + team + ", age=" + age
		+ ", result=" + result + ", paR=" + paR + ", pcR=" + pcR + "]";
    }

    @Override
    public int compareTo(ResultRow rr) {
	Integer rrRank = null;
	Integer thisRank = null;
	try{rrRank = Integer.valueOf(rr.rank);} catch(Exception e){}
	try{thisRank = Integer.valueOf(this.rank);} catch(Exception e){}
	if (rrRank != null && thisRank != null){
	    return thisRank.compareTo(rrRank);
	} else if (thisRank != null){
	    return -1;
	} else if (rrRank != null){
	    return 1;
	} else if (this.rank != null && rr.rank != null){
	    return this.rank.compareTo(rr.rank);
	} else if (this.rank != null){
	    return -1;
	} else if (rr.rank != null){
	    return 1;
	} else {
	    return 0;
	}
    }

}
