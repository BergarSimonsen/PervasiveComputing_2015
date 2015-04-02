package dk.itu.spct.itucontextphone.view;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dk.itu.spct.itucontextphone.R;
import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;

public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private static final String TAG = "MAP";

    private GlobalValues gv;

    private GoogleMap map;

    public void setLocation(Location loc) {
        if(map != null && loc != null) {
            Utils.doLog(TAG, "Settings location 'manually'", Const.INFO);
            gv.setLatestLatitude(loc.getLatitude());
            gv.setLatestLongitude(loc.getLongitude());
//            LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
//            map.setMyLocationEnabled(true);
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
//            map.addMarker(new MarkerOptions().title("Custom marker").snippet("Some marker set by the location monitor").position(ll));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        gv = GlobalValues.getInstance();

//        gv.setMapActivity(this);

//        Bundle extras = getIntent().getExtras();
//        latitude = extras.getDouble(Const.LATITUDE);
//        longitude = extras.getDouble(Const.LONGITUDE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
//        startContextService();
    }

//    private void startContextService() {
//        startService(new Intent(MapActivity.this, ContextService.class));
//    }


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
        if(gv.getLatestLatitude() > 0.0 && gv.getLatestLongitude() > 0.0) {
            LatLng loc = new LatLng(gv.getLatestLatitude(), gv.getLatestLongitude());

            Utils.doLog(TAG, logPrefix + "LatLng: " + loc.toString(), Const.INFO);

            Utils.doLog(TAG, logPrefix + "Setting my location", Const.INFO);
            googleMap.setMyLocationEnabled(true);
            Utils.doLog(TAG, logPrefix + "moving camera..", Const.INFO);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

            Utils.doLog(TAG, logPrefix + "Adding marker", Const.INFO);
            googleMap.addMarker(new MarkerOptions().title("Sidney").snippet("The most populous city in Australia.").position(loc));
        } else {
            Utils.doLog(TAG, logPrefix + "Location not found.", Const.ERROR);
        }
    }
}
