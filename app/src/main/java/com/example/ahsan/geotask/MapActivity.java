package com.example.ahsan.geotask;

import android.*;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, SearchView
        .OnQueryTextListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, LocationListener {

    MenuItem btnsubmit;
    Button btn;
    public GoogleMap googleMap;
    String location;
    private LatLng mPosition = new LatLng(10.0, 10.0);
    SearchView searchView;
    Circle circle;
    Spinner spinner;
    MarkerOptions marker;
    double radius;
    double lat, lng;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.radius_spinner);
        map.getMapAsync(this);
        buildGoogleApiClient();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double r = Double.parseDouble(spinner.getSelectedItem().toString());
                if (circle != null) {
                    circle.remove();
                }
                circle = googleMap.addCircle(new CircleOptions()
                        .center(mPosition)
                        .radius(r)
                        .strokeColor(Color.BLUE));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.map_menu, menu);
        btnsubmit = menu.findItem(R.id.button_bar);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        btn = (Button) MenuItemCompat.getActionView(btnsubmit);
        btnsubmit.setVisible(false);
        MenuItemCompat.collapseActionView(searchItem);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                btnsubmit.setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                btnsubmit.setVisible(false);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.button_bar:
                Intent intent = new Intent();
                double latitude = mPosition.latitude;
                double longitude = mPosition.longitude;
                Toast.makeText(getApplicationContext(), "Lat " + latitude + "longitude " + longitude, Toast.LENGTH_LONG).show();
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                setResult(200, intent);
                createNotification();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        LatLng latLng = null;
        try {
            List<Address> list = geocoder.getFromLocationName(strAddress, 1);
            if (list.size() == 0)
                return latLng;
            Address address = list.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            latLng = new LatLng(lat, lng);
            //  googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            return new LatLng(lat, lng);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("return lat/lng ", "lat/lng " + lat + " " + lng);
        return latLng;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("query ", query);
        mPosition = getLocationFromAddress(query);
        if (mPosition == null) {
            Toast.makeText(getApplicationContext(), "no address found", Toast.LENGTH_LONG).show();
            return false;
        }
        Log.e("position ", " " + mPosition);
        if (marker != null)
            marker = null;
        marker = new MarkerOptions().position(mPosition).title("Marker in Any");
        googleMap.addMarker(marker);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 16.0f));
        double r = Double.parseDouble(spinner.getSelectedItem().toString());
        Toast.makeText(getApplicationContext(), "nothing: " + query, Toast.LENGTH_LONG).show();
        if (circle != null) {
            circle.remove();
        }
        circle = googleMap.addCircle(new CircleOptions()
                .center(mPosition)
                .radius(r)
                .strokeColor(Color.BLUE));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle("notification")
                        .setContentText("please click you moron");
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
// Builds the notification and issues it.
        mNotificationManager.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        handleLocation(mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("connection cliend", "suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("connection cliend", "failed");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }

    void handleLocation(Location location) {
        Log.e("location", location.getLatitude() + " " + location.getLongitude());
        lat = location.getLatitude();
        lng = location.getLongitude();
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in dhaka"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        LatLng latLng = new LatLng(23.752813, 90.380997);
        if (sydney.equals(latLng)) {
            Toast.makeText(getApplicationContext(), "create notification", Toast.LENGTH_LONG).show();
            createNotification();
        }
    }
}
