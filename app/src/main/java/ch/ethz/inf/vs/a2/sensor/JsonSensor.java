package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.a2.http.HttpRawRequestFactory;

/**
 * Created by jan on 15.10.15.
 *
 * extends RawHttpSensor and just changes the request and how the parsing works,
 * uses the rawRequest.
 *
 */
public class JsonSensor extends RawHttpSensor{

    @Override
    protected Object generateRequest(String host, int port, String path) {
        return HttpRawRequestFactory.getInstance(host, port, path, true).generateRequest();
    }

    @Override
    public double parseResponse(String response) {

        // first let's remove the "HTTP/1.1..." garbage in front of the actual json
        int begin = response.indexOf("{");
        String jsonString = response.substring(begin);

        // now parse the json and get our value!
        try {
            JSONObject jo = new JSONObject(jsonString);
            return jo.getDouble("value");
        } catch (JSONException e) {
            Log.d("###", e.toString());
        }
        return 0;
    }

}
