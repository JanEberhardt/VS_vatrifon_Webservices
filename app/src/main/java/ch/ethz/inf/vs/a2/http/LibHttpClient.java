package ch.ethz.inf.vs.a2.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jan on 15.10.15.
 *
 * uses the apache HttpClient to get the HTML back
 */
public class LibHttpClient implements SimpleHttpClient {

    @Override
    public String execute(Object request) {
        try {
            Log.d("###", "LibHttpClient.execute()");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute((HttpUriRequest) request);
            Log.d("###", "status: "+response.getStatusLine().toString());
            BufferedReader buf =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String answerLine;
            StringBuilder sb = new StringBuilder();
            while((answerLine = buf.readLine()) != null)
                sb.append(answerLine);
            Log.d("###", "execute res:"+sb.toString());
            return sb.toString();
        } catch (IOException e) {
            Log.d("###asdf", e.toString());
        }
        return null;
    }
}
