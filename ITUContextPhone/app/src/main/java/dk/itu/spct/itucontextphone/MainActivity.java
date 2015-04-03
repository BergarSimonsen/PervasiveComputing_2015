package dk.itu.spct.itucontextphone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.monitor.LocationMonitor;
import dk.itu.spct.itucontextphone.rest.GetContextEntity;
import dk.itu.spct.itucontextphone.rest.PostContextEntity;
import dk.itu.spct.itucontextphone.service.ContextService;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.GlobalValues;
import dk.itu.spct.itucontextphone.tools.Utils;


public class MainActivity extends ActionBarActivity {

    private GlobalValues gv;

    private final String TAG = "MainActivity";

    private Button startServiceButton;

    private Button locationMonitor;
    private Button ambientMonitor;

    private GoogleMap map;

    private boolean serviceIsBound;


//    public static String POST(){
//        InputStream inputStream = null;
//        String result = "";
//        String url = "http://1-dot-united-time-89112.appspot.com/context/single";
//        try {
//
//            // 1. create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // 2. make POST request to the given URL
//            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // 3. build jsonObject
////            JSONObject jsonObject = new JSONObject("ContextEntity");
////            jsonObject.accumulate("id", 987654321);
////            jsonObject.accumulate("timeStamp", 123456789);
////            jsonObject.accumulate("type", "ambient light");
////            jsonObject.accumulate("value", "some value");
////            jsonObject.accumulate("sensor", "sensor light");
//
//            // 4. convert JSONObject to JSON to String
////            json = jsonObject.toString();
//
//            ContextEntity entity = new ContextEntity();
//            entity.setId(5L);
//            entity.setTimeStamp(989898989);
//            entity.setType("ambient light123123");
//            entity.setValue("test123123");
//            entity.setSensor("sensor123123 light");
//
//            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
//            ObjectMapper mapper = new ObjectMapper();
//            json = mapper.writeValueAsString(entity);
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            Utils.doLog("XXX", "----------------------", Const.INFO);
//            Utils.doLog("XXX", json, Const.INFO);
//            Utils.doLog("XXX", "----------------------", Const.INFO);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        // 11. return result
//        return result;
//    }
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = GlobalValues.getInstance();

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.new_map);
        map = mf.getMap();
        gv.setMap(map);

        Button tb = (Button) findViewById(R.id.test_button);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    new RestTask().execute(new URL("http://localhost:8080"));
                    ContextEntity e = new ContextEntity();
                    e.setId(22);
                    e.setTimeStamp(123456);
                    e.setType("SOME_TYP");
                    e.setValue("some random value for testing");
                    e.setSensor("just another sensor");
                    ContextEntityList tmp = new ContextEntityList();
                    tmp.add(e);
//                    new PostContextEntity().execute(tmp);
                new GetContextEntity().execute(0L);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
            }
        });

        startServiceButton = (Button) findViewById(R.id.start_service_btn);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleService();
            }
        });

        locationMonitor = (Button) findViewById(R.id.start_location_monitor);
        locationMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationMonitor locMon = new LocationMonitor(MainActivity.this);
                    }
                });
                t.start();
            }
        });
        ambientMonitor = (Button) findViewById(R.id.start_ambient_monitor);
    }

    private void toggleService() {
        if(serviceIsBound)
            unbindContextService();
        else
            bindContextService();
        updateServiceButton(serviceIsBound);
    }

    private void updateServiceButton(boolean isBound) {
        if(isBound)
            startServiceButton.setText("Stop Service");
        else
            startServiceButton.setText("Start Service");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Utils.doLog(TAG, "onServiceConnected", Const.INFO);
            ContextService.ContextServiceBinder binder = (ContextService.ContextServiceBinder) service;
            GlobalValues.getInstance().setService(binder.getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.doLog(TAG, "onServiceDisconnected", Const.INFO);
            GlobalValues.getInstance().setService(null);
        }
    };

    private void unbindContextService() {
        getApplicationContext().unbindService(connection);
        Utils.doLog(TAG, "Context service unbound!", Const.INFO);
        serviceIsBound = false;
    }

    private void bindContextService() {
        Utils.doLog(TAG, "bindService", Const.INFO);
        Intent intent = new Intent(this, ContextService.class);
        if(!getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            Utils.doLog(TAG, "error binding service!", Const.INFO);
        } else {
            Utils.doLog(TAG, "service successfully bound", Const.INFO);
            serviceIsBound = true;
        }
    }
}
