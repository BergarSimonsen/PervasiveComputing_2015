package beacon.spct.itu.dk.ibeacon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skyhookwireless.wps.*;


public class SkyhookActivity extends ActionBarActivity {

    private static final String TAG = "SkyhookActivity";
    private XPS xps;
    private final String API_KEY = "eJwz5DQ0AAFjY1NzzmoTC0sLJzcnA11LSzNjXUNLczddU0sjR103Z2M3CzdXAzMXU8daABGAC0I";

    private Button getLocationButton;

    private EditText latitude;
    private EditText longitude;
    private EditText address;

    private Handler handler;

    // Handler messages
    private static final int LOCATION_MESSAGE = 1;
    private static final int ERROR_MESSAGE = 2;
    private static final int DONE_MESSAGE = 3;
    private static final int REGISTRATION_SUCCESS_MESSAGE = 4;
    private static final int REGISTRATION_ERROR_MESSAGE = 5;
    private static final int LOCATION_LIST_MESSAGE = 6;

    private void setUIHandler()
    {
        handler = new Handler()
        {
            @Override
            public void handleMessage(final Message msg)
            {
//                switch (msg.what) {
//                    case LOCATION_MESSAGE:
//                        Location l = (Location) msg.obj;
//                        double la = l.getLatitude();
//                        double lo = l.getLongitude();
//                        String ad = l
//                        _tv.setText(((Location) msg.obj).toString());
//                        return;
//                    case LOCATION_LIST_MESSAGE:
//                    {
//                        final StringBuilder sb = new StringBuilder();
//                        for (final Location location : (Location[]) msg.obj)
//                            sb.append(location+"\n\n");
//                        _tv.setText(sb.toString());
//                    }
//                    return;
//                    case ERROR_MESSAGE:
//                        _tv.setText(((WPSReturnCode) msg.obj).name());
//                        return;
//                    case DONE_MESSAGE:
//                        deactivateStopButton();
//                        _stop = false;
//                        return;
//                    case REGISTRATION_SUCCESS_MESSAGE:
//                        _tv.setText("Registration succeeded");
//                        return;
//                    case REGISTRATION_ERROR_MESSAGE:
//                        _tv.setText("Registration failed ("+((WPSReturnCode) msg.obj).name()+")");
//                        return;
//                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skyhook);

        latitude = (EditText) findViewById(R.id.skyhook_latitude);
        longitude = (EditText) findViewById(R.id.skyhook_longitude);
        address = (EditText) findViewById(R.id.skyhook_address);

        getLocationButton = (Button) findViewById(R.id.skyhook_get_location);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        xps = new XPS(this);
        xps.setKey(API_KEY);
    }

    private void fetchLocation() {
        if(xps != null) {
            Log.i(TAG, "fetchLocation");
//            xps.getLocation(null, WPSStreetAddressLookup.WPS_NO_STREET_ADDRESS_LOOKUP, callback);
            xps.getLocation(null, WPSStreetAddressLookup.WPS_FULL_STREET_ADDRESS_LOOKUP, callback);
        } else {
            Log.i(TAG, "fetchLocation xps == null");
        }
    }

    private void updateView(double lat, double lon, String address) {
        latitude.setText(String.valueOf(lat));
        longitude.setText(String.valueOf(lon));
        if(address != null)
            this.address.setText(address);
        else this.address.setText("NULL");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skyhook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    WPSLocationCallback callback = new WPSLocationCallback() {
        @Override
        public void handleWPSLocation(WPSLocation wpsLocation) {
            Log.i(TAG, "callback.handleWpsLocation");

            double la = wpsLocation.getLatitude();
            double lo = wpsLocation.getLongitude();
            String add = wpsLocation.getStreetAddress().toString();
            updateView(la, lo, add);

        }

        @Override
        public void done() {
            Log.i(TAG, "callback.done");
        }

        @Override
        public WPSContinuation handleError(WPSReturnCode wpsReturnCode) {
            Log.i(TAG, "callback.handleError");

            // TODO: Handle error

            return null;
        }
    };
}
