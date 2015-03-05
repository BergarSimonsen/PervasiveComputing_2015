//package beacon.spct.itu.dk.ibeacon;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.ResultReceiver;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * Created by bs on 2/28/15.
// */
//public class AddressResultReceiver extends ResultReceiver {
//
//    private String addressOutput;
//    private Context context;
//    private TextView addressTV;
//
//    public AddressResultReceiver(Handler handler) {
//        super(handler);
//    }
//
//    @Override
//    protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//        addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
//        if(addressTV != null)
//            addressTV.setText(addressOutput);
//
//        if(resultCode == Constants.SUCCESS_RESULT && context != null)
//            Toast.makeText(context, "Address found!", Toast.LENGTH_SHORT).show();
//    }
//
//    public void setContext(Context context) { this.context = context; }
//    public Context getContext() { return context; }
//
//    public void setAddressTV(TextView tv) { this.addressTV = tv; }
//    public TextView getAddressTV() { return addressTV; }
//
//}
