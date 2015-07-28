package an.dpr.cyclingresultsapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtils {
    
    private static final String ENCODING_UCI = "cp1252";
    private static final String HTTP = "http";

    private static final Logger log = LoggerFactory.getLogger(NetworkUtils.class);

    public static String getRequest(String baseUrl, String get) throws ClientProtocolException, IOException {
	CloseableHttpClient httpclient = HttpClients.createDefault();
	StringBuilder ret = new StringBuilder();
	try {
	    // http://cyclingresults-dprsoft.rhcloud.com/rest/competitions/query/20140101,20140601,1,1,UWT
	    HttpHost target = new HttpHost(baseUrl, 80, HTTP);
	    HttpHost proxy = new HttpHost("proxy.sdc.hp.com", 8080, "http");

	    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
	    HttpGet request = new HttpGet(get);
	    request.setConfig(config);

	    log.debug("Executing request " + request.getRequestLine() + " to " + target + " via " + proxy);

	    CloseableHttpResponse response = httpclient.execute(target, request);
	    try {
		InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), ENCODING_UCI);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
		    ret.append(line);
		}
	    } finally {
		response.close();
	    }
	} finally {
	    httpclient.close();
	}
	return ret.toString();
    }
}
