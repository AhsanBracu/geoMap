package com.example.ahsan.geotask;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.ahsan.geotask.model.Task;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    DatabaseHelper databaseHelper;
    ArrayList<Task> arrayList = new ArrayList<Task>();
    ListView listView;
    Task_Adapter adapter;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        add=(Button)findViewById(R.id.add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        databaseHelper = new DatabaseHelper(this);
        arrayList = databaseHelper.getTask();
        adapter = new Task_Adapter(getApplicationContext(), arrayList);
        listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Task task=adapter.getItem(position);
               // Task tsk=arrayList.get(position);
               // long id_2=task.getId();
               // Log.e("task name ","t "+tsk.title+" "+tsk.id);
                int Id= adapter.getItem(position).getId();
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra("ID",Id);
                Log.e("id_1 ", " " + Id);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),AddActivity.class));
            }
        });

    }
}
