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

/*
 Rula
http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&TaalCode=2&StyleID=0&SportID=102&CompetitionID=20695&EditionID=810688&EventID=12146&GenderID=1&ClassID=1&PhaseStatusCode=262280&EventPhaseID=810689&Phase1ID=0&Detail=1&Ranking=0&DerivedEventPhaseID=-1&S00=1&S01=2&S02=3&PageNr0=-1
no
http://www.uci.infostradasports.com/asp/lib/TheASP.asp?PageID=19006&Phase2ID=0&Phase3ID=0&DerivedEventPhaseID=-1&Detail=1&Ranking=0&S00=1&S01=2&S02=3&PageNr0=-1&SportID=102&CompetitionID=20695&EditionID=810688&EventID=12146&GenderID=1&ClassID=1&Phase1ID=0


TaalCode=2&StyleID=0&PhaseStatusCode=262280&EventPhaseID=810689

CompetitionCodeInv=1
*/