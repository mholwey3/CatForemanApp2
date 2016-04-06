package com.mcholwey.catforemanapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TractorMapActivity extends AppCompatActivity implements OnMapReadyCallback{

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
        LatLng tempLatLng = new LatLng(0,0);

        for (Tractor t : overview.tractorListAdapter.tractors) {
            tempLatLng = new LatLng(t.getLatitude(), t.getLongitude());
            t.setMarker(tractorMap.addMarker(new MarkerOptions().position(tempLatLng).title(t.getName()).snippet("State: " + t.getCurrentStateString(t.getCurrentState()))));
        }

        tractorMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, 18.0f));
    }
}
