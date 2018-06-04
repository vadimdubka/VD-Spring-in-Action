package com.vadimdubka.spittr.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Spittle {
    private final Long    id;
    private final String  message;
    private       Date    time; // TODO сделать Final
    private       Double  latitude;
    private       Double  longitude;
    private       Spitter spitter; // TODO сделать Final
    private       Date    postedTime; // TODO сделать Final
    
    public Spittle(String message, Date time) {
        this(null, message, time, null, null);
    }
    
    public Spittle(Long id, String message, Date time, Double longitude, Double latitude) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public Spittle(Long id, Spitter spitter, String message, Date postedTime) {
        this.id = id;
        this.spitter = spitter;
        this.message = message;
        this.postedTime = postedTime;
    }
    
    public long getId() {
        return id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Date getTime() {
        return time;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public Spitter getSpitter() {
        return spitter;
    }
    
    public void setSpitter(Spitter spitter) {
        this.spitter = spitter;
    }
    
    public Date getPostedTime() {
        return postedTime;
    }
    
    public void setPostedTime(Date postedTime) {
        this.postedTime = postedTime;
    }
    
    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "id", "time");
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id", "time");
    }
    
}
