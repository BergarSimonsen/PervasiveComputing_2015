package dk.itu.spct.itucontextphone.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Handler;
import android.os.RemoteException;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import dk.itu.spct.itucontextphone.io.Response;
import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.rest.GetContextEntity;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/5/15.
 */
public class BeaconRangingMonitor implements ContextMonitor, BeaconConsumer {
    private GlobalValues gv;
    private static final String TAG = "BeaconRangingMonitor";
    private BeaconManager beaconManager;
    private BeaconParser parser;

    private String UUID = "19294e1a-c784-11e4-a8eb-4b157ec8f932";

    private Context context;
    private ContextEntityList data;

    public BeaconRangingMonitor(Context context) {
        this.context = context;
        this.data = new ContextEntityList();
        initBeaconManager();
    }

    private void initBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(context);
        gv = GlobalValues.getInstance();

        BeaconParser parser = new BeaconParser();
        parser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(parser);

        beaconManager.bind(BeaconRangingMonitor.this);
        Utils.doLog(TAG, "beacon monitor started", Const.INFO);
    }

    private void destroyBeaconManager() {
        beaconManager.unbind(this);
        beaconManager = null;
        parser = null;
    }

    Handler handler = new Handler();

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Utils.doLog(TAG, "didRangeBeaconsInRegion", Const.INFO);
                if(beacons.size() > 0) {
                    Beacon b = beacons.iterator().next();
                    if(b.getId1() != null) {
                        ContextEntity tmp = toContextEntity(b);
                        Response r = null;
                        try {
                            r = new GetContextEntity().execute(tmp.getId()).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if(r != null) {
                            String msg = Utils.responseCodeToString(r.getResponseCode());
                            Utils.doLog(TAG, msg, Const.INFO);
                            if(r.getDataCount() == 0)
                                data.add(tmp);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateMap();
                            }
                        });
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("uniqueID", null, null, null));
        } catch (RemoteException e) {
            Utils.doLog(TAG, e.getMessage(), Const.ERROR);
        }
    }

    private void updateMap() {
        if(gv.getMap() != null) {
            LatLng ll = new LatLng(55.659890, 12.591188);
            gv.getMap().setMyLocationEnabled(true);
            gv.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
            gv.getMap().addMarker(new MarkerOptions().title("PIBeacon marker").snippet("Marker set by PIBeacon Monitor").position(ll));
        }
    }

    private ContextEntity toContextEntity(Beacon b) {
        ContextEntity ce = new ContextEntity();
        ce.setType(Const.TYPE_PIBEACON);
        ce.setSensor(Const.SENSOR_BLUETOOTH);
        ce.setTimeStamp(Utils.getTimeNow());
        ce.setValue(b.getId1().toString());
        ce.setId(Utils.generateHash(ce));

        return ce;
    }

    public void stopMonitor() {
        destroyBeaconManager();
    }

    @Override
    public Context getApplicationContext() {
        return context;
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

    @Override
    public ContextEntityList sample() {
        ContextEntityList tmp = new ContextEntityList();
        tmp.addAll(data);
        data.clear();
        return tmp;
    }

    @Override
    public String getName() {
        return "BeaconRangingMonitor";
    }
}

