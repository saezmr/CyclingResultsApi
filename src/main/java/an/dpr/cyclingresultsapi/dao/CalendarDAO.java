package an.dpr.cyclingresultsapi.dao;

import java.util.List;

import an.dpr.cyclingresultsapi.domain.CalendarEvent;

public interface CalendarDAO {

    void create(CalendarEvent event);

    public void edit(CalendarEvent event);

    void remove(CalendarEvent event);

    CalendarEvent find(Long id);
    
    boolean exists(CalendarEvent event);

    List<CalendarEvent> findAll();

    List<CalendarEvent> query(CalendarEvent filter);
}
