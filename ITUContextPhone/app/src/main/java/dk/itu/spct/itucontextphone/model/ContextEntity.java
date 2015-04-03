package dk.itu.spct.itucontextphone.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bs on 3/22/15.
 */
public class ContextEntity {
    private long id;
    private String sensor;
    private String value;
    private String type;
    private long timeStamp;

    public ContextEntity() { }
    public ContextEntity(long id, String sensor, String value, String type, long timeStamp) {
        this.id = id;
        this.sensor = sensor;
        this.value = value;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public static ContextEntity fromJson(JSONObject json) throws JSONException {
        ContextEntity e = new ContextEntity();
        e.setId(json.getLong("id"));
        e.setValue(json.getString("value"));
        e.setSensor(json.getString("sensor"));
        e.setType(json.getString("type"));
        e.setTimeStamp(json.getLong("timeStamp"));
        return e;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getSensor() { return sensor; }

    public void setSensor(String sensor) { this.sensor = sensor; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public long getTimeStamp() { return timeStamp; }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

    @Override
    public String toString() { return String.valueOf(id) + " " + sensor; }
}
