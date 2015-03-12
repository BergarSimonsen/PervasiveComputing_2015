package awarephone.spct.itu.dk.awarephone;

import android.util.Log;

/**
 * Created by bs on 3/12/15.
 */
public class Utils {

    public static void doLog(String tag, String msg, int level) {
        switch(level) {
            case Const.I:
                Log.i(tag, msg);
                break;
            case Const.D:
                Log.d(tag, msg);
                break;
            case Const.E:
                Log.e(tag, msg);
                break;
        }
    }

}
