package an.dpr.cyclingresultsapi.util;

public class Utils {
    /**
     * 
     * @param string
     * @return
     */
    public static Long getKeyId(String string, String key) {
	int fromIdx = string.indexOf(key);
	if (fromIdx < 0 ){
	    return null;
	} else {
	    int toIdx = string.indexOf("&", fromIdx);
	    return Long.parseLong(string.substring(fromIdx + key.length()+1, toIdx));
	}
    }
}
