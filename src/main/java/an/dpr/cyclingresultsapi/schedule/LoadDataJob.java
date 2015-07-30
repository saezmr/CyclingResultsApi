package an.dpr.cyclingresultsapi.schedule;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import an.dpr.cyclingresultsapi.bo.CompetitionsBO;
import an.dpr.cyclingresultsapi.util.Contracts;
import an.dpr.cyclingresultsapi.util.DateUtil;

@Component("loadDataJob")
public class LoadDataJob {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDataJob.class);
    
    @Autowired
    private CompetitionsBO competitionService;

    /**
     * Carga las competiciones pendientes
     * TODO de momento solo masculino elite, pendiente para resto de categorias, sobretodo femenino elite!!
     */
    public void loadLastCompetitions(){
	try {
	    log.debug("inicio carga de datos");
	    Calendar cal = Calendar.getInstance();
	    String genderID = Contracts.MEN_GENDER_ID;
	    String classID = Contracts.ELITE_CLASS_ID;
	    String initDate = DateUtil.format(cal.getTime(), Contracts.DATE_FORMAT_SEARCH_COMPS);
	    cal.add(Calendar.YEAR, 1);
	    String finishDate = DateUtil.format(cal.getTime(), Contracts.DATE_FORMAT_SEARCH_COMPS);
	    log.debug("solicitamos "+genderID+","+classID+","+initDate+","+finishDate);
	    Boolean result = competitionService.loadCompetitionsService(genderID, classID, initDate, finishDate);
	    log.info("Carga de datos para periodo "+initDate+"-"+finishDate+" con resultado:"+result);
	} catch(Exception e){
	    log.error("Error en la ejeucion del JOB de carga", e);
	}
	
    }
    
}
