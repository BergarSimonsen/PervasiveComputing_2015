package dk.itu.spct.itucontextphone.tools;

/**
 * Created by bs on 3/21/15.
 */
public class Const {

    // Application wide log tag
    public static final String TAG              =  "ITU_Context_phone";

    // Log level constants
    public static final int DEBUG               = 0;
    public static final int ERROR               = 1;
    public static final int INFO                = 2;
    public static final int VERBOSE             = 3;
    public static final int WARN                = 4;

    // Maps constants
    public static final String LATITUDE         = "latitude";
    public static final String LONGITUDE        = "longitude";

    // Web service response codes
    public final static int RESPONSE_SUCCESS	= 0;
    public final static int RESPONSE_ERROR		= 1;
    public final static int RESPONSE_EXISTS		= 2;
    public final static int RESPONSE_NOT_FOUND	= 3;

    // Web service urls
    public static final String WS_ROOT_URL      = "http://1-dot-united-time-89112.appspot.com/context";
    public static final String WS_POST_SINGLE   = WS_ROOT_URL + "/single";
    public static final String WS_POST_LIST     = WS_ROOT_URL + "/list";

    // Thread sleep in millis (used in context service)
    public static long THREAD_SLEEP             = 5000;
    public static int DATA_LIMIT                = 20;

    // Accelerometer sample rate in ms
    public static long ACC_SAMPLE_RATE           = 500;
}
