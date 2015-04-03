package dk.itu.spct.itucontextphone.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import dk.itu.spct.itucontextphone.model.ContextEntity;
import dk.itu.spct.itucontextphone.model.ContextEntityList;
import dk.itu.spct.itucontextphone.tools.Const;
import dk.itu.spct.itucontextphone.tools.Utils;

/**
 * Created by bs on 4/3/15.
 */
public class PostContextEntity extends AsyncTask<ContextEntityList, Void, Response> {
    @Override
    protected Response doInBackground(ContextEntityList... params) {
        InputStream inputStream = null;

        ContextEntityList list = null;

        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        ObjectMapper mapper = null;
        JSONObject res;
        Response resp = null;

        try {
            if(params != null && params.length > 0)
                list = params[0];

            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(Const.WS_POST_LIST);
            mapper = new ObjectMapper();

            String s = mapper.writeValueAsString(list);

            StringEntity se = new StringEntity(s);

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            httpResponse = httpClient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            String result = "";
            if(inputStream != null)
                result = Utils.convertInputStreamToString(inputStream);

            res = new JSONObject(result);
            resp = new Response();
            resp.setData(ContextEntityList.fromJson((JSONArray) res.get("data")));
            resp.setResponseCode(res.getInt("responseCode"));

            Utils.doLog("POST", "result = " + result, Const.INFO);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Utils.doLog("PostContextEntity", ex.getMessage() != null ? ex.getMessage() : "exception closing inputStream", Const.ERROR);
                }
            }
        }

        return resp;
    }


}
