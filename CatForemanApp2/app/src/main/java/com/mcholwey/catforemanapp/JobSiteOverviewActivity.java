package com.mcholwey.catforemanapp;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler4;

public class JobSiteOverviewActivity extends AppCompatActivity {

    TractorListAdapter tractorListAdapter;
    Context context = JobSiteOverviewActivity.this;
    ArrayList<Tractor> tractors = new ArrayList();
    ListView listView;
    String host = "http://bradley-capstone-app.azurewebsites.net";
    HubConnection connection = new HubConnection(host);
    HubProxy hub;
    android.os.Handler handler;
    final int REFRESH_DELAY = 10000; // Ten Seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_site_overview);

        handler = new android.os.Handler();

        //FAB to redirect application to Site-wide statistics activity
        //Each FAB needs an on click listener which overrides an onClick method
        FloatingActionButton statsFAB = (FloatingActionButton) findViewById(R.id.statsButton);
        statsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start new activity by creating an intent that goes from current activity to new activity
                Intent statsIntent = new Intent(JobSiteOverviewActivity.this, SiteStatisticsActivity.class);
                startActivity(statsIntent);
            }
        });

        //FAB to redirect application to Google Map activity
        FloatingActionButton mapFAB = (FloatingActionButton) findViewById(R.id.mapsButton);
        mapFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(JobSiteOverviewActivity.this, TractorMapActivity.class);
                startActivity(mapIntent);
            }
        });

        //FAB to refresh the gps data
        FloatingActionButton refreshGPSFAB = (FloatingActionButton) findViewById(R.id.refreshGPSButton1);
        refreshGPSFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for (Tractor t : tractors) {
                    hub.invoke("requestGPS", t.getSerialNumber());
                }
            }
        });

        requestGPSData();

        requestStatData();

        /* Only create the tractor list adapter and set each item's click event if this is the first
         * time the app is being opened. This prevents the application from duplicating the list
         * every time the JobSiteOverviewActivity is restarted.
         */
        if(savedInstanceState == null) {
            listView = (ListView) this.findViewById(R.id.listView);
            tractorListAdapter = new TractorListAdapter(context, tractors);
            listView.setAdapter(tractorListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView detailsText = (TextView) findViewById(R.id.detailsTextView);
                    detailsText.setText(TractorListAdapter.tractors.get(position).getDetails());
                }
            });

            /* Establish a connection with the SignalR backend
             * Again, this should only happen once at the start of the application
             */
            Platform.loadPlatformComponent(new AndroidPlatformComponent());
            hub = connection.createHubProxy("SensorHub");
            addSubscriptionHandlers();

            SignalRFuture<Void> awaitConnection = connection.start();
            try {
                awaitConnection.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    // Will refresh gps data from SignalR every REFRESH_DELAY seconds
    public void requestGPSData() {
        handler.postDelayed(new Runnable() {
            public void run() {
                for (Tractor t : tractors) {
                    hub.invoke("requestGPS", t.getSerialNumber());
                }

                handler.postDelayed(this, REFRESH_DELAY);
            }
        }, REFRESH_DELAY);
    }

    // Will refresh stats data from SignalR every REFRESH_DELAY seconds
    public void requestStatData(){
        handler.postDelayed(new Runnable(){
            public void run(){
                for (Tractor t : tractors) {
                    hub.invoke("requestStats", t.getSerialNumber());
                }

                handler.postDelayed(this, REFRESH_DELAY);
            }
        }, REFRESH_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* This is how we set up methods for the SignalR backend to call at appropriate times
     * SubscriptionHandler1 is for methods with 1 argument
     * SubscriptionHandler2 is for methods with 2 arguments ... and so on up to 5.
     * The invocation of these methods will be handled by the SignalR backend
     */
    public void addSubscriptionHandlers() {
        hub.on("addTractors",
                new SubscriptionHandler1<Tractor[]>() {
                    @Override
                    public void run(Tractor[] tractors) {
                        addTractors(tractors);
                    }
                }
                , Tractor[].class);

        hub.on("removeTractors",
                new SubscriptionHandler1<String[]>() {
                    @Override
                    public void run(String[] serialNums) {
                        for (String s : serialNums) {
                            removeTractor(s);
                        }
                    }
                }
                , String[].class);

        hub.on("updateGPS",
                new SubscriptionHandler4<String, Double, Double, Float>() {
                    @Override
                    public void run(String serialNum, Double longitude, Double latitude, Float speed) {
                        updateGPS(serialNum, latitude, longitude, speed);
                    }
                }
                , String.class, Double.class, Double.class, Float.class);

        hub.on("updateStateInfo",
                new SubscriptionHandler2<String, Integer>() {
                    @Override
                    public void run(String serialNum, Integer stateNum) {
                        updateStateInfo(serialNum, stateNum);
                    }
                }
                , String.class, Integer.class);

        hub.on("updateStats",
                new SubscriptionHandler2<String, Statistic>() {
                    @Override
                    public void run(String serialNum, Statistic statistic) {
                        updateStats(serialNum, statistic);
                    }
                }
                , String.class, Statistic.class);
    }

    /* Add an articulated truck to the site-wide list of tractors
     * Handled on the SignalR end
     */
    public void addTractors(Tractor[] tractors){
        for (Tractor t : tractors) {
            tractorListAdapter.tractors.add(t);
        }
    }

    /* Remove an articulated truck from the list
     * Handled on the SignalR end
     */
    public void removeTractor(String serialNum){
        for (Tractor t : tractors) {
            if(t.getSerialNumber().equals(serialNum)){
                tractorListAdapter.tractors.remove(t);
                return;
            }
        }
    }

    /* Update GPS information such as longitude, latitude, and speed EVERY TIME signalR receives new
     * data from an articulated truck. We are offering an alternative way to refresh this information
     * either by pressing a FAB to request updated gps data or by requesting this data every X seconds
     * to reduce the amount of cellular data used throughout the day.
     */
    public void updateGPS(final String serialNum, final double longitude, final double latitude, final Float speed){

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                for (Tractor t : tractorListAdapter.tractors) {
                    if (t.getSerialNumber().equals(serialNum)) {
                        //Set GPS data
                        t.setLongitude(longitude);
                        t.setLatitude(latitude);
                        t.setSpeed(speed);
                        LatLng tempLatLng = new LatLng(latitude, longitude);

                        if(t.getMarker() != null)
                            t.getMarker().setPosition(tempLatLng);

                        break;
                    } else {
                        ++position;
                    }
                }

                try{
                    TextView textView = (TextView) findViewById(R.id.detailsTextView);
                    textView.setText(TractorListAdapter.tractors.get(position).getDetails());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateStats(final String serialNum, final Statistic stat){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                for (Tractor t : tractors) {
                    if(t.getSerialNumber().equals(serialNum)) {
                        t.setStat(stat);

                        break;
                    }
                    else{
                        ++position;
                    }
                }

                try{
                    TextView textView = (TextView) findViewById(R.id.detailsTextView);
                    textView.setText(TractorListAdapter.tractors.get(position).getDetails());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /* Update the state of a particular articulated truck
     * Handled on the SignalR end
     */
    public void updateStateInfo(final String serialNum, final int stateNum){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                for (Tractor t : tractorListAdapter.tractors) {
                    if (t.getSerialNumber().equals(serialNum)) {
                        t.setCurrentState(Tractor.TractorStateEnum.values()[stateNum]);
                        if(t.getMarker() != null)
                            t.getMarker().setSnippet(Tractor.stateStrings[stateNum]);
                        break;
                    } else {
                        ++position;
                    }
                }

                try{
                    TextView textView = (TextView) findViewById(R.id.detailsTextView);
                    textView.setText(TractorListAdapter.tractors.get(position).getDetails());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void signalrUpdateLoadStats(final int numLoads, final float distanceToLoad, final float avgSpeedLoadToDump, final int timeLoadToDump){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView detailsText = (TextView) findViewById(R.id.detailsTextView);
                tractorListAdapter.tractors.get(0).setLoadStats(updateLoadStats(numLoads, distanceToLoad, avgSpeedLoadToDump, timeLoadToDump));

            }
        });
    }

    public void signalrUpdateDumpStats(final int numDumps, final float distanceToDump, final float avgSpeedDumpToLoad, final int timeDumpToLoad){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView detailsText = (TextView) findViewById(R.id.detailsTextView);
                tractorListAdapter.tractors.get(0).setDumpStats(updateDumpStats(numDumps, distanceToDump, avgSpeedDumpToLoad, timeDumpToLoad));
            }
        });
    }

    public static String updateLoadStats(final int numLoads, final float distanceToLoad, final float avgSpeedLoadToDump, final int timeLoadToDump){
        StringBuilder builder = new StringBuilder();

        builder.append("Number of loads (within last 24 hours): " + numLoads);
        builder.append("\n\nDistance driven to load: " + distanceToLoad);
        builder.append("\n\nAverage speed from LOAD to DUMP: " + avgSpeedLoadToDump);
        builder.append("\n\nTime taken from LOAD to DUMP: " + timeLoadToDump);

        return builder.toString();
    }

    public static String updateDumpStats(final int numDumps, final float distanceToDump, final float avgSpeedDumpToLoad, final int timeDumpToLoad){
        StringBuilder builder = new StringBuilder();

        builder.append("Number of dumps (within last 24 hours): " + numDumps);
        builder.append("\n\nDistance driven to dump: " + distanceToDump);
        builder.append("\n\nAverage speed from DUMP to LOAD: " + avgSpeedDumpToLoad);
        builder.append("\n\nTime taken from DUMP to LOAD: " + timeDumpToLoad);

        return builder.toString();
    }
}



