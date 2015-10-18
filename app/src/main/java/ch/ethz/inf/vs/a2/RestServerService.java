package ch.ethz.inf.vs.a2;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RestServerService extends Service {
    String DEBUG_TAG = "###RestServerService###";
    ServerSocket server_socket;
    InetAddress address;
    Thread serverThread;
    List<Sensor> sensorList;
    MediaPlayer mp;
    Vibrator vibrator;
    public Boolean serverRunning;

    public RestServerService() {
    }

    class ServerThread implements Runnable {
        public void run() {
            Log.d(DEBUG_TAG, "test");


            while (!Thread.currentThread().isInterrupted()) {

                Socket socket = null;


                try {
                    socket = server_socket.accept();
                    Thread responseThread = new Thread(new ResponseThread(socket));
                    responseThread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(DEBUG_TAG, "not");
                }

            }


        }

    }


    class ResponseThread implements Runnable {
        Socket socket;

        public ResponseThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Log.d(DEBUG_TAG, "great");

            BufferedReader input = null;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String request = input.readLine();

                String response = handleRequest(request);

                PrintStream output = new PrintStream(socket.getOutputStream(), true);
                output.println(response);
                socket.close();
                Log.d(DEBUG_TAG, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate() {
        serverRunning = true;
        Log.d(DEBUG_TAG, "service started");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        try {
            server_socket = new ServerSocket(RestServerActivity.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverThread = new Thread(new ServerThread());
        serverThread.start();

        SensorManager sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        List<android.hardware.Sensor> sensors = sm.getSensorList(android.hardware.Sensor.TYPE_ALL);
        Iterator iterator = sensors.iterator();

        sensorList = new ArrayList<Sensor>();

        while (iterator.hasNext()) {
            sensorList.add((Sensor) iterator.next());
        }

    }


    public String handleRequest(String request) {
        String response = "";
        if (request.substring(0, 3).contains("GET")) {
            String[] path = request.substring(3, request.indexOf("HTTP/1.1") - 1).split("/");
            if (path.length > 2) {
                if (path[1].contains("sensors")) {
                    return generateHTML(generateSensorHTML(path[2]));
                } else if (path.length > 3) {
                    if (path[1].contains("actuators")) {
                        if (path[2].contains("vibra")) {
                            return generateHTML(generateVibraHTML(path[3]));
                        } else if (path[2].contains("sound")) {
                            return generateHTML(generateSoundHTML(path[3]));
                        } else {
                            return generateHTML("The actuator " + path[2] + " is not available. Please refer to the index:</br>" + generateIndex());
                        }
                    }
                }
                return generateHTML("The method '" + path[2] + "' is not supported. Please refer to the index: </br>" + generateIndex());
            }
            return generateHTML(generateIndex());
        }

        return "Only GET-Requests are supported";

    }

    private String generateSoundHTML(String repeat) {
        if (repeat.contains("loop")) {
            playSound(true);
            return "<h1>Sound</h1></br>You choose to loop. Click here to end: <a href='/actuators/sound/stop'>STOP!</a>";
        } else if (repeat.contains("once")) {
            playSound(false);
            return "<h1>Sound</h1></br>You choose to play the sound once.";
        } else if (repeat.contains("stop")) {
            stopSound();
            return "<h1>Sound</h1></br>Ai Ai sir, no sound no more ;)";
        } else {
            return "<h1>Sound</h1></br>The repeat-Option '" + repeat + "' is not available. Use 'once', 'loop' or 'stop' or refer to the index: </br>" + generateIndex();
        }
    }

    private void playSound(Boolean repeat) {

        if (mp != null) {
            mp.stop();
        }

        int sound;
        if (repeat)
            sound = R.raw.loop;
        else
            sound = R.raw.sound;
        mp = MediaPlayer.create(this, sound);
        mp.setVolume(1.0f, 1.0f);
        mp.setLooping(repeat);

        mp.start();

    }

    private void stopSound() {
        mp.stop();
        try {
            mp.prepareAsync();
        } catch (IllegalStateException e) {
            Log.e(DEBUG_TAG, e.toString());
        }

    }

    private String generateVibraHTML(String timeString) {

        int time;
        try {
            time = Integer.parseInt(timeString);
        } catch (NumberFormatException e) {
            return generateHTML("The duration has to be an Integer, but was '"+timeString+"'. Please check your input or refer to the index:</br>" + generateIndex());
        }

        vibrator.vibrate(time);

        return "<h1>Vibrator</h1></br>You choose to vibrate for "+time+" ms.";
    }

    private String generateHTML(String content) {
        return "<html><head></head><body>" + content + "</body><html>";
    }

    private String generateIndex() {

        String result = "<h1>Index</h1>";
        result += "<h2>Sensors</h2>";

        for (Sensor sensor : sensorList) {
            result += "<a href='/sensors/" + sensor.hashCode() + "'>" + sensor.getName() + "</a></br>";
        }

        result += "<h2>Actuators</h2>";
        result += "<a href='/actuators/vibra/200'>Vibrator 200ms</a></br>";
        result += "<a href='/actuators/vibra/500'>Vibrator 500ms</a></br>";
        result += "<a href='/actuators/sound/once'>Sound (once)</a></br>";
        result += "<a href='/actuators/sound/loop'>Sound (loop)</a></br>";


        return result;
    }


    private String generateSensorHTML(String hash) {

        int hashCode;
        try {
            hashCode = Integer.parseInt(hash);
        } catch (NumberFormatException e) {
            return generateHTML("The Sensor with the Hash '" + hash + "' could not be found, as hash has to be an Integers. Please check your input or refer to the index:</br>" + generateIndex());
        }

        String response = "";
        for (Sensor sensor : sensorList) {
            if (sensor.hashCode() == hashCode) {
                response += "<h1>" + sensor.getName() + "</h1>";
                response += "<b>Vendor: </b>" + sensor.getVendor() + "</br>";
                response += "<b>Version: </b>" + sensor.getVersion() + "</br>";
                response += "<b>Power: </b>" + sensor.getPower() + " mA</br>";
                response += "<b>Minimum Delay: </b>" + sensor.getMinDelay() + " ms</br>";
                response += "<b>Maximum Range: </b>" + sensor.getMaximumRange() + "</br>";
                response += "<b>Resolution: </b>" + sensor.getResolution() + "</br>";


                int sensorType = sensor.getType();
                SensorData[] res;
                switch (sensorType) {
                    case Sensor.TYPE_ACCELEROMETER:
                    case Sensor.TYPE_LINEAR_ACCELERATION:
                    case Sensor.TYPE_GRAVITY:
                        res = new SensorData[3];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_mpss);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_mpss);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_mpss);
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        res = new SensorData[3];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_radps);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_radps);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_radps);
                        break;
                    case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                        res = new SensorData[6];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_radps);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_radps);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_radps);
                        res[3] = new SensorData(R.string.sensor_drift_x, 0, R.string.sensor_radps);
                        res[4] = new SensorData(R.string.sensor_drift_y, 0, R.string.sensor_radps);
                        res[5] = new SensorData(R.string.sensor_drift_z, 0, R.string.sensor_radps);
                        break;
                    case Sensor.TYPE_ROTATION_VECTOR:
                        res = new SensorData[4];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_unitless);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_unitless);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_unitless);
                        res[3] = new SensorData(R.string.sensor_scalar_component, 0, R.string.sensor_unitless);
                        break;
                    case Sensor.TYPE_STEP_COUNTER:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_steps, 0, R.string.sensor_no_unit);
                        break;
                    case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                        res = new SensorData[3];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_unitless);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_unitless);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_unitless);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        res = new SensorData[3];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_mutesla);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_mutesla);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_mutesla);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                        res = new SensorData[6];
                        res[0] = new SensorData(R.string.sensor_x, 0, R.string.sensor_mutesla);
                        res[1] = new SensorData(R.string.sensor_y, 0, R.string.sensor_mutesla);
                        res[2] = new SensorData(R.string.sensor_z, 0, R.string.sensor_mutesla);
                        res[3] = new SensorData(R.string.sensor_iron_bias_x, 0, R.string.sensor_mutesla);
                        res[4] = new SensorData(R.string.sensor_iron_bias_y, 0, R.string.sensor_mutesla);
                        res[5] = new SensorData(R.string.sensor_iron_bias_z, 0, R.string.sensor_mutesla);
                        break;
                    case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    case Sensor.TYPE_TEMPERATURE:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_temp, 0, R.string.sensor_degreecel);
                        break;
                    case Sensor.TYPE_LIGHT:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_light, 0, R.string.sensor_lux);
                        break;
                    case Sensor.TYPE_PRESSURE:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_pressure, 0, R.string.sensor_hpa);
                        break;
                    case Sensor.TYPE_PROXIMITY:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_distance, 0, R.string.sensor_cm);
                        break;
                    case Sensor.TYPE_RELATIVE_HUMIDITY:
                        res = new SensorData[1];
                        res[0] = new SensorData(R.string.sensor_humidity, 0, R.string.sensor_relpercent);
                        break;
                    // default case, just print the sensor readings...
                    default:
                        res = new SensorData[3];
                        res[0] = new SensorData(R.string.sensor_undefined, 0, R.string.sensor_no_unit);
                        res[1] = new SensorData(R.string.sensor_undefined, 0, R.string.sensor_no_unit);
                        res[2] = new SensorData(R.string.sensor_undefined, 0, R.string.sensor_no_unit);
                }


                for (SensorData resource : res){
                    response += "<b>"+getString(resource.name)+": </b>" + resource.value + " "+getString(resource.unit)+"</br>";
                }

                return response;
            }
        }
        return generateHTML("The Sensor with the Hash '" + hash + "' could not be found. Please check your input or refer to the index:</br>" + generateIndex());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void onDestroy() {
        serverRunning = false;
        Log.d(DEBUG_TAG, "Service destroied");

        try {
            serverThread.interrupt();
            server_socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
