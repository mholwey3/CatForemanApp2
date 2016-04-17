package com.mcholwey.catforemanapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.jar.*;

public class TractorMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static GoogleMap tractorMap;
    private JobSiteOverviewActivity overview;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tractor_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overview = new JobSiteOverviewActivity();

        FloatingActionButton refreshFAB = (FloatingActionButton)findViewById(R.id.refreshMapButton);
        refreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle "up" button behavior here.
            onBackPressed();
            return true;
        }
        // return true if you handled the button click, otherwise return false.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        tractorMap = googleMap;
        tractorMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            tractorMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        LatLng tempLatLng = new LatLng(0,0);

        for (Tractor t : overview.tractorListAdapter.tractors) {
            tempLatLng = new LatLng(t.getLatitude(), t.getLongitude());
            t.setMarker(tractorMap.addMarker(new MarkerOptions().position(tempLatLng).title(t.getName()).snippet("State: " + t.getCurrentStateString(t.getCurrentState()))));
        }

        tractorMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, 18.0f));
    }
}
