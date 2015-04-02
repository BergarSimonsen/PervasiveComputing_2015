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

import dk.itu.spct.itucontextphone.monitor.LocationMonitor;
import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;


public class MainActivity extends ActionBarActivity {

    private GlobalValues gv;

    private final String TAG = "MainActivity";

    private Button startServiceButton;

    private Button locationMonitor;
    private Button ambientMonitor;

    private GoogleMap map;

    private boolean serviceIsBound;



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

        locationMonitor = (Button) findViewById(R.id.start_location_monitor);
        locationMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationMonitor locMon = new LocationMonitor(MainActivity.this);
                    }
                });
                t.start();
            }
        });
        ambientMonitor = (Button) findViewById(R.id.start_ambient_monitor);
    }

    private void toggleService() {
        if(serviceIsBound)
            unbindContextService();
        else
            bindContextService();
        updateServiceButton(serviceIsBound);
    }

    private void updateServiceButton(boolean isBound) {
        if(isBound)
            startServiceButton.setText("Stop Service");
        else
            startServiceButton.setText("Start Service");
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
            GlobalValues.getInstance().setService(binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.doLog(TAG, "onServiceDisconnected", Const.INFO);
            GlobalValues.getInstance().setService(null);
        }
    };

    private void unbindContextService() {
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
