package an.dpr.cyclingresultsapi.util;


public interface Contracts {

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
    
    //valores predefinidos por la UCI
    public static final String PAGE_ID_ONE_DAY = "19006";
    public static final String PAGE_ID_STAGE_RACE = "19004";
    
    //stage url
    public static final String ALL_RESULTS = "&S00=1&S01=2&S02=3&PageNr0=-1";
    public static final String URL_STAGE_1="http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&CompetitionCodeInv=1&Phase2ID=0&Phase3ID=0&DerivedEventPhaseID=-1&Detail=1&Ranking=0"+ALL_RESULTS;
    public static final String URL_STAGE_EVENT_DATA="&SportID=SPORT_ID&CompetitionID=COMPETITION_ID&EditionID=EDITION_ID&SeasonID=SEASON_ID&EventID=EVENT_ID&GenderID=GENDER_ID&ClassID=CLASS_ID";
    public static final String URL_STAGE_DATA="&Phase1ID=PHASE1_ID";

}
