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
        JSONArray jsonArr = new JSONArray();
        ContextEntityList tmpList = params[0];
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(Const.WS_POST_LIST);

            ObjectMapper mapper = new ObjectMapper();

//            for(ContextEntity e : tmpList) {
//                jsonArr.put(mapper.writeValueAsString(e));
//            }

            String s = mapper.writeValueAsString(tmpList);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(s);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

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
