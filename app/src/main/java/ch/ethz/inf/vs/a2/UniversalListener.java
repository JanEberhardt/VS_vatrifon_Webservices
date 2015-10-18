package ch.ethz.inf.vs.a2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import ch.ethz.inf.vs.a2.sensor.SensorListener;

/**
 * Created by jan on 18.10.15.
 *
 * stores always the newest values.
 */
public class UniversalListener implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent event) {
        int hash = event.sensor.hashCode();
        UniversalSensorDataCacher.set(hash, event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
