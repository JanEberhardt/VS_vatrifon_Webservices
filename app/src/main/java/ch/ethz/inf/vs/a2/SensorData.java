package ch.ethz.inf.vs.a2;

/**
 * Created by jan on 25.09.15.
 * <p/>
 * Object of this type holds one measured value including the name and unit.
 */

public class SensorData {
    public int name;
    public float value;
    public int unit;

    public SensorData(int name, float value, int unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }
}
