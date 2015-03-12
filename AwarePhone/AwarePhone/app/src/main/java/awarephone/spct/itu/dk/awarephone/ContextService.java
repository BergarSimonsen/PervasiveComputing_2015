package awarephone.spct.itu.dk.awarephone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ContextService extends Service {

    private static final String TAG = "ContextService";
    private static final String LOG_PREFIX = "ContextService.";

    public ContextService() {
    }

    @Override
    public void onCreate() {
        doLog("onCreate", Const.I);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        doLog("onDestroy", Const.I);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        doLog("onBind", Const.I);
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        doLog("onUnbind", Const.I);
        return super.onUnbind(intent);
    }

    private void doLog(String msg, int level) {
        switch(level) {
            case Const.I:
                Log.i(TAG, msg);
                break;
            case Const.D:
                Log.d(TAG, msg);
                break;
            case Const.E:
                Log.e(TAG, msg);
                break;
        }
    }

    public class ContextServiceBinder extends Binder {
        public ContextService getService() { return ContextService.this; }
    }
}
