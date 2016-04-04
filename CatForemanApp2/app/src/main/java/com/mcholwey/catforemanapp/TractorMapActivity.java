package com.mcholwey.catforemanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler4;

public class TractorMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap tractorMap;
    private JobSiteOverviewActivity overview;
    private HubProxy hub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractor_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overview = new JobSiteOverviewActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle "up" button behavior here.
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            NavUtils.navigateUpTo(this, upIntent);
            return true;
        }
        // return true if you handled the button click, otherwise return false.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        tractorMap = googleMap;
        LatLng tempLatLng = new LatLng(0,0);

        for (Tractor t : overview.tractorListAdapter.tractors) {
            tempLatLng = new LatLng(t.getLatitude(), t.getLongitude());
            t.setMarker(tractorMap.addMarker(new MarkerOptions().position(tempLatLng).title(t.getName()).snippet("State: " + t.getCurrentState())));
        }

        tractorMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, 12.0f));
    }
}
