package an.dpr.cyclingresultsapi.domain;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CalendarEvent.class)
public class CalendarEvent_ {
    public static volatile SingularAttribute<CalendarEvent, Long> id;
    public static volatile SingularAttribute<CalendarEvent, Date> initDate;
    public static volatile SingularAttribute<CalendarEvent, Date> finishDate;
    public static volatile SingularAttribute<CalendarEvent, String> name;
    public static volatile SingularAttribute<CalendarEvent, String> country;
    public static volatile SingularAttribute<CalendarEvent, String> category;
    public static volatile SingularAttribute<CalendarEvent, String> eventClass;
    
}
