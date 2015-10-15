package ch.ethz.inf.vs.a2.sensor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;
import ch.ethz.inf.vs.a2.http.HttpRawRequestFactory;
import ch.ethz.inf.vs.a2.http.RawHttpClient;
import ch.ethz.inf.vs.a2.http.RemoteServerConfiguration;

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
        this.httpClient = new RawHttpClient();
    }

    @Override
    public double parseResponse(String response) {
        // we parse the html string by using Jsoup!
        // this a an external library that we added in the libs folder
        Document doc = Jsoup.parse(response);
        Elements es = doc.getElementsByClass("getterValue");
        String res = es.get(0).html();
        return Double.parseDouble(res);
    }

    @Override
    public void getTemperature() throws NullPointerException {
        String host = RemoteServerConfiguration.HOST;
        int port = RemoteServerConfiguration.REST_PORT;

        // todo: put this in a nicer place maybe?
        String path = "/sunspots/Spot1/sensors/temperature";

        HttpRawRequest r = HttpRawRequestFactory.getInstance(host, port, path);
        new AsyncWorker().execute(r.generateRequest());
    }
}
