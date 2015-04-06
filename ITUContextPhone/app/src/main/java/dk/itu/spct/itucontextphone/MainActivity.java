package dk.itu.spct.itucontextphone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import dk.itu.spct.itucontextphone.monitor.AmbientMonitor;
import dk.itu.spct.itucontextphone.monitor.BeaconMonitor;
import dk.itu.spct.itucontextphone.monitor.BeaconRangingMonitor;
import dk.itu.spct.itucontextphone.monitor.ContextMonitor;
import dk.itu.spct.itucontextphone.monitor.LocationMonitor;
import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;


public class MainActivity extends ActionBarActivity {

    private GlobalValues gv;

    private final String TAG = "MainActivity";

    private Button startServiceButton;
    private Button locationMonitorButton;
    private Button ambientMonitorButton;
    private Button pibeaconMonitorButton;
    private Button rangingButton;

    private GoogleMap map;

    private LocationMonitor locationMonitor;
    private AmbientMonitor ambientMonitor;
    private BeaconMonitor beaconMonitor;
    private BeaconRangingMonitor beaconRangingMonitor;

    private boolean serviceIsBound;
    private boolean locationStarted;
    private boolean ambientStarted;
    private boolean beaconStarted;
    private boolean beaconRangingStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = GlobalValues.getInstance();

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.new_map);
        map = mf.getMap();
        gv.setMap(map);

        startServiceButton = (Button) findViewById(R.id.start_service_btn);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleService();
            }
        });

        locationMonitorButton = (Button) findViewById(R.id.start_location_monitor);
        locationMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLocation();
                updateButtonUI();
            }
        });

        pibeaconMonitorButton = (Button) findViewById(R.id.start_pibeacon_monitor);
        pibeaconMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBeacon();
                updateButtonUI();
            }
        });

        rangingButton = (Button) findViewById(R.id.start_ranging_monitor);
        rangingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBeaconRanging();
                updateButtonUI();
            }
        });

        ambientMonitorButton = (Button) findViewById(R.id.start_ambient_monitor);
        ambientMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAmbient();
                updateButtonUI();
            }
        });
    }

    private void toggleBeaconRanging() {
        if(beaconRangingStarted) 
            stopBeaconRangingMonitor();
        else
            startBeaconRangingMonitor();
    }

    private void startBeaconRangingMonitor() {
        beaconRangingMonitor = new BeaconRangingMonitor(MainActivity.this);
        registerMonitor(beaconRangingMonitor);
        beaconRangingStarted = true;
    }

    private void stopBeaconRangingMonitor() {
        if(beaconRangingMonitor != null) {
            beaconRangingMonitor.stopMonitor();
            unregisterMonitor(beaconRangingMonitor);
            beaconRangingMonitor = null;
            beaconRangingStarted = false;
        }
    }

    private void toggleBeacon() {
        if(beaconStarted) {
            stopBeaconMonitor();
        } else {
            startBeaconMonitor();
        }
    }

    private void stopBeaconMonitor() {
        if(beaconMonitor != null) {
            beaconMonitor.stopMonitor();
            unregisterMonitor(beaconMonitor);
            beaconMonitor = null;
            beaconStarted = false;
        }
    }

    private void startBeaconMonitor() {
        beaconMonitor = new BeaconMonitor(MainActivity.this);
        registerMonitor(beaconMonitor);
        beaconStarted = true;
    }

    private void toggleService() {
        if(serviceIsBound)
            unbindContextService();
        else
            bindContextService();
        updateButtonUI();
    }

    private void toggleLocation() {
        if(locationStarted) {
            stopLocationMonitor();
        } else {
            startLocationMonitor();
        }
    }

    private void toggleAmbient() {
        if(ambientStarted)
            stopAmbientMonitor();
        else
            startAmbientMonitor();
    }

    private void startAmbientMonitor() {
        ambientMonitor = new AmbientMonitor(MainActivity.this);
        registerMonitor(ambientMonitor);
        ambientStarted = true;
    }

    private void stopAmbientMonitor() {
        if(ambientMonitor != null) {
            ambientMonitor.stopMonitor();
            unregisterMonitor(ambientMonitor);
            ambientMonitor = null;
            ambientStarted = false;
        }
    }

    private void startLocationMonitor() {
        locationMonitor = new LocationMonitor(MainActivity.this);
        registerMonitor(locationMonitor);
        locationStarted = true;
    }

    private void stopLocationMonitor() {
        if(locationMonitor != null) {
            locationMonitor.stopLocationUpdates();
            locationMonitor.stopClient();
            unregisterMonitor(locationMonitor);
            locationMonitor = null;
            locationStarted = false;
        }
    }

    private void registerMonitor(ContextMonitor m) {
        if(gv.getService() != null) {
            gv.getService().registerMonitor(m);
        }
    }

    private void unregisterMonitor(ContextMonitor m) {
        if(gv.getService() != null && serviceIsBound) {
            gv.getService().unregisterMonitor(m.getName());
        }
    }

    private void updateButtonUI() {
        startServiceButton.setText(serviceIsBound ? "Stop Service" : "Start Service");
        locationMonitorButton.setText(locationStarted ? "Stop Location Monitor" : "Start Location Monitor");
        ambientMonitorButton.setText(ambientStarted ? "Stop Ambient Monitor" : "Start Ambient Monitor");
        pibeaconMonitorButton.setText(beaconStarted ? "Stop PIBeacon Monitor" : "Start PIBeacon Monitor");
        rangingButton.setText(beaconRangingStarted ? "Stop PIBeacon Ranging Monitor" : "Start PIBeacon Ranging Monitor");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Utils.doLog(TAG, "onServiceConnected", Const.INFO);
            ContextService.ContextServiceBinder binder = (ContextService.ContextServiceBinder) service;
            gv.setService(binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.doLog(TAG, "onServiceDisconnected", Const.INFO);
            GlobalValues.getInstance().setService(null);
        }
    };

    private void unbindContextService() {
        gv.getService().stop();
        getApplicationContext().unbindService(connection);
        Utils.doLog(TAG, "Context service unbound!", Const.INFO);
        serviceIsBound = false;
    }

    private void bindContextService() {
        Utils.doLog(TAG, "bindService", Const.INFO);
        Intent intent = new Intent(this, ContextService.class);
        if(!getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            Utils.doLog(TAG, "error binding service!", Const.INFO);
        } else {
            Utils.doLog(TAG, "service successfully bound", Const.INFO);
            serviceIsBound = true;
        }
    }
}
