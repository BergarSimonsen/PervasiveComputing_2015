package dk.itu.spct.itucontextphone.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dk.itu.spct.itucontextphone.io.Response;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/3/15.
 */
public class GetContextEntity extends AsyncTask<Long, Void, Response> {

    @Override
    protected Response doInBackground(Long... params) {
        InputStream inputStream = null;
        JSONArray jsonArr = new JSONArray();
        try {

            long id = params[0];
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Const.WS_ROOT_URL);
            if(id > 0) {
                httpGet = new HttpGet(Const.WS_ROOT_URL + "?id=" + id);
            }

            // 7. Set some headers to inform server about the type of the content
            httpGet.setHeader("Accept", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpGet);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            String result = "";
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            JSONObject res = new JSONObject(result);
            Response resp = new Response();
            resp.setData(ContextEntityList.fromJson((JSONArray) res.get("data")));
            resp.setResponseCode(res.getInt("responseCode"));


            Utils.doLog("POST", "result = " + result, Const.INFO);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
