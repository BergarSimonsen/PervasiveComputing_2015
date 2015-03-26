package dk.itu.spct.itucontextphone.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import dk.itu.spct.itucontextphone.monitor.ContextMonitor;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

public class ContextService extends Service {
    private static final String TAG = "ContextService";

    private ArrayList<ContextMonitor> monitors;
    private ContextServiceBinder binder;

    public ContextService() { }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.doLog(TAG, "onCreate", Const.INFO);
//        if(monitors == null) monitors = new ArrayList<Monitor>();
        binder = new ContextServiceBinder();
        main();
    }

    @Override
    public void onDestroy() {
        Utils.doLog(TAG, "onDestroy", Const.INFO);
        monitors = null;
        binder = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Utils.doLog(TAG, "onBind", Const.INFO);
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.doLog(TAG, "onUnbind", Const.INFO);
        return super.onUnbind(intent);
    }

    public synchronized void registerMonitor(ContextMonitor monitor) {
        monitors.add(monitor);
    }

    public synchronized void unregisterMonitor(ContextMonitor monitor) {
        monitors.remove(monitor);
    }

    private void main() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(ContextMonitor m : monitors) {
                        m.sample();
                        // TODO: complete
                    }
                }
            }
        });
        t.start();
    }

    public class ContextServiceBinder extends Binder {
        public ContextService getService() { return ContextService.this; }
    }
}