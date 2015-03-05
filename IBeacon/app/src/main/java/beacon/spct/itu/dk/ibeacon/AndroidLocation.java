package beacon.spct.itu.dk.ibeacon;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AndroidLocation extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "AndroidLocation";

    private final String REQUESTING_LOCATION_UPDATES_KEY = "RequestingLocationUpdates";
    private final String LOCATION_KEY = "Location";
    private final String LAST_UPDATED_TIME_STRING_KEY = "LastUpdatedTimeString";

    private GoogleApiClient client;
    private Location lastLocation;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private String lastUpdateTime;
    private String addressOutput;

    private EditText latitude;
    private EditText longitude;
    private EditText updateTime;
    private EditText addressET;

    private Button startClient;
    private Button stopClient;
    private Button startRequest;
    private Button stopRequest;
    private Button startService;
    protected Button showOnMap;

    private boolean requestingLocationUpdates;
    private boolean addressRequested;

    private AddressResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_location);

        updateValuesFromBundle(savedInstanceState);

        resultReceiver = new AddressResultReceiver(new Handler());

        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        updateTime = (EditText) findViewById(R.id.updateTime);
        addressET = (EditText) findViewById(R.id.address_et);

        startClient = (Button) findViewById(R.id.client_start);
        startClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClient();
            }
        });

        stopClient = (Button) findViewById(R.id.client_stop);
        stopClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopClient();
            }
        });

        startRequest = (Button) findViewById(R.id.request_start);
        startRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        stopRequest = (Button) findViewById(R.id.request_stop);
        stopRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates(true);
            }
        });

        startService = (Button) findViewById(R.id.service_start);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAddressButtonHandler();
            }
        });

        showOnMap = (Button) findViewById(R.id.show_on_map);
        showOnMap.setEnabled(false);
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOnMap();
            }
        });


        buildGoogleApiClient();
        createLocationRequest();

        client.connect();
    }

    public void fetchAddressButtonHandler() {
        if(client.isConnected() && lastLocation != null)
            startIntentService();

        addressRequested = true;
        updateUI(false);
    }

    private void updateUI(boolean req) {
        if(req) {
            if(currentLocation != null) {
                latitude.setText(String.valueOf(currentLocation.getLatitude()));
                longitude.setText(String.valueOf(currentLocation.getLongitude()));
                if(addressOutput != null) addressET.setText(addressOutput);
            }
            if(lastUpdateTime != null)
                updateTime.setText(lastUpdateTime);
        }else {
            latitude.setText(String.valueOf(lastLocation.getLatitude()));
            longitude.setText(String.valueOf(lastLocation.getLongitude()));
            if(addressOutput != null) addressET.setText(addressOutput);
        }
    }

    private void stopLocationUpdates(boolean manual) {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        if(manual) requestingLocationUpdates = false;
        Log.i(TAG, "Stopped requesting location updates");
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        requestingLocationUpdates = true;
        Log.i(TAG, "Requesting location updates");
    }

    private void startClient() {
        client.connect();
        Log.i(TAG, "Client started!");
    }

    private void stopClient() {
        client.disconnect();
        Log.i(TAG, "Client stopped!");
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG, "Created google api client!");
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.i(TAG, "Created location request");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_android_location, menu);
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if(lastLocation != null) {
            updateUI(false);
            if(!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(addressRequested)
                startIntentService();
        }

        if(requestingLocationUpdates) startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { Log.i(TAG, "onConnectionFailed"); }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        stopLocationUpdates(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if((client != null && client.isConnected()) && requestingLocationUpdates) {
            startLocationUpdates();
        } else {
            Log.i(TAG, "Could not start requesting location updates");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates);
        outState.putParcelable(LOCATION_KEY, currentLocation);
        outState.putString(LAST_UPDATED_TIME_STRING_KEY, lastUpdateTime);
        super.onSaveInstanceState(outState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY))
                requestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            else
                Log.i(TAG, String.format("updateValuesFromBundle: %s not found", REQUESTING_LOCATION_UPDATES_KEY));

            if(savedInstanceState.keySet().contains(LOCATION_KEY))
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            else
                Log.i(TAG, String.format("updateValuesFromBundle: %s not found", LOCATION_KEY));

            if(savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY))
                lastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            else
                Log.i(TAG, String.format("updateValuesFromBundle: %s not found", LAST_UPDATED_TIME_STRING_KEY));

            updateUI(true);
        } else {
            Log.i(TAG, "updateValuesFromBundle: Bundle == null");
        }
    }

    protected void startIntentService() {
        if(currentLocation != null) {
            Intent intent = new Intent(this, FetchAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, resultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, currentLocation);
            startService(intent);
        }
    }

    protected void showOnMap() {
        Intent intent = new Intent(this, MapActivity.class);
        if(currentLocation != null) {
            intent.putExtra(Constants.LATITUDE, currentLocation.getLatitude());
            intent.putExtra(Constants.LONGITUDE, currentLocation.getLongitude());
        } else if (lastLocation != null) {
            intent.putExtra(Constants.LATITUDE, lastLocation.getLatitude());
            intent.putExtra(Constants.LONGITUDE, lastLocation.getLongitude());
        } else {
            return;
        }
        startActivity(intent);
    }

    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            if(resultCode == Constants.SUCCESS_RESULT)
                Toast.makeText(getApplicationContext(), "Address found!", Toast.LENGTH_SHORT).show();

            addressRequested = false;
            updateUI(false);
            if(currentLocation != null || lastLocation != null)
                showOnMap.setEnabled(true);
        }
    }
}
