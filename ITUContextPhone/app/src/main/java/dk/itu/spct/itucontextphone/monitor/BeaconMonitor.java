package dk.itu.spct.itucontextphone.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/5/15.
 */
public class BeaconMonitor implements ContextMonitor, BeaconConsumer {

    private static final String TAG = "BeaconMonitor";
    private Context context;
    private BeaconManager beaconManager;
    private BeaconParser parser;
    private ContextEntityList data;

    public BeaconMonitor(Context context) {
        this.context = context;
        data = new ContextEntityList();
        initBeaconManager();
    }

    public void stopMonitor() {
        destroyBeaconManager();
    }

    private void initBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(context);

        BeaconParser parser = new BeaconParser();
        parser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(parser);

        beaconManager.bind(BeaconMonitor.this);
        Utils.doLog(TAG, "beacon monitor started", Const.INFO);
    }

    private void destroyBeaconManager() {
        beaconManager.unbind(this);
        beaconManager = null;
        parser = null;
    }

    private ContextEntity toContextEntity(Region r) {
        ContextEntity ce = new ContextEntity();
        ce.setType("Type_PIBeacon");
        ce.setSensor("Bluetooth");
        ce.setTimeStamp(Utils.getTimeNow());
        ce.setValue(r.getId1().toString());
        ce.setId(Utils.generateHash(ce));

        return ce;
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
        return "BeaconMonitor";
    }

    @Override
    public void onBeaconServiceConnect() {
        Utils.doLog(TAG, "onBeaconServiceConnect", Const.INFO);
        beaconManager.setMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                Utils.doLog(TAG, "I just saw an beacon for the first time!", Const.INFO);
                if(region != null && region.getId1() != null)
                    data.add(toContextEntity(region));
            }

            @Override
            public void didExitRegion(Region region) {
                Utils.doLog(TAG, "I no longer see an beacon", Const.INFO);
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Utils.doLog(TAG, "I have just switched from seeing/not seeing beacons: " + state, Const.INFO);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
            Utils.doLog(TAG, "startedMonitorBeacons", Const.INFO);
        } catch (RemoteException e) { Log.i(TAG, e.getMessage());  }
    }

    @Override
    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
        Utils.doLog(TAG, "unbindService", Const.INFO);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        Utils.doLog(TAG, "bindService", Const.INFO);
        return context.bindService(intent, serviceConnection, i);
    }
}