package com.example.ahsan.geotask;

import android.*;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.ahsan.geotask.model.DayClass;
import com.example.ahsan.geotask.model.Task;

public class AddActivity extends AppCompatActivity {


    Button submit;
    ImageButton start_Time, end_time, calender;
    ImageButton map_Show;
    EditText task_title, task_details;
    public static int map_result = 200;
    Day day;
    double lat, lng;
    TextView address, show_drawer, show_date, startTime, stopTime;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    DatabaseHelper databaseHelper;
    String date, time;
    CheckBox sat, sun, mon, tues, wed, thurs, fri;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //mBuilder = new NotificationCompat.Builder(this).setContentTitle("You have wor")
        task_title = (EditText) findViewById(R.id.title);
        task_details = (EditText) findViewById(R.id.description);
        startTime = (TextView) findViewById(R.id.text_start);
        stopTime = (TextView) findViewById(R.id.text_stop);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        databaseHelper = new DatabaseHelper(this);
        sat = (CheckBox) findViewById(R.id.check_saturday);
        sun = (CheckBox) findViewById(R.id.check_sunday);
        mon = (CheckBox) findViewById(R.id.check_monday);
        tues = (CheckBox) findViewById(R.id.check_tuesday);
        wed = (CheckBox) findViewById(R.id.check_wednesday);
        thurs = (CheckBox) findViewById(R.id.check_thursday);
        fri = (CheckBox) findViewById(R.id.check_friday);
        show_date = (TextView) findViewById(R.id.text_showDate);
        // show_drawer = (TextView) findViewById(R.id.drawer_Show);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        start_Time = (ImageButton) findViewById(R.id.start_Time);
        start_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time();
            }
        });
        end_time = (ImageButton) findViewById(R.id.end_Time);
        databaseHelper = new DatabaseHelper(this);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onDrawerClicked(position);

            }
        });
        submit = (Button) findViewById(R.id.submit);
        // show_list = (Button) findViewById(R.id.show_list);
        calender = (ImageButton) findViewById(R.id.calendar);
        map_Show = (ImageButton) findViewById(R.id.show_map);
        address = (TextView) findViewById(R.id.address);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatefield();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submittask();
                startActivity(new Intent(getApplication(), MainActivity.class));
            }
        });
        map_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    Log.e("Check permission ", "check");
                    requestPermission();
                } else {
                    Intent intent = new Intent(getApplication(), MapActivity.class);
                    startActivityForResult(intent, 200);
                }
            }
        });
    }

    public void time() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                time = hourOfDay + ":" + minute;
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void requestPermission() {
        Log.e("request permission ", "request");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION)) {
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow " +
                    "in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                .ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplication(), MapActivity.class);
                    //    startActivity(new Intent(getApplication(), MapActivity.class));
                    startActivityForResult(i, 200);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access" +
                                    " location data.",
                            Toast
                                    .LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onDrawerClicked(int position) {

        show_drawer.setText("Position:::::: " + position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {

            //  if(resultCode==RESULT_OK){
            //String s=data.getStringExtra("Latitude");

            Toast.makeText(getApplication(), "yeeeeee ", Toast.LENGTH_LONG).show();
            lat = data.getDoubleExtra("Latitude", 0.0);
            lng = data.getDoubleExtra("Longitude", 0.0);
            //Toast.makeText(getApplication(),"yeeeeee "+lat+"  "+lng+"  "+s,Toast.LENGTH_LONG).show();
            address.setText("lat " + lat + " lng " + lng);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String address_1 = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                address.setText(address_1 + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void submittask() {
        String title = task_title.getText().toString();
        String details = task_details.getText().toString();
        long date_millisecond;
        long time_millisecond;
        //  Task task=new Task(title,details,lat,lng,1,0.0,0.0,);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date d = simpleDateFormat.parse(date);
            date_millisecond = d.getTime();
            Toast.makeText(getApplicationContext(), "date  " + date + "  time " + time, Toast.LENGTH_LONG).show();
            simpleDateFormat = new SimpleDateFormat("HH:MM");
            java.util.Date time_t = simpleDateFormat.parse(time);
            time_millisecond = time_t.getTime();
            DayClass dayClass = chooseday();
            Toast.makeText(getApplicationContext(), "date  " + date_millisecond + "  time " + time_millisecond, Toast.LENGTH_LONG).show();
            Task task = new Task(title, details, lat, lng, 1, time_millisecond, time_millisecond, date_millisecond);
            long id = databaseHelper.task_insert(task);
            Intent intent = new Intent(getApplication(), DetailActivity.class);
            databaseHelper.dayInsert(dayClass, id);
            intent.putExtra("ID", id);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                            .setContentTitle(task.title)
                            .setContentText(task.detail);

            Intent resultIntent = new Intent(this, DetailActivity.class);
            resultIntent.putExtra("ID", id);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            Calendar calendar=Calendar.getInstance();
            startActivity(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DayClass chooseday() {
        DayClass dayClass = new DayClass();
        if (sat.isChecked())
            dayClass.setSaturday(1);
        if (sun.isChecked())
            dayClass.setSunday(1);
        if (mon.isChecked())
            dayClass.setMonday(1);
        if (tues.isChecked())
            dayClass.setTuesday(1);
        if (wed.isChecked())
            dayClass.setWednesday(1);
        if (thurs.isChecked())
            dayClass.setThursday(1);
        if (fri.isChecked())
            dayClass.setFriday(1);
        return dayClass;

    }

    public void setDatefield() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Toast.makeText(getApplicationContext(), " " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_LONG).show();
                        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        show_date.setText(date);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}