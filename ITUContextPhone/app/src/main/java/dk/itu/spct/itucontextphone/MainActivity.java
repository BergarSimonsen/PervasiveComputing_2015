package dk.itu.spct.itucontextphone;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import dk.itu.spct.itucontextphone.monitor.ContextMonitor;
import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;
import dk.itu.spct.itucontextphone.view.MapActivity;


public class MainActivity extends ActionBarActivity {

    private final String TAG = "MainActivity";

    private Button startServiceButton;
    private Button startMapButton;

    private Button locationMonitor;
    private Button ambientMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMapButton = (Button) findViewById(R.id.start_map_btn);
        startMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean locEnabled = Utils.isLocationEnabled(MainActivity.this);
                if (locEnabled)
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                else {
                    Utils.showLocationDialog(MainActivity.this);
                    locEnabled = Utils.isLocationEnabled(MainActivity.this);
                    if (locEnabled)
                        startActivity(new Intent(MainActivity.this, MapActivity.class));
                }
            }
        });

        startServiceButton = (Button) findViewById(R.id.start_service_btn);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        locationMonitor = (Button) findViewById(R.id.start_location_monitor);
        ambientMonitor = (Button) findViewById(R.id.start_ambient_monitor);
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

    private void bindContextService(ContextMonitor monitor) {
//        Utils.doLog(TAG, "bindService", Const.INFO);
//        Intent intent = new Intent(this, ContextService.class);
//        if(!getApplicationContext().bindService(intent, monitor.getConnection(), Context.BIND_AUTO_CREATE)) {
//            Utils.doLog(TAG, "AmbientMonitor not bound to service", Const.INFO);
//        } else {
//            Utils.doLog(TAG, "AmbientMonitor bound to service", Const.INFO);
//        }
    }
}
