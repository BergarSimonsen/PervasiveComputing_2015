package awarephone.spct.itu.dk.awarephone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import awarephone.spct.itu.dk.awarephone.monitors.BaseMonitor;

public class ContextService extends Service {

    private static final String TAG = "ContextService";

    private ArrayList<BaseMonitor> monitors;
    private ContextServiceBinder binder;

    public ContextService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.doLog(TAG, "onCreate", Const.I);
        if(monitors == null) monitors = new ArrayList<BaseMonitor>();
        binder = new ContextServiceBinder();
//        main();
    }

    private void main() {
        while(true) {
            // TODO: Implement main loop
        }
    }

    public synchronized void registerMonitor(BaseMonitor monitor) {
        monitors.add(monitor);
    }

    public synchronized void unregisterMonitor(BaseMonitor monitor) {
        monitors.remove(monitor);
    }

    @Override
    public void onDestroy() {
        Utils.doLog(TAG, "onDestroy", Const.I);
        monitors = null;
        binder = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Utils.doLog(TAG, "onBind", Const.I);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.doLog(TAG, "onUnbind", Const.I);
        return super.onUnbind(intent);
    }

    public class ContextServiceBinder extends Binder {
        public ContextService getService() { return ContextService.this; }
    }
}
