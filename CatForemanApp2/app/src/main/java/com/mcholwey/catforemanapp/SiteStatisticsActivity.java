package com.mcholwey.catforemanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Owner on 4/3/2016.
 */
public class SiteStatisticsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
