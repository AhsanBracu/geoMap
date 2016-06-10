package com.example.ahsan.geotask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ahsan.geotask.model.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    DatabaseHelper mdDatabaseHelper;
    TextView title, detail, date;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ArrayList<String> arrayList= (ArrayList<String>) Arrays.asList("One", "Two", "Three");
        String[]stringf={"one","two"};
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mdDatabaseHelper = new DatabaseHelper(this);
        title = (TextView) findViewById(R.id.detail_title);
        detail = (TextView) findViewById(R.id.detail_description);
        date = (TextView) findViewById(R.id.detail_date);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.detail_map);
        Intent intent = getIntent();
        int id=intent.getIntExtra("ID",0);
        Log.e("id_2", " " + id);
        task = mdDatabaseHelper.getTaskDetail(Integer.toString(id));
        title.setText(task.title);
        detail.setText(task.detail);
        //date.setText(task.due_date);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                LatLng position = new LatLng(task.lat, task.lng);
                googleMap.addMarker(new MarkerOptions().position(position).title(task.title));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12.0f));
            }
        });


    }

}
