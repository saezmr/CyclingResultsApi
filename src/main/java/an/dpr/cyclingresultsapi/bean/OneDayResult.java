package an.dpr.cyclingresultsapi.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneDayResult {

    @XmlElement
    private String rank;
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

    public String getRank() {
	return rank;
    }

    public void setRank(String rank) {
	this.rank = rank;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getNat() {
	return nat;
    }

    public void setNat(String nat) {
	this.nat = nat;
    }

    public String getTeam() {
	return team;
    }

    public void setTeam(String team) {
	this.team = team;
    }

    public String getAge() {
	return age;
    }

    public void setAge(String age) {
	this.age = age;
    }

    public String getResult() {
	return result;
    }

    public void setResult(String result) {
	this.result = result;
    }

    public String getPaR() {
	return paR;
    }

    public void setPaR(String paR) {
	this.paR = paR;
    }

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
	
	public OneDayResult build(){
	    OneDayResult r = new OneDayResult();
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

}
