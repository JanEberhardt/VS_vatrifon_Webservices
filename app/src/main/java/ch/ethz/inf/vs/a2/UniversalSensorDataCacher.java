package ch.ethz.inf.vs.a2;

import java.util.HashMap;

/**
 * Created by jan on 18.10.15.
 *
 * Stores always the newest sensor readings.
 */
public class UniversalSensorDataCacher {

    private static HashMap<String, float[]> map;

    public static float[] get(int hash){
        if(map == null)
            map = new HashMap<>();
        float[] res = map.get(Integer.toString(hash));
        if(res == null){
            // since in the biggest case we need 6 different data values...
            res = new float[6];
        }
        return res;

    }

    public static void set(int hash, float[] data){
        if(map == null)
            map = new HashMap<>();
        map.put(Integer.toString(hash), data);
    }
}
