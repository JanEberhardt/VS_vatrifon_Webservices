package ch.ethz.inf.vs.a2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.ethz.inf.vs.a2.sensor.Sensor;
import ch.ethz.inf.vs.a2.sensor.SensorFactory;
import ch.ethz.inf.vs.a2.sensor.SensorListener;

public class RestActivity extends AppCompatActivity implements SensorListener {

    private TextView reading, currentMethod;
    private Sensor s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        // get the TextView where we display the method to get the values from the server
        currentMethod = (TextView) findViewById(R.id.current_method);
        currentMethod.setText(R.string.raw_method);

        // get the TextView where we later display the value
        reading = (TextView) findViewById(R.id.rest_reading);

        // create a new RawHttpSensor and register this class
        // later the user can switch between the sensors by using the menu
        s = SensorFactory.getInstance(SensorFactory.Type.RAW_HTTP);
        s.registerListener(this);
        s.getTemperature();

        // set the onclick listener for the refresh button
        Button b = (Button) findViewById(R.id.rest_refresh);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestActivity.this.s.getTemperature();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        s.unregisterListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        s.registerListener(this);
        s.getTemperature();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.raw:
                s.unregisterListener(this);
                s = SensorFactory.getInstance(SensorFactory.Type.RAW_HTTP);
                currentMethod.setText(R.string.raw_method);
                break;
            case R.id.html:
                s.unregisterListener(this);
                s = SensorFactory.getInstance(SensorFactory.Type.HTML);
                currentMethod.setText(R.string.html_method);
                break;
            case R.id.json:
                s.unregisterListener(this);
                s = SensorFactory.getInstance(SensorFactory.Type.JSON);
                currentMethod.setText(R.string.json_method);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        s.registerListener(this);
        s.getTemperature();
        return true;
    }
}
