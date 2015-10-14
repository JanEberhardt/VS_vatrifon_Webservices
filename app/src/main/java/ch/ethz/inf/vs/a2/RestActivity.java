package ch.ethz.inf.vs.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestActivity extends AppCompatActivity implements SensorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
    }

    @Override
    public void onReceiveDouble(double value) {
        Log.d("###", "received double: " + value);

    }

    @Override
    public void onReceiveString(String message) {
        Log.d("###", "received string:" + message);

    }
}
