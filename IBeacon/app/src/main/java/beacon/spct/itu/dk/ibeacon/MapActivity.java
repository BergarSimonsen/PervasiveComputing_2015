package beacon.spct.itu.dk.ibeacon;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private static final String TAG = "MAP";

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble(Constants.LATITUDE);
        longitude = extras.getDouble(Constants.LONGITUDE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
    public void onMapReady(GoogleMap googleMap) {
        String logPrefix = "onMapReady: ";
        if(latitude > 0.0 && longitude > 0.0) {
            LatLng loc = new LatLng(latitude, longitude);

            Log.i(TAG, logPrefix + "LatLng: " + loc.toString());

            Log.i(TAG, logPrefix + "Setting my location");
            googleMap.setMyLocationEnabled(true);
            Log.i(TAG, logPrefix + "moving camera..");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

            Log.i(TAG, logPrefix + "Adding marker");
            googleMap.addMarker(new MarkerOptions().title("Sidney").snippet("The most populous city in Australia.").position(loc));
        } else {
            Log.e(TAG, logPrefix + "Location not found.");
        }
    }
}
