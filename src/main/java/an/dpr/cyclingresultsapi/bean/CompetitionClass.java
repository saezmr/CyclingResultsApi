package an.dpr.cyclingresultsapi.bean;

/*
 * 
 */
public enum CompetitionClass {
    UWT("UWT"), HC1("1.HC"), HC2("2.HC"), C11("1.1"), C21("2.1"), C12("1.2"), C22("2.2"), CN("CN"), ALL("ALL");
    
    private String code;

    private CompetitionClass(String code){
	this.code = code;
    }
    
    public String code(){
	return code;
    }
    
    public static CompetitionClass get(String text){
	CompetitionClass ret = null;
	for(CompetitionClass cc : values()){
	    if (cc.code.equals(text)){
		ret = cc;
		break;
	    }
	}
	return ret;
    }
    
}
