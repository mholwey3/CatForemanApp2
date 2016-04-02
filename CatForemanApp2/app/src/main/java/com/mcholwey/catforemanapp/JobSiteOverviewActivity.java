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
    HubProxy hub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_site_overview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mapsButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JobSiteOverviewActivity.this, TractorMapActivity.class);
                startActivity(i);
            }
        });

        listView = (ListView) this.findViewById(R.id.listView);
        listView.setAdapter(new TractorListAdapter(context, tractors));
        //listView.setAdapter(new TractorListAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) findViewById(R.id.detailsTextView);
                textView.setText(TractorListAdapter.tractors.get(position).getDetails());
            }
        });

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        String host = "http://bradley-capstone-app.azurewebsites.net";
        HubConnection connection = new HubConnection(host);
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

        try {
            hub.invoke("TestMethod").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ClientTestMethod(String s) {
        System.out.println(s);
    }

    public void addSubscriptionHandlers() {
        hub.on("addTractors",
                new SubscriptionHandler1<Tractor[]>() {
                    @Override
                    public void run(Tractor[] tractors) {
                        System.out.println("ADDING TRACTORS");
                        for (Tractor t : tractors) {
                            tractorListAdapter.tractors.add(t);
                        }
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
                        System.out.println("GOING TO CALL UPDATE GPS");
                        updateGPS(serialNum, longitude, latitude, speed);
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

        hub.on("clientTestMethod",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(String s) {

                    }
                }
                , String.class);
    }

//    public void clickedButton(View view) {
//        try {
//            hub.invoke("TestMethod").get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

    public void removeTractor(String serialNum){
        for (Tractor t : tractors) {
            if(t.getSerialNumber().equals(serialNum)){
                tractors.remove(t);
                return;
            }
        }
    }

    public void updateGPS(String serialNum, double longitude, double latitude, Float speed){
        System.out.println("INSIDE UPDATE GPS");
        int position = 0;
        for(Tractor t : tractors){
            if(t.getSerialNumber().equals(serialNum)){
                System.out.println("FOUND CORRECT TRACTOR");
                t.setLongitude(longitude);
                t.setLatitude(latitude);
                t.setSpeed(speed);
                break;
            }else {
                ++position;
            }
        }

        changesMade(position);
    }

    public void updateStateInfo(String serialNum, int stateNum){
        int position = 0;
        for(Tractor t : tractors){
            if(t.getSerialNumber().equals(serialNum)){
                t.setCurrentState(Tractor.TractorStateEnum.values()[stateNum]);
                break;
            }else {
                ++position;
            }
        }

        changesMade(position);
    }

    public void changesMade(final int position)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.detailsTextView);
                textView.setText(TractorListAdapter.tractors.get(position).getDetails());
            }
        });
    }
}



