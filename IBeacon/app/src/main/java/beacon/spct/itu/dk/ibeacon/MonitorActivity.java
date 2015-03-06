package beacon.spct.itu.dk.ibeacon;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class MonitorActivity extends ActionBarActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private Context context;
    private BeaconManager beaconManager;
    private BeaconParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        context = this;
        initBeaconManager();
    }

    private void initBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this);

        BeaconParser parser = new BeaconParser();
        parser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(parser);

        beaconManager.bind(this);
    }

    private void destroyBeaconManager() {
        beaconManager.unbind(this);
        beaconManager = null;
        parser = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBeaconManager();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {

            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
                Toast.makeText(context, "I just saw an beacon for the first time!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                Toast.makeText(context, "I no longer see an beacon", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
                Toast.makeText(context, "I have just switched from seeing/not seeing beacons: "+state, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) { Log.i(TAG, e.getMessage());  }
    }

}