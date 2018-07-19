/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    private TextView emptyview;
    private static final int EARTH_QUAKE_LOADER_ID = 1;
    // Create a new {@link ArrayAdapter} of earthquakes
    EarthquakeAdapter adapter;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=1&limit=10";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTH_QUAKE_LOADER_ID, null, this);
        }
        else
        {
            View loadingindicator=findViewById(R.id.loading_indicator);
            loadingindicator.setVisibility(View.GONE);
            emptyview=(TextView) findViewById(R.id.empty_view);
            emptyview.setText(R.string.no_internet_connection);
        }
        emptyview=(TextView)findViewById(R.id.empty_view);

        /*// Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
        earthquakes.add(new Earthquake("7.2","San Francissco","Feb 2, 2017"));
        earthquakes.add(new Earthquake("6.1","London","July 26, 2016"));
        earthquakes.add(new Earthquake("3.9","Tokyo","Nov 20, 2015"));
        earthquakes.add(new Earthquake("5.4","Mexico City","May 3, 2014"));
        earthquakes.add(new Earthquake("2.8","Moscow","Jan 31, 2013"));
        earthquakes.add(new Earthquake("4.9","Rio de Janeiro ","Aug 19, 2012"));
        earthquakes.add(new Earthquake("1.6","Paris","Oct 30, 2011"));
        */

        /**EarthquakeAsyncTask task=new EarthquakeAsyncTask();
         *task.execute(USGS_REQUEST_URL);*/
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(emptyview);
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Earthquake quake = adapter.getItem(position);//Get the {@link word} object at the given position the user clicked on
                //Toast.makeText(EarthquakeActivity.this,quake.getUrl(),Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(quake.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);

                /*// Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);*/
            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.v("EarthquakeActivity", "OnCreateLoader");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.v("EarthquakeActivity", "OnFinished");
        View loadingIndicator=findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        emptyview.setText(R.string.empty);
        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.v("EarthquakeActivity", "OnReset");
        adapter.clear();
    }
}
    //private class EarthquakeAsyncTask extends AsyncTask<String,Void,List<Earthquake>>
    //{
        /**
         * {@link AsyncTask} to perform the network request on a background thread, and then
         * update the UI with the list of earthquakes in the response.
         *
         * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
         * an output type. Our task will take a String URL, and return an Earthquake. We won't do
         * progress updates, so the second generic is just Void.
         *
         * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
         * The doInBackground() method runs on a background thread, so it can run long-running code
         * (like network activity), without interfering with the responsiveness of the app.
         * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
         * UI thread, so it can use the produced data to update the UI.
         */

        //@Override
       /*protected List<Earthquake> doInBackground(String... urls)
        {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<Earthquake> data)
        {
            // Clear the adapter of previous earthquake data
            adapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }
    }*/
