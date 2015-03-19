package awarephone.spct.itu.dk.awarephone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import awarephone.spct.itu.dk.awarephone.monitors.Monitor;

public class ContextService extends Service {

    private static final String TAG = "ContextService";

    private ArrayList<Monitor> monitors;
    private ContextServiceBinder binder;

    public ContextService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.doLog(TAG, "onCreate", Const.I);
        if(monitors == null) monitors = new ArrayList<Monitor>();
        binder = new ContextServiceBinder();
        main();
    }

    private void main() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(Monitor m : monitors) {
                        m.sample();
                        // TODO: complete
                    }
                }
            }
        });
        t.start();
    }

    public synchronized void registerMonitor(Monitor monitor) {
        monitors.add(monitor);
    }

    public synchronized void unregisterMonitor(Monitor monitor) {
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
