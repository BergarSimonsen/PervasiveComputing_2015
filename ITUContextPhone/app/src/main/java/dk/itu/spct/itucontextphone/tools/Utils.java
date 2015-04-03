package dk.itu.spct.itucontextphone.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import dk.itu.spct.itucontextphone.R;
import dk.itu.spct.itucontextphone.model.ContextEntity;

/**
 * Created by bs on 3/21/15.
 */
public class Utils {

    //region Logging
    public static void doLog(String message, int level) {
        doLog(Const.TAG, message, level);
    }

    public static void doLog(String message) {
        doLog(Const.TAG, message, Const.DEBUG);
    }

    public static void doLog(String tag, String message, int level) {
        switch(level) {
            case Const.DEBUG:
                Log.d(tag, message);
                break;
            case Const.ERROR:
                Log.e(tag, message);
                break;
            case Const.INFO:
                Log.i(tag, message);
                break;
            case Const.VERBOSE:
                Log.v(tag, message);
                break;
            case Const.WARN:
                Log.w(tag, message);
                break;
        }
    }
    //endregion

    public static boolean isLocationEnabled(final Context context) {
        return isLocationEnabled(context, Const.TAG);
    }

    public static boolean isLocationEnabled(final Context context, final String logTag) {
        LocationManager lm = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        if(lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check if location (gps) is enabled
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            doLog(logTag, ex.getMessage(), Const.ERROR);
        }

        // check if network is enabled
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            doLog(logTag, ex.getMessage(), Const.ERROR);
        }

        if(!gps_enabled && !network_enabled) return false;
        else return true;
    }

    public static void showLocationDialog(final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.location_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.location_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                //get gps
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    /**
     * Convert response codes from web service to textual representation
     * @param code
     * @return string representation of code.
     */
    public static String responseCodeToString(int code) {
        switch(code) {
            case Const.RESPONSE_SUCCESS:
                return "Success";
            case Const.RESPONSE_ERROR:
                return "Error";
            case Const.RESPONSE_EXISTS:
                return "Entity already exists";
            case Const.RESPONSE_NOT_FOUND:
                return "Entity not found";
            default:
                return "Unknown error";
        }
    }

    public static ContextEntity locationDataToContextEntity(Location location) {
        String val = String.valueOf(location.getLongitude()) + "_" + String.valueOf(location.getLatitude());
        ContextEntity e = new ContextEntity();
        e.setId(UUID.randomUUID().getLeastSignificantBits());
        e.setSensor("Android Location");
        e.setValue(val);
        e.setType("Location");
        e.setTimeStamp(Calendar.getInstance().get(Calendar.MILLISECOND));
        return e;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
