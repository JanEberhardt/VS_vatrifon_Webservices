package ch.ethz.inf.vs.a2.sensor;

import org.apache.http.client.methods.HttpGet;

import ch.ethz.inf.vs.a2.http.SimpleHttpClientFactory;

/**
 * Created by jan on 15.10.15.
 *
 * extends RawHttpSensor and just changes the client that is used, parsing is inherited from
 * RawHttpSensor.
 */
public class HtmlSensor extends RawHttpSensor {

    @Override
    protected void setHttpClient() {
        this.httpClient = SimpleHttpClientFactory.getInstance(SimpleHttpClientFactory.Type.LIB);
    }

    @Override
    protected Object generateRequest(String host, int port, String path) {
        return new HttpGet("http://"+host+":"+port+path);
    }


}
