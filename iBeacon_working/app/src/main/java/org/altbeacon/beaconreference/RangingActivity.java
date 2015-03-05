package org.altbeacon.beaconreference;

import java.math.BigDecimal;
import java.util.Collection;

import android.app.Activity;

import android.os.Bundle;
import android.os.RemoteException;
//import android.util.Log;
import android.widget.EditText;

//import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
//import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class RangingActivity extends Activity implements BeaconConsumer {
    //protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranging);

        beaconManager.bind(this);
    }
    @Override 
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override 
    protected void onPause() {
    	super.onPause();
    	if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
        @Override 
        public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
            if (beacons.size() > 0) {
                Beacon aBeacon = beacons.iterator().next();
                String beaconLocation = "unknown location";
                // based on the id of the beacon, we can get the approximate location of the device.
                // This will usually be queried from a web db, but for our example, I can hardcode it here.
                // Also I only check id3, because id1 and id2 will be hardcoded, as the device will only
                // look for our own deployed beacons which have the same global id
                if(aBeacon.getId3().toString().equals("4242")) beaconLocation = "desk 5G02 (PitLab)";

                // get the distance and set precision of 2 decimal digits for better readability
                Double distance = new BigDecimal(aBeacon.getDistance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                //logToDisplay("I see a beacon with id3: "+aBeacon.getId3()+" which is about "+aBeacon.getDistance()+" meters away.");
//              logToDisplay("I see a beacon with id's: \n[ "+aBeacon.toString()+" ]\n which is about "+aBeacon.getDistance()+" meters away." +
//                        " Other info: Manufacturer:" + aBeacon.getManufacturer() + " ; and id3 is: " + aBeacon.getId3());
                logToDisplay("I am "+ distance + " meters away from " + beaconLocation + " unique ID is: " + aBeacon.getId1());
            }
            else {
                logToDisplay("... waiting for a beacon signal ...");
                //logToDisplay("i cant see none of them beacons...");
            }
        }

        });

        try {
            String uniqueid = "Unique ID, which I don't use right now";
            // by setting the unique id's I can make it find only a specific beacon
            Identifier id1 = Identifier.parse("8DEEFBB9-F738-4297-8040-96668BB44281");
            Identifier id2 = Identifier.parse("5000");
            Identifier id3 = Identifier.parse("4242");
            //beaconManager.startRangingBeaconsInRegion(new Region(uniqueid, id1, id2, id3));
            beaconManager.startRangingBeaconsInRegion(new Region(uniqueid, null, null, null));
        } catch (RemoteException e) { logToDisplay("ERROR!"); }
    }
    private void logToDisplay(final String line) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	EditText editText = (EditText)RangingActivity.this
    					.findViewById(R.id.rangingText);
    	    	editText.append(line+"\n");            	
    	    }
    	});
    }
}
