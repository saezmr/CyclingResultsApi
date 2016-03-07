package an.dpr.cyclingresultsapi.bean;

/*
 * TODO repensar esto por el tema de que no siempre las carreras han tenido esta calificacion UCI
 */
public enum CompetitionClass {
    UWT("UWT"), HC1("1.HC"), HC2("2.HC"), C11("1.1"), C21("2.1"), C12("1.2"), C22("2.2"), CN("CN")
    ,UPT("UPT")//UCI PRO TOUR (2005 - 2007)
    ,HIS("HIS")//GRANDES VUELTAS 2008-2010
    ,GT("GT")//GRANDES VUELTAS  2004 o menor
    ,ALL("ALL")
    ,CDM("CDM")//campeonato del mundo;
    ,C1("C1"),C2("C2")//categorias de cyclo-cross
    ;
    
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
	return ret != null ? ret : ALL;
    }
    
}
