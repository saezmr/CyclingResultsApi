package an.dpr.cyclingresultsapi.bean;

public enum SportID {
    ROAD(102);
    
    private int code;
    
    private SportID(int code){
	this.code = code;
    }
    
    public int code(){
	return code;
    }
}
