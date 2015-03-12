package awarephone.spct.itu.dk.awarephone.monitors;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import awarephone.spct.itu.dk.awarephone.Const;
import awarephone.spct.itu.dk.awarephone.ContextService;
import awarephone.spct.itu.dk.awarephone.MainActivity;
import awarephone.spct.itu.dk.awarephone.R;
import awarephone.spct.itu.dk.awarephone.Utils;

public class AmbientMonitor extends BaseMonitor implements SensorEventListener  {

    private static final String TAG = "AmbientMonitor";

    public AmbientMonitor(Context context, String sensorName, String sensorType) {
        super(context, sensorName, sensorType);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
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
