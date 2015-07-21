package an.dpr.cyclingresultsapi.bean;

/*
 * 
 */
public enum CompetitionClass {
    UWT("UWT"), HC1("1.HC"), HC2("2.HC"), C11("1.1"), C21("2.1"), C12("1.2"), C22("2.2");
    
    private String code;

    private CompetitionClass(String code){
	this.code = code;
    }
    
    public String code(){
	return code;
    }
}
