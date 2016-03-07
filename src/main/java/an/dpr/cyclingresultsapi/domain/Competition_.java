package an.dpr.cyclingresultsapi.domain;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import an.dpr.cyclingresultsapi.bean.CompetitionClass;
import an.dpr.cyclingresultsapi.bean.CompetitionType;


@StaticMetamodel(Competition.class)
public class Competition_ {
    public static volatile SingularAttribute<Competition, Long> id;
    public static volatile SingularAttribute<Competition, Long> competitionID;
    public static volatile SingularAttribute<Competition, Long> eventID;
    public static volatile SingularAttribute<Competition, Long> editionID;
    public static volatile SingularAttribute<Competition, Long> genderID;
    public static volatile SingularAttribute<Competition, Long> classID;
    public static volatile SingularAttribute<Competition, Long> phase1ID;
    public static volatile SingularAttribute<Competition, Long> phaseClassificationID;
    public static volatile SingularAttribute<Competition, Long> sportID;
    public static volatile SingularAttribute<Competition, CompetitionClass> competitionClass;
    public static volatile SingularAttribute<Competition, CompetitionType> competitionType;
    public static volatile SingularAttribute<Competition, Date> initDate;
    public static volatile SingularAttribute<Competition, Date> finishDate;
}
