package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by PC on 16-10-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{
    private String url;
    public EarthquakeLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading()
    {
        Log.v("EarthquakeLoader","OnStartLoading");
        forceLoad();
    }
    @Override
    public List<Earthquake> loadInBackground() {
        Log.v("EarthquakeLoader","LoadingBackGround");
        List<Earthquake> earthquakes=QueryUtils.fetchEarthquakeData(url);
        return earthquakes;
    }
}
