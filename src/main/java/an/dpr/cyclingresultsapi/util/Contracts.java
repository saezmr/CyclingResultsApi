package an.dpr.cyclingresultsapi.util;


public interface Contracts {
    //values
    public static final Long DEFAULT_CLASS_ID = (long) 1;
    public static final Long DEFAULT_GENDER_ID = (long) 1;
    public static final String MEN_GENDER_ID="1";
    public static final String WOMEN_GENDER_ID="2";
    public static final String ELITE_CLASS_ID="1";
    public static final String JUNIOR_CLASS_ID="2";
    public static final String UNDER23_CLASS_ID="101";
    public static final Long PHASE_1_ID_GENERAL_CLASSIFICATIONS = (long)0;
    public static final String SEASON_2015="488";
    public static final String SEASON_2014="486";
    public static final String SEASON_2013="484";
    public static final String SEASON_2012="482";
    public static final String SEASON_2011="480";
    public static final String SEASON_2010="478";
    public static final String SEASON_2009="476";
    public static final String IN_PROGRESS_FLAG="progress";
    
    
    //KEYS DE LA UCI
    public static final String SEASON_ID_KEY="SeasonID";
    public static final String EVENT_ID_KEY="EventID";
    public static final String EDITION_ID_KEY="EditionID";
    public static final String PHASE_CLASSIFICATION_ID_KEY="PhaseClassificationID";
    public static final String EVENT_PHASE_ID_KEY="EventPhaseID";
    public static final String COMPETITION_ID_KEY="CompetitionID";
    public static final String GENDER_ID_KEY="GenderID";
    public static final String CLASS_ID_KEY="ClassID";
    public static final String PAGE_ID_KEY="PageID";
    public static final String SPORT_ID_KEY="SportID";
    public static final String PHASE1_ID_KEY="Phase1ID";
    public static final String PHASE2_ID_KEY="Phase2ID";
    public static final String PHASE3_ID_KEY="Phase3ID";
    
    //CLAVES PARA REPLACE
    public static final String SEASON_ID="SEASON_ID";
    public static final String EVENT_ID="EVENT_ID";
    public static final String EDITION_ID="EDITION_ID";//EditionID
    public static final String PHASE_CLASSIFICATION_ID="PHASE_CLASSIFICATION_ID";//PhaseClassificationID
    public static final String EVENT_PHASE_ID="EVENT_PHASE_ID";//EventPhaseID
    public static final String COMPETITION_ID="COMPETITION_ID";
    public static final String GENDER_ID="GENDER_ID";
    public static final String CLASS_ID="CLASS_ID";
    public static final String PAGE_ID="PAGE_ID";
    public static final String SPORT_ID="SPORT_ID";
    public static final String PHASE1_ID="PHASE1_ID";
    public static final String PHASE2_ID="PHASE2_ID";
    public static final String PHASE3_ID="PHASE3_ID";
    public static final String INIT_DATE="INIT_DATE";
    public static final String FINISH_DATE="FINISH_DATE";
    
    //valores predefinidos por la UCI
    public static final String PAGE_ID_ONE_DAY = "19006";
    public static final String PAGE_ID_STAGE_RACE = "19004";

    public static final String DATE_FORMAT_SEARCH_COMPS = "yyyyMMdd";
    public static final String BASE_URL_UCI = "www.uci.infostradasports.com";
    //estan todos los datos de la historia!!!
    public static final String ALL_COMPS = "/asp/lib/TheASP.asp?PageID=19004&TaalCode=2&StyleID=0&SportID=102&CompetitionID=-1&EditionID=-1&EventID=-1"
    	+ "&GenderID="+GENDER_ID+"&ClassID="+CLASS_ID
    	+ "&EventPhaseID=0&Phase1ID=0&Phase2ID=0&CompetitionCodeInv=1&PhaseStatusCode=262280&DerivedEventPhaseID=-1&SeasonID=488"
    	+ "&StartDateSort="+INIT_DATE+"&EndDateSort="+FINISH_DATE
    	+ "&Detail=1&DerivedCompetitionID=-1&S00=-3&S01=2&S02=1&PageNr0=-1&Cache=8";
    //+"&PageNr0=-1"; para que se muestren todos los resultados y no solo la primer pagina (40 eventos)
    //stage results url
    public static final String ALL_RESULTS = "&S00=1&S01=2&S02=3&PageNr0=-1";
    public static final String URL_STAGE_1="/asp/lib/TheASP.asp?PageID=19006&CompetitionCodeInv=1&Phase2ID=0&Phase3ID=0&DerivedEventPhaseID=-1&Detail=1&Ranking=0"+ALL_RESULTS;
    public static final String URL_STAGE_EVENT_DATA="&SportID=SPORT_ID&CompetitionID=COMPETITION_ID&EditionID=EDITION_ID&EventID=EVENT_ID&GenderID=GENDER_ID&ClassID=CLASS_ID";
    public static final String URL_STAGE_DATA="&Phase1ID=PHASE1_ID";
    
    public static final String STAGE_URL = "/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0&SportID=102"
	    + "&CompetitionID="+COMPETITION_ID+"&EditionID="+EDITION_ID+"&EventID="+EVENT_ID+"&GenderID="+GENDER_ID+"&ClassID="+CLASS_ID+"&Phase1ID="+PHASE1_ID
    	+ "&PhaseClassificationID=-1&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1&Cache=8";
    
    public static final String CLASSIFICATIONS_URL = "/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0&SportID=102"
    	+ "&CompetitionID="+COMPETITION_ID+"&EditionID="+EDITION_ID+"&EventID="+EVENT_ID+"&GenderID="+GENDER_ID+"&ClassID="+CLASS_ID
    	+"&PhaseClassificationID="+PHASE_CLASSIFICATION_ID+"&EventPhaseID="+EVENT_PHASE_ID
    	+ "&Phase1ID=0&Phase2ID=0&Phase3ID=0&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1&Cache=8";
    
    //stage events url
    public static final String URL_STAGE_EVENTS_1 = "/asp/lib/TheASP.asp?PageID=19004&Phase1ID=-1&Phase2ID=0&Phase3ID=0"
    	+ "&CompetitionCodeInv=1&Detail=1&Ranking=0&All=0&TaalCode=2&StyleID=0&Cache=8";
    public static final String URL_STAGE_EVENTS_DATA= "&SportID="+SPORT_ID+"&CompetitionID="+COMPETITION_ID+"&EditionID="+EDITION_ID+"&SeasonID="+SEASON_ID
	    +"&ClassID="+CLASS_ID+"&GenderID="+GENDER_ID+"&EventID="+EVENT_ID+"&EventPhaseID="+EVENT_PHASE_ID;

}
