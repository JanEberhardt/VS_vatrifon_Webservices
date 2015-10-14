package ch.ethz.inf.vs.a2.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jan on 14.10.15.
 */
public class RawHttpClient implements SimpleHttpClient{
    public static final String serverName = "vslab.inf.ethz.ch";
    public static final int serverPort = 8081;
    public InetAddress serverAddress;

    @Override
    public String execute(Object request) {
        try {
            serverAddress = InetAddress.getByName(serverName);
        }
        catch (UnknownHostException e){
            Log.d("###", e.toString());
        }

        try {
            Socket s = new Socket(serverAddress, serverPort);
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            Log.d("###", answer);
        }
        catch (IOException e){
            Log.d("###", e.toString());
        }

        return null;
    }
}
