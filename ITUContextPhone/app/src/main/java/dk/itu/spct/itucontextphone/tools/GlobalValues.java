package dk.itu.spct.itucontextphone.tools;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.view.MapActivity;

/**
 * Created by bs on 4/2/15.
 */
public class GlobalValues {

//    private MapActivity mapActivity;

    private Location currentLocation;

    private double latestLongitude;
    private double latestLatitude;

    private GoogleMap map;

    private static GlobalValues instance;
    public static GlobalValues getInstance() {
        if(instance == null)
            instance = new GlobalValues();
        return instance;
    }

    private ContextService service;

    public ContextService getService() {
        return service;
    }

    public void setService(ContextService service) {
        this.service = service;
    }

//    public MapActivity getMapActivity() {
//        return mapActivity;
//    }
//
//    public void setMapActivity(MapActivity mapActivity) {
//        this.mapActivity = mapActivity;
//    }

    public double getLatestLatitude() {
        return latestLatitude;
    }

    public void setLatestLatitude(double latestLatitude) {
        this.latestLatitude = latestLatitude;
    }

    public double getLatestLongitude() {
        return latestLongitude;
    }

    public void setLatestLongitude(double latestLongitude) {
        this.latestLongitude = latestLongitude;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
