package ch.ethz.inf.vs.a2.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute((HttpGet) request);
            BufferedReader buf =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String answerLine;
            StringBuilder sb = new StringBuilder();
            while((answerLine = buf.readLine()) != null)
                sb.append(answerLine);
            return sb.toString();
        } catch (IOException e) {
            Log.d("###", e.toString());
        }
        return null;
    }
}
