package awarephone.spct.itu.dk.awarephone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import awarephone.spct.itu.dk.awarephone.monitors.AmbientMonitor;
import awarephone.spct.itu.dk.awarephone.monitors.Monitor;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startAmbient = (Button) findViewById(R.id.start_ambient_btn);
        startAmbient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbientMonitor monitor = new AmbientMonitor(MainActivity.this, "TYPE_LIGHT", "amb_light");
                bindService(monitor);
            }
        });
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

    private void bindService(Monitor monitor) {
        Utils.doLog(TAG, "bindService", Const.I);
        Intent intent = new Intent(this, ContextService.class);
        if(!getApplicationContext().bindService(intent, monitor.getConnection(), Context.BIND_AUTO_CREATE)) {
            Utils.doLog(TAG, "AmbientMonitor not bound to service", Const.I);
        } else {
            Utils.doLog(TAG, "AmbientMonitor bound to service", Const.I);
        }
    }
}
