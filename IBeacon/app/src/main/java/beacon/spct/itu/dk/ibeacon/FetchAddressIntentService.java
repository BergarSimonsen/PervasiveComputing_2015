package beacon.spct.itu.dk.ibeacon;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {

    private static final String TAG = "FetchAddressIntentService";
    protected ResultReceiver receiver;

    private Geocoder geocoder;

    public FetchAddressIntentService() {
        super(TAG);
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String errorMessage = "";
        receiver = intent.getParcelableExtra(Constants.RECEIVER);

        if(receiver == null) {
            Log.wtf(TAG, "fetchAddressIntentService.onHandleIntent No receiver found!");
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        if (location == null) {
            errorMessage = "No location data provided!";
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }


        geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            errorMessage = "Service not available";
            Log.e(TAG, "fetchAddressIntentService.onHandleIntent " + errorMessage, ioException);

        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid longitude / latitude values";
            Log.e(TAG, "fetchAddressIntentService.onHandleIntent " + errorMessage, illegalArgumentException);
        }

        if(addresses == null || addresses.size() == 0) {
            if(errorMessage.isEmpty()) {
                errorMessage = "No addresses found";
                Log.e(TAG, "fetchAddressIntentService.onHandleIntent " + errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "fetchAddressIntentService.onHandleIntent address found.");
            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }
}
