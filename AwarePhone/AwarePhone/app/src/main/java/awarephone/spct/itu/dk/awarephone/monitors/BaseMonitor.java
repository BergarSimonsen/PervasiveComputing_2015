package awarephone.spct.itu.dk.awarephone.monitors;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import awarephone.spct.itu.dk.awarephone.Const;
import awarephone.spct.itu.dk.awarephone.ContextService;
import awarephone.spct.itu.dk.awarephone.Utils;

/**
 * Created by bs on 3/12/15.
 */
public class BaseMonitor {

    private static final String TAG = "BaseMonitor";

//    public static final String SENSOR_NAME_KEY = "SensorName";
//    public static final String SENSOR_TYPE_KEY = "SensorType";
//    public static final String CONTEXT_KEY = "Context";
//    public static final String SERVICE_KEY = "Service";
//    public static final String CONTEXT_SERVICE_BOUND_KEY = "ContextServiceBound";
//    public static final String SENSOR_MANAGER_KEY = "SensorManager";
//    public static final String SENSOR_KEY = "Sensor";

    protected final String SENSOR_NAME;
    protected final String SENSOR_TYPE;
    protected Context context;
    protected ContextService service;
    protected boolean contextServiceBound;

    protected SensorManager sensorManager;

    protected Sensor sensor;

    public BaseMonitor(Context context, String sensorName, String sensorType) {
        this.context = context;
        this.SENSOR_NAME = sensorName;
        this.SENSOR_TYPE = sensorType;
        initializeSensorManager();
    }

    private void initializeSensorManager() {
        if(context != null)
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        else
            Utils.doLog(TAG, "Could not initialize sensor manager!", Const.E);
    }

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Utils.doLog(TAG, "onServiceConnected", Const.I);
            ContextService.ContextServiceBinder binder = (ContextService.ContextServiceBinder) service;
            BaseMonitor.this.setService(binder.getService());
            BaseMonitor.this.setContextServiceBound(true);
            BaseMonitor.this.service.registerMonitor(BaseMonitor.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.doLog(TAG, "onServiceDisconnected", Const.I);
            BaseMonitor.this.setContextServiceBound(false);
        }
    };

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public ContextService getService() {
        return service;
    }

    public void setService(ContextService service) {
        this.service = service;
    }

    public boolean isContextServiceBound() {
        return contextServiceBound;
    }

    public void setContextServiceBound(boolean contextServiceBound) {
        this.contextServiceBound = contextServiceBound;
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    public void setConnection(ServiceConnection connection) {
        this.connection = connection;
    }
}
