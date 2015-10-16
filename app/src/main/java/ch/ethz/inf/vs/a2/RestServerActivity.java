package ch.ethz.inf.vs.a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class RestServerActivity extends AppCompatActivity {

    Boolean serverRunning = false;
    String serverIP = "";
    public static int SERVER_PORT = 8088;
    Button startServer;
    Button stopServer;
    TextView status;
    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_server);

        service = new Intent(this, RestServerService.class);

        startServer = (Button) findViewById(R.id.start_server_btn);
        stopServer = (Button) findViewById(R.id.stop_server_btn_server_btn);
        status = (TextView) findViewById(R.id.status_tv);

        if (serverRunning) {
            startServer.setVisibility(View.GONE);
            stopServer.setVisibility(View.VISIBLE);
            status.setText("The Server is running on IP " + serverIP + " on port " + SERVER_PORT);
        } else {
            startServer.setVisibility(View.VISIBLE);
            stopServer.setVisibility(View.GONE);
            status.setText("The Server is currently not running.");
        }


        startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(service);
                serverRunning = true;
                startServer.setVisibility(View.GONE);
                stopServer.setVisibility(View.VISIBLE);

                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    for (NetworkInterface i : Collections.list(interfaces)) {
                        if (i.getDisplayName().contains("wlan0")) {
                            for (InetAddress address : Collections.list(i.getInetAddresses())) {
                                if(address.toString().length()<20) {
                                    serverIP = address.toString().substring(1);
                                }
                            }
                        }

                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


                status.setText("The Server is running on IP " + serverIP + " on port " + SERVER_PORT);
            }
        });

        stopServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(service);
                serverRunning = false;
                startServer.setVisibility(View.VISIBLE);
                stopServer.setVisibility(View.GONE);
                status.setText("The Server is not running.");
            }
        });

    }
}
