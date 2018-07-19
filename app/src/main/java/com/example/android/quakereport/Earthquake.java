package com.example.android.quakereport;

/**
 * Created by PC on 04-10-2017.
 */

public class Earthquake {
    private String location,url;
    private double magnitude;
    private long timeInMillisecond;
    public Earthquake(double magnitude,String location,long timeInMillisecond,String url)
    {
        this.url=url;
        this.magnitude=magnitude;
        this.location=location;
        this.timeInMillisecond=timeInMillisecond;
    }
    public double getMagnitude(){ return magnitude; }
    public String getLocation()
    {
        return location;
    }
    public long getTimeInMillisecond()
    {
        return timeInMillisecond;
    }
    public String getUrl()
    {
        return url;
    }
}
