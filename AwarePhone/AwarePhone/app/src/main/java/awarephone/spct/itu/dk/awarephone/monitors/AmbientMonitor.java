package awarephone.spct.itu.dk.awarephone.monitors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import awarephone.spct.itu.dk.awarephone.Const;
import awarephone.spct.itu.dk.awarephone.Utils;
import awarephone.spct.itu.dk.awarephone.model.ContextEntity;

public class AmbientMonitor extends Monitor implements SensorEventListener  {

    private static final String TAG = "AmbientMonitor";

    public AmbientMonitor(Context context, String sensorName, String sensorType) {
        super(context, sensorName, sensorType);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public ContextEntity sample() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.doLog(TAG, "onSensorChanged", Const.I);
        Utils.doLog(TAG, event.toString(), Const.I);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Utils.doLog(TAG, "onAccuracyChanged", Const.I);
        Utils.doLog(TAG, "accuracy: " + accuracy, Const.I);
        Utils.doLog(TAG, sensor.toString(), Const.I);
    }

    public static String getTag() {
        return TAG;
    }
}
