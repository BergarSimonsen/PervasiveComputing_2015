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

public class AccelerationMonitor implements ContextMonitor, SensorEventListener {

    private static final String TAG = "AccelerationMonitor";
    private SensorManager sensorManager;
    private Context context;
    private Sensor senAccelerometer;
    private ContextEntityList data;

    private long lastUpdate = 0;

    public void startMonitor() {
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopMonitor() {
        sensorManager.unregisterListener(this);
        sensorManager = null;
    }

    public AccelerationMonitor(Context context) {
        this.context = context;
        this.data = new ContextEntityList();
        initializeSensorManager();

        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startMonitor();
    }

    private void initializeSensorManager() {
        if (context != null)
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        else
            Utils.doLog(TAG, "Could not initialize sensor manager!", Const.ERROR);
    }

    @Override
    public ContextEntityList sample() {
        ContextEntityList tmp = new ContextEntityList();
        tmp.addAll(data);
        data.clear();
        Utils.doLog(TAG, "Service is sampling accelerometer data.", Const.INFO);
        return tmp;
    }

    @Override
    public String getName() {
        return "Acceleration monitor";
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Utils.doLog(TAG, "onSensorChanged - acceleration", Const.INFO);

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > Const.ACC_SAMPLE_RATE) {
            Utils.doLog(TAG, "Adding accelerometer data to the list.", Const.INFO);
            data.add(toContextEntity(sensorEvent));

            lastUpdate = curTime;
        }
    }

    private ContextEntity toContextEntity(SensorEvent e) {
        ContextEntity ce = new ContextEntity();
        float[] val = e.values;

        String v = "";
        for (int i = 0; i < val.length; i++) {
            v += String.valueOf(val[i]);
            if (i < val.length - 1) {
                v += "__";
            }
        }
        ce.setId(Utils.getRandomId());
        ce.setValue(v);
        ce.setTimeStamp(Utils.getTimeNow());
        ce.setType("Acceleration");
        ce.setSensor("Accelerometer");
        return ce;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Utils.doLog(TAG, "onAccuracyChanged", Const.INFO);
        Utils.doLog(TAG, "accuracy: " + accuracy, Const.INFO);
    }
}
