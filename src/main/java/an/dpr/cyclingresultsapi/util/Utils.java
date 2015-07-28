package an.dpr.cyclingresultsapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    /**
     * 
     * @param string
     * @return
     */
    public static Long getKeyId(String string, String key) {
	int fromIdx = string.indexOf(key);
	if (fromIdx < 0) {
	    return null;
	} else {
	    int toIdx = string.indexOf("&", fromIdx);
	    return Long.parseLong(string.substring(fromIdx + key.length() + 1, toIdx));
	}
    }

}
