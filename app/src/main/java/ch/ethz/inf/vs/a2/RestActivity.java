package ch.ethz.inf.vs.a2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.ethz.inf.vs.a2.sensor.RawHttpSensor;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestActivity extends AppCompatActivity implements SensorListener {

    private TextView reading;
    private RawHttpSensor s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        // get the TextView where we later display the value
        reading = (TextView) findViewById(R.id.restReading);

        // create a new RawHttpSensor and register this class
        s = new RawHttpSensor();
        s.registerListener(this);
        s.getTemperature();

        // set the onclick listener for the refresh button
        Button b = (Button) findViewById(R.id.restRefresh);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestActivity.this.s.getTemperature();
            }
        });
    }

    @Override
    public void onReceiveDouble(double value) {
        reading.setText(Double.toString(value));
        Toast t = Toast.makeText(this, R.string.value_updated_toast, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onReceiveString(String message) {
        Log.d("###", "RestActivity received string:" + message);

    }
}
