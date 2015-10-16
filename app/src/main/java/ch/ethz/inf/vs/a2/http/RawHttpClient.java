package ch.ethz.inf.vs.a2.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jan on 14.10.15.
 *
 * uses java.net.Socket to execute a raw http request and get the result back
 *
 * note: don't call execute from the UI-tread (will trigger NetworkOnMainTreadException)
 *
 */
public class RawHttpClient implements SimpleHttpClient{
    private InetAddress serverAddress;

    @Override
    public String execute(Object request) {
        // first of all let's make a DNS lookup to get the servers IP address
        try {
            serverAddress = InetAddress.getByName(RemoteServerConfiguration.HOST);
        }
        catch (UnknownHostException e){
            Log.d("###", e.toString());
        }

        // now let's execute the command via a socket and get back the result!
        try {
            if(serverAddress == null)
                return null;
            Socket s = new Socket(serverAddress.getHostAddress(), RemoteServerConfiguration.REST_PORT);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.print(request.toString());
            pw.flush();
            BufferedReader buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answerLine;
            StringBuilder sb = new StringBuilder();
            while((answerLine = buf.readLine()) != null) {
                sb.append(answerLine);
            }
            return sb.toString();
        }
        catch (IOException e){
            Log.d("###", e.toString());
        }

        return null;
    }
}
