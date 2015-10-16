package ch.ethz.inf.vs.a2;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class RestServerService extends Service {
    String DEBUG_TAG = "###RestServerService###";
    ServerSocket server_socket;
    InetAddress address;
    Thread serverThread;
    public RestServerService() {
    }

    class ServerThread implements Runnable {
        public void run(){
            Log.d(DEBUG_TAG, "test");


            while (!Thread.currentThread().isInterrupted()) {

                Socket socket = null;


                try {
                    socket = server_socket.accept();
                    Log.d(DEBUG_TAG, "great");

                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String read = input.readLine();
                    PrintStream output = new PrintStream(socket.getOutputStream(), true);
                    output.println(read);
                    socket.close();
                    Log.d(DEBUG_TAG, read);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(DEBUG_TAG, "not");

                }

            }


        }

    }

    public void onCreate() {
        Log.d(DEBUG_TAG, "service started");

        try {
            server_socket = new ServerSocket(RestServerActivity.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverThread = new Thread(new ServerThread());
        serverThread.start();


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void onDestroy() {
        Log.d(DEBUG_TAG, "Service destroied");

        try {
            serverThread.interrupt();
            server_socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
