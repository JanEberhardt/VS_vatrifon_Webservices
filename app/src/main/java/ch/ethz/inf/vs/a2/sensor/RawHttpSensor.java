package ch.ethz.inf.vs.a2.sensor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ch.ethz.inf.vs.a2.http.HttpRawRequestFactory;
import ch.ethz.inf.vs.a2.http.RemoteServerConfiguration;
import ch.ethz.inf.vs.a2.http.SimpleHttpClientFactory;

/**
 * Created by jan on 14.10.15.
 *
 * starts AsyncWorker to get the temperature reading and parses the html response using
 * Jsoup.
 *
 */
public class RawHttpSensor extends AbstractSensor{

    @Override
    protected void setHttpClient() {
        this.httpClient = SimpleHttpClientFactory.getInstance(SimpleHttpClientFactory.Type.RAW);
    }

    protected Object generateRequest(String host, int port, String path) {
        return HttpRawRequestFactory.getInstance(host, port, path).generateRequest();
    }

    @Override
    public double parseResponse(String response) {
        // we parse the html string by using Jsoup!
        // this a an external library that we added in the libs folder
        if(response == null)
            return -999.00;
        Document doc = Jsoup.parse(response);
        // finds spans with class getterValue
        Elements es = doc.select("span.getterValue");
        String res = es.get(0).html();
        return Double.parseDouble(res);
    }

    @Override
    public void getTemperature() throws NullPointerException {
        String host = RemoteServerConfiguration.HOST;
        int port = RemoteServerConfiguration.REST_PORT;
        String path = RemoteServerConfiguration.SPOT_1_TEMP_URL;

        Object req = generateRequest(host, port, path);
        new AsyncWorker().execute(req);
    }
}
