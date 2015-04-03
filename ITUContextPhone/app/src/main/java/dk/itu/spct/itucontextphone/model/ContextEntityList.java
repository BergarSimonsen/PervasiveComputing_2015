package dk.itu.spct.itucontextphone.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bs on 4/1/15.
 */
public class ContextEntityList extends ArrayList<ContextEntity> {
    public static ContextEntityList fromJson(JSONArray json) throws JSONException {
        ContextEntityList list = new ContextEntityList();
        for(int i = 0; i < json.length(); i++) {
            JSONObject j = (JSONObject) json.get(i);
            list.add(ContextEntity.fromJson(j));
        }
        return list;
    }
}
