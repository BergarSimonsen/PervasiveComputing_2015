package dk.itu.spct.itucontextphone.monitor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/5/15.
 */
public class AmbientMonitor implements ContextMonitor, SensorEventListener {

    private static final String TAG = "AmbientMonitor";
    private Sensor sensor;
    private SensorManager sensorManager;
    private Context context;
    private ContextEntityList data;

    public AmbientMonitor(Context context) {
        this.context = context;
        this.data = new ContextEntityList();
        initializeSensorManager();
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        startMonitor();
    }

    public void startMonitor() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopMonitor() {
        sensorManager.unregisterListener(this);
        sensorManager = null;
    }

    private void initializeSensorManager() {
        if(context != null)
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        else
            Utils.doLog(TAG, "Could not initialize sensor manager!", Const.ERROR);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.doLog(TAG, "onSensorChanged", Const.INFO);
        Utils.doLog(TAG, event.toString(), Const.INFO);
        data.add(toContextEntity(event));
    }

    private ContextEntity toContextEntity(SensorEvent e) {
        ContextEntity ce = new ContextEntity();
        float[] val = e.values;
        String v = "";
        for(int i = 0; i < val.length; i++) {
            v += String.valueOf(val[i]);
            if(i < val.length - 1) {
                v += "__";
            }
        }
//        ce.setId(Long.valueOf(ce.hashCode()));
        ce.setId(Utils.getRandomId());
        ce.setValue(v);
        ce.setTimeStamp(Utils.getTimeNow());
        ce.setType("Ambient Light");
        ce.setSensor("Type_Light");
        ce.setId(Utils.generateHash(ce));
        return ce;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Utils.doLog(TAG, "onAccuracyChanged", Const.INFO);
        Utils.doLog(TAG, "accuracy: " + accuracy, Const.INFO);
        Utils.doLog(TAG, sensor.toString(), Const.INFO);
    }

    @Override
    public ContextEntityList sample() {
        ContextEntityList tmp = new ContextEntityList();
        tmp.addAll(data);
        data.clear();
        return tmp;
    }

    @Override
    public String getName() {
        return "AmbientMonitor";
    }
}
