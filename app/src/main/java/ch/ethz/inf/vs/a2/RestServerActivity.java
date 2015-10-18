package ch.ethz.inf.vs.a2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class RestServerActivity extends AppCompatActivity {

    Boolean serverRunning = false;
    String serverIP = "";
    public static int SERVER_PORT = 8088;
    Button startServer;
    Button stopServer;
    TextView status;
    Intent service = null;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_server);


        startServer = (Button) findViewById(R.id.start_server_btn);
        stopServer = (Button) findViewById(R.id.stop_server_btn_server_btn);
        status = (TextView) findViewById(R.id.status_tv);

        service = new Intent(this, RestServerService.class);

        startServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // initialize the notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("REST SERVER")
                                .setContentText("Click to manage the Server");

                Intent resultIntent = new Intent(getApplicationContext(), RestServerActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getApplicationContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                // don't let the notification go away
                mBuilder.setOngoing(true);

                mBuilder.setContentIntent(resultPendingIntent);
                notificationManager.notify(1, mBuilder.build());

                startService(service);
                serverRunning = true;
                startServer.setVisibility(View.GONE);
                stopServer.setVisibility(View.VISIBLE);

                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    for (NetworkInterface i : Collections.list(interfaces)) {
                        if (i.getDisplayName().contains("wlan0")) {
                            for (InetAddress address : Collections.list(i.getInetAddresses())) {
                                if (address.toString().length() < 20) {
                                    serverIP = address.toString().substring(1);
                                }
                            }
                        }

                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


                String temp = "The Server is running on IP " + serverIP + " on port " + SERVER_PORT;
                status.setText(temp);
            }
        });

        stopServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(1);

                stopService(service);
                serverRunning = false;
                startServer.setVisibility(View.VISIBLE);
                stopServer.setVisibility(View.GONE);
                String temp = "The Server is not running.";
                status.setText(temp);
            }
        });

        startServer.performClick();


    }
}
