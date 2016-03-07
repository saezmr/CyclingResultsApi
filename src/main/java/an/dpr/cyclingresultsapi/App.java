package an.dpr.cyclingresultsapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
    
    public static void main(String...args){
	System.out.println(getURL("http://www.uci.ch/road/results/"));
    }

    public static String getURL(String p_URL) {
        StringBuffer out = new StringBuffer();

        try {
            //get default report id
            URL url = new URL(p_URL);

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(false);
             huc.setConnectTimeout(30 * 1000);
             huc.setReadTimeout(300 * 1000);
             huc.setRequestMethod("GET");
             huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
             huc.connect();

            InputStream input = huc.getInputStream();

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String inputLine;

            //int x = 0;
            while ((inputLine = in.readLine()) != null)
            {
                out.append(inputLine);
            }

            in.close();
        } catch (Exception e) {
             e.printStackTrace();
        } 

        return out.toString();
    }

}
