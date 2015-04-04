package dk.itu.spct.itucontextphone.monitor;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.rest.PostContextEntity;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;
import dk.itu.spct.itucontextphone.view.MapActivity;

/**
 * Created by bs on 3/22/15.
 */
public class LocationMonitor implements ContextMonitor, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "LocationMonitor";

    private Context context;

    private GoogleApiClient client;
    private Location lastLocation;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private String lastUpdateTime;
    private String addressOutput;

    private ContextEntityList data;

    private GlobalValues gv;

    public LocationMonitor(Context context) {
        this.context = context;
        this.data = new ContextEntityList();
        this.gv = GlobalValues.getInstance();
        buildGoogleApiClient();
        createLocationRequest();
        startClient();
        startLocationUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Utils.doLog(TAG, "onConnected", Const.INFO);
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if(lastLocation != null) {
//            updateUI(false);
            if(!Geocoder.isPresent()) {
                Utils.doLog(TAG, "No geocoder available", Const.INFO);
                return;
            }
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utils.doLog(TAG, "onConnectionSuspended", Const.INFO);
    }

    @Override
    public void onLocationChanged(Location location) {
        Utils.doLog(TAG, "onLocationChanged", Const.INFO);
        Utils.doLog(TAG, location.getLatitude() + ", " + location.getLongitude(), Const.INFO);
        currentLocation = location;
        lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        data.add(Utils.locationDataToContextEntity(location));
        updateMap(location);
//        if(GlobalValues.getInstance().getMapActivity() != null) {
//            GlobalValues.getInstance().getMapActivity().setLocation(location);
//        }
//        updateUI(true);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utils.doLog(TAG, "onConnectionFailed", Const.INFO);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        Utils.doLog(TAG, "Stopped requesting location updates", Const.INFO);
    }

    private void startLocationUpdates() {
        if(client != null && client.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
            Utils.doLog(TAG, "Requesting location updates", Const.INFO);
        }
    }

    private void startClient() {
        client.connect();
        Utils.doLog(TAG, "Client started!", Const.INFO);
    }

    private void stopClient() {
        client.disconnect();
        Utils.doLog(TAG, "Client stopped!", Const.INFO);
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Utils.doLog(TAG, "Created google api client!", Const.INFO);
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Utils.doLog(TAG, "Created location request", Const.INFO);
    }

    @Override
    public ContextEntityList sample() {
        ContextEntityList tmp = new ContextEntityList();
        tmp.addAll(data);
        Utils.doLog("ContextService", "sample, tmp.size() " + tmp.size() + " data.size() " + data.size(), Const.INFO);
        data.clear();
        return tmp;
    }

    @Override
    public String getName() {
        return "LocationMonitor";
    }

    private void updateMap(Location loc) {
        if(gv.getMap() != null) {
            LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
            gv.getMap().setMyLocationEnabled(true);
            gv.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
            gv.getMap().addMarker(new MarkerOptions().title("Custom marker").snippet("Some marker set by the location monitor").position(ll));
        }
    }

    public ContextEntityList getData() {
        return data;
    }

    public void setData(ContextEntityList data) {
        this.data = data;
    }

    private long getUUID(){
        ContextEntityList list = getData();
        long id = list.get(0).getId();
        return id;
    }

    private void postDataToCloud(){
        ContextEntityList list = getData();
        new PostContextEntity().execute(list);
    }



}
