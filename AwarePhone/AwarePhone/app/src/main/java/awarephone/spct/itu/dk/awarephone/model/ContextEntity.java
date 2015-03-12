package awarephone.spct.itu.dk.awarephone.model;

import java.util.Date;

/**
 * Created by bs on 3/12/15.
 */
public class ContextEntity {
    private int id;

    private String type;
    private String sensor;
    private double value;
    private Date timeStamp;

    public ContextEntity(int id, String type, String sensor, double value, Date timestamp) {
        this.id = id;
        this.type = type;
        this.sensor = sensor;
        this.value = value;
        this.timeStamp = timestamp;
    }

    public ContextEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
