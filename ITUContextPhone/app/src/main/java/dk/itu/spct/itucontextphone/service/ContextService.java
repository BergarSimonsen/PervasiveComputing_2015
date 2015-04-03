package dk.itu.spct.itucontextphone.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.monitor.ContextMonitor;
import dk.itu.spct.itucontextphone.rest.PostContextEntity;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

public class ContextService extends Service {
    private static final String TAG = "ContextService";

    private ArrayList<ContextMonitor> monitors;
    private ContextServiceBinder binder;
    private ContextEntityList data;

    public ContextService() { }

    private boolean running = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.doLog(TAG, "onCreate", Const.INFO);
        if(monitors == null) monitors = new ArrayList<ContextMonitor>();
        if(data == null) data = new ContextEntityList();
        binder = new ContextServiceBinder();
        running = true;
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

    public synchronized void stop() {
        running = false;
    }

    private synchronized void postData() {
        ContextEntityList tmp = new ContextEntityList();
        tmp.addAll(data);
        data.clear();
        int tmpCount = tmp.size();
        new PostContextEntity().execute(tmp);
        Utils.doLog(TAG, tmpCount + " entries sent to backend", Const.INFO);
    }

    private synchronized void addToData(ContextEntityList list) {
        Utils.doLog("ContextService", "adding to data " + list.size() + " data.size() " + data.size(), Const.INFO);
        data.addAll(list);
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
                while(running) {
                    try {
//                        Utils.doLog(TAG, "Service is running in main loop!", Const.INFO);
                        Thread.sleep(Const.THREAD_SLEEP);
                        if(!running) break;
                        for(ContextMonitor m : monitors) {
                            ContextEntityList tmp = m.sample();
                            Utils.doLog("ContextService", "tmp size: " + tmp.size(), Const.INFO);
                            addToData(tmp);
                            Utils.doLog(TAG, "doneSampling " + m.getName() + " data.size(): " + data.size(), Const.INFO);
                            // TODO: complete
                        }
                        if(data.size() >= Const.DATA_LIMIT) {
                            postData();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Utils.doLog(TAG, e.getMessage(), Const.ERROR);
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
