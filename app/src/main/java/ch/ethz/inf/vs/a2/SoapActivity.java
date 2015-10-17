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

public class SoapActivity extends AppCompatActivity implements SensorListener{

    private TextView reading, currentMethod;
    private Sensor s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap);

        // get the TextView where we later display the value
        reading = (TextView) findViewById(R.id.rest_reading);
        s = SensorFactory.getInstance(SensorFactory.Type.XML);
        s.registerListener(this);
        s.getTemperature();

        currentMethod = (TextView) findViewById(R.id.current_method);
        currentMethod.setText(R.string.xml_method);

        // set the onclick listener for the refresh button
        Button b = (Button) findViewById(R.id.rest_refresh);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoapActivity.this.s.getTemperature();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_soap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xml:
                s.unregisterListener(this);
                s = SensorFactory.getInstance(SensorFactory.Type.XML);
                currentMethod.setText(R.string.xml_method);
                break;
            case R.id.soap:
                s.unregisterListener(this);
                s = SensorFactory.getInstance(SensorFactory.Type.SOAP);
                currentMethod.setText(R.string.soap_method);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        s.registerListener(this);
        s.getTemperature();
        return true;
    }
}
