package dk.itu.spct.itucontextphone.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import dk.itu.spct.itucontextphone.io.Response;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/3/15.
 */
public class GetContextEntity extends AsyncTask<Long, Void, Response> {

    private InputStream inputStream;

    @Override
    protected Response doInBackground(Long... params) {
        inputStream = null;
        Response resp = null;
        JSONObject res;

        try {
            HttpClient httpClient;
            HttpGet httpGet;
            HttpResponse httpResponse;

            if(params != null && params.length > 0) {
                long id = params[0];

                httpClient = new DefaultHttpClient();
                httpGet = new HttpGet(Const.WS_ROOT_URL);
                if(id > 0) httpGet = new HttpGet(Const.WS_ROOT_URL + "?id=" + id);

                httpGet.setHeader("Accept", "application/json");

                httpResponse = httpClient.execute(httpGet);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                String result = "";
                if(inputStream != null)
                    result = Utils.convertInputStreamToString(inputStream);

                if(result != null && result.length() > 0) {
                    res = new JSONObject(result);
                    resp = new Response();
                    resp.setData(ContextEntityList.fromJson((JSONArray) res.get("data")));
                    resp.setResponseCode(res.getInt("responseCode"));
                }
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resp;
    }
}
