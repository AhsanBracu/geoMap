package com.example.ahsan.geotask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.example.ahsan.geotask.model.DayClass;
import com.example.ahsan.geotask.model.Task;

/**
 * Created by ahsan on 11/17/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Task_Details";
    public static final int DB_VERSION = 1;
    public static final String TASK_TABLE = "task";
    public static final String CATAGORY_TABLE = "catagory";
    public static final String ALARM_TABLE = "alarm";
    public static final String DAY_TABLE = "day_table";


    /////////////////task table
    public static final String ID_FIELD = "_id";
    public static final String TITLE_FIELD = "title";
    public static final String DETAIL_FIELD = "detail";
    public static final String SOURCE_LAT_FIELD = "s_lat";
    public static final String SOURCE_LNG_FIELD = "s_lng";
    public static final String STATUS_FIELD = "status";
    public static final String START_TIME_FIELD = "start_time";
    public static final String FINISH_TIME_FIELD = "finish_time";
    public static final String DUE_DATE = "due_date";
    public static final String ACTIVE_DAY = "active_day";
///////////////////////

    //////////////day table
    public static final String DAY_ID_FIELD = "_id";
    public static final String TASK_DAY_ID_FIELD = "task_day_id";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";

    public static final String TASK_TABLE_SQL = "CREATE TABLE "
            + TASK_TABLE + " (" + ID_FIELD + " integer, "
            + TITLE_FIELD + " TEXT, " + DETAIL_FIELD + " text, "
            + SOURCE_LAT_FIELD + " double, " + SOURCE_LNG_FIELD + " double, "
            + DUE_DATE + " double, " + ACTIVE_DAY + " integer, "
            + STATUS_FIELD + " integer, " + START_TIME_FIELD + " double, "
            + FINISH_TIME_FIELD + " double, primary key (" + ID_FIELD + "))";

    public static final String DAY_TABLE_SQL = "CREATE TABLE " + DAY_TABLE
            + "(" + DAY_ID_FIELD + " integer primary key autoincrement, "
            + TASK_DAY_ID_FIELD + " integer, " + SATURDAY + " integer, " + SUNDAY + " integer, "
            + MONDAY + " integer, " + TUESDAY + " integer, " + WEDNESDAY + " integer, "
            + THURSDAY + " integer, " + FRIDAY + " integer , foreign key (" + TASK_DAY_ID_FIELD
            + ") references " + TASK_TABLE + " (" + ID_FIELD + " ))";

	/*
     * " foreign key (" + APP_CHECK_FIELD + ") REFERENCES " + ALARM_TABLE + " ("
	 * + CHECK_FIELD +
	 * "), f_name text,foreign key(f_name) references catagory(name))";
	 */

	/*
     * APP_CATAGORY_FIELD + " TEXT, foreign key(" + APP_CATAGORY_FIELD +
	 * ") REFERENCES " + CATAGORY_TABLE + "(" + CATAGORY_FIELD +
	 * "), primary key (" + TITLE_FIELD + "," + APP_CATAGORY_FIELD + "))";
	 *
	 * ;
	 */

    // ", FOREIGN KEY (" + APP_CHECK_FIELD + ") REFERENCES "
    // + ALARM_TABLE + " ( " + CHECK_FIELD + " ))";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(TASK_TABLE_SQL);
        Log.e("TABLE CREATE", TASK_TABLE_SQL);
        // db.execSQL(ALARM_TABLE_SQL);
        // Log.e("TABLE CREATE", ALARM_TABLE_SQL);
        db.execSQL(DAY_TABLE_SQL);
        Log.e("TABLE CREATE", DAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade logic

    }


    long task_insert(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, task.title);
        values.put(DETAIL_FIELD, task.detail);
        values.put(SOURCE_LAT_FIELD, task.lat);
        values.put(SOURCE_LNG_FIELD, task.lng);
        values.put(START_TIME_FIELD, task.start_time);
        values.put(FINISH_TIME_FIELD, task.finish_time);
        values.put(STATUS_FIELD, task.status);
        values.put(DUE_DATE, task.due_date);
        long inserted = database.insert(TASK_TABLE, null, values);
        return inserted;
    }

    long dayInsert(DayClass day, long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SATURDAY, day.getSaturday());
        values.put(SUNDAY, day.getSunday());
        values.put(MONDAY, day.getMonday());
        values.put(TUESDAY, day.getTuesday());
        values.put(WEDNESDAY, day.getWednesday());
        values.put(THURSDAY, day.getThursday());
        values.put(FRIDAY, day.getFriday());
        values.put(TASK_DAY_ID_FIELD, id);
        long inserted = database.insert(DAY_TABLE, null, values);
        return inserted;
    }

    long dateinsert(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, task.title);
        values.put(DETAIL_FIELD, task.detail);
        values.put(SOURCE_LAT_FIELD, task.lat);
        values.put(SOURCE_LNG_FIELD, task.lng);
        values.put(START_TIME_FIELD, task.start_time);
        values.put(FINISH_TIME_FIELD, task.finish_time);
        values.put(STATUS_FIELD, task.status);
        values.put(DUE_DATE, task.due_date);
        long inserted = database.insert(TASK_TABLE, null, values);
        return inserted;

    }


    public ArrayList<Task> getTask() {
        ArrayList<Task> arrayList = new ArrayList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        Task task;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String title = cursor.getString(cursor
                        .getColumnIndex(TITLE_FIELD));
                String description = cursor.getString(cursor.getColumnIndex(DETAIL_FIELD));
                double lat = cursor.getDouble(cursor.getColumnIndex(SOURCE_LAT_FIELD));
                double lng = cursor.getDouble(cursor.getColumnIndex(SOURCE_LNG_FIELD));
                double start_Time = cursor.getDouble(cursor.getColumnIndex(START_TIME_FIELD));
                double end_time = cursor.getDouble(cursor.getColumnIndex(FINISH_TIME_FIELD));
                int status = cursor.getInt(cursor.getColumnIndex(STATUS_FIELD));
                double date = cursor.getDouble(cursor.getColumnIndex(DUE_DATE));
                task = new Task(title, description, lat, lng, status, start_Time, end_time, date);
                task.setId(cursor.getInt(cursor.getColumnIndex(ID_FIELD)));
                arrayList.add(task);
                cursor.moveToNext();
            }
        }

        return arrayList;
    }


    public Task getTaskDetail(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE + " where "
                + ID_FIELD + " = ?";
        // String[] columns={NAME_FIELD, EMAIL_FIELD, PHONE_FIELD};
        // SELECT * FROM EMPLOYEE;
        Cursor cursor = db.rawQuery(query, new String[]{id});
        Task task = null;
        Log.e("title ", "lalallalalal");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            // for (int i = 0; i < cursor.getCount(); i++) {
            String title = cursor.getString(cursor
                    .getColumnIndex(TITLE_FIELD));
            String description = cursor.getString(cursor.getColumnIndex(DETAIL_FIELD));
            double lat = cursor.getDouble(cursor.getColumnIndex(SOURCE_LAT_FIELD));
            double lng = cursor.getDouble(cursor.getColumnIndex(SOURCE_LNG_FIELD));
            double start_Time = cursor.getDouble(cursor.getColumnIndex(START_TIME_FIELD));
            double end_time = cursor.getDouble(cursor.getColumnIndex(FINISH_TIME_FIELD));
            int status = cursor.getInt(cursor.getColumnIndex(STATUS_FIELD));
            double date = cursor.getDouble(cursor.getColumnIndex(DUE_DATE));
            task = new Task(title, description, lat, lng, status, start_Time, end_time, date);
        }
        cursor.close();
        db.close();
        return task;

    }

    public void getActiveTask(int status) {
        String id = Integer.toString(status);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE + " where "
                + ID_FIELD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{id});

    }


    long catagory_insert() {
        SQLiteDatabase dp = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(CATAGORY_FIELD, c.name);
        long inserted = dp.insert(CATAGORY_TABLE, null, values);
        return inserted;

    }

    long Appointment_insert() {
      /*  SQLiteDatabase dp = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, a.title);
        values.put(SOURCE_FIELD, a.source);
        values.put(DESTINATION_FIELD, a.destination);
        values.put(APP_CATAGORY_FIELD, a.f_catagory);
        values.put(APP_CHECK_FIELD, a.check);
        values.put(TIME_FIELD, a.time);
        values.put(DATE_FIELD, a.date);
        values.put(NOTES_FIELD, a.notes);
        values.put(TIME_DATE_FIELD, a.time_date);
        values.put(DAY_FIELD, a.day);
        values.put(MONTH_FIELD, a.month);
        values.put(SOURCE_LAT_FIELD, a.s_lat);
        values.put(SOURCE_LNG_FIELD, a.s_lng);
        values.put(DESTINATON_LAT_FIELD, a.d_lat);
        values.put(DESTINATON_LNG_FIELD, a.d_lng);
        values.put(ALARM_FIELD, a.alarm_time);
        long inserted = dp.insert(APPOINTMENT_TABLE, null, values);

        */
        return 0;
    }

    public int Appointment_update() {
        /*SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, a.title);
        values.put(SOURCE_FIELD, a.source);
        values.put(DESTINATION_FIELD, a.destination);
        values.put(APP_CATAGORY_FIELD, a.f_catagory);
        values.put(APP_CHECK_FIELD, a.check);
        values.put(NOTES_FIELD, a.notes);
        values.put(TIME_FIELD, a.time);
        values.put(DATE_FIELD, a.date);
        values.put(TIME_DATE_FIELD, a.time_date);
        values.put(DAY_FIELD, a.day);
        values.put(MONTH_FIELD, a.month);
        values.put(SOURCE_LAT_FIELD, a.s_lat);
        values.put(SOURCE_LNG_FIELD, a.s_lng);
        values.put(DESTINATON_LAT_FIELD, a.d_lat);
        values.put(DESTINATON_LNG_FIELD, a.d_lng);
        values.put(ALARM_FIELD, a.alarm_time);
        return db.update(APPOINTMENT_TABLE, values, A_ID_FIELD + "=?",
                new String[] { String.valueOf(a.id) });
                */
        return 0;
    }

   /* public ArrayList<String> sort() {
        SQLiteDatabase dp = this.getReadableDatabase();
        ArrayList<String> array = new ArrayList<String>();
        Cursor cursor = dp.query(APPOINTMENT_TABLE, null, null, null, null,
                null, TIME_DATE_FIELD);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(cursor.getColumnIndex(A_ID_FIELD));
                String title = cursor.getString(cursor
                        .getColumnIndex(TITLE_FIELD));
                String source = cursor.getString(cursor
                        .getColumnIndex(SOURCE_FIELD));
                String destination = cursor.getString(cursor
                        .getColumnIndex(DESTINATION_FIELD));
                String notes = cursor.getString(cursor
                        .getColumnIndex(NOTES_FIELD));
                String time = cursor.getString(cursor
                        .getColumnIndex(TIME_FIELD));
                String date = cursor.getString(cursor
                        .getColumnIndex(DATE_FIELD));
                int check = cursor.getInt(cursor
                        .getColumnIndex(APP_CHECK_FIELD));
                String catagory = cursor.getString(cursor
                        .getColumnIndex(APP_CATAGORY_FIELD));
                long time_date = cursor.getLong(cursor
                        .getColumnIndex(TIME_DATE_FIELD));
                String day = cursor.getString(cursor.getColumnIndex(DAY_FIELD));
                String month = cursor.getString(cursor
                        .getColumnIndex(MONTH_FIELD));
                double s_lat = cursor.getDouble(cursor
                        .getColumnIndex(SOURCE_LAT_FIELD));
                double s_lng = cursor.getDouble(cursor
                        .getColumnIndex(SOURCE_LNG_FIELD));
                double d_lat = cursor.getDouble(cursor
                        .getColumnIndex(DESTINATON_LAT_FIELD));
                double d_lng = cursor.getDouble(cursor
                        .getColumnIndex(DESTINATON_LNG_FIELD));
                long alarmtime = cursor.getLong(cursor
                        .getColumnIndex(ALARM_FIELD));
             /*   Appointment app = new Appointment(title, notes, source,
                       destination, time, date, catagory, check, time_date,
                        day, month, s_lat, s_lng, d_lat, d_lng, alarmtime, id);
                        */

    //array.add(app);
           /*     cursor.moveToNext();

            }
        } else
            Log.e("problem", "catagory is not inculded in appointment");
        cursor.close();
        dp.close();
        return array;

    }

    public int check_Catagory() {
        String query = "SELECT * FROM " + CATAGORY_TABLE + " where "
                + CATAGORY_FIELD + " is not null";
        SQLiteDatabase dp = this.getReadableDatabase();
        Cursor c = dp.rawQuery(query, null);
        // Cursor cursor = dp.query(CATAGORY_TABLE, null, null, null, null,
        // null,
        // null);
        // dp.close();
        if (c != null && c.getCount() > 0)
            return 1;
        else
            return 0;

    }

    public int catagory_id() {
        String query = "SELECT " + ID_FIELD + " FROM " + CATAGORY_TABLE;
        SQLiteDatabase dp = this.getReadableDatabase();
        Cursor cursor = dp.rawQuery(query, null);
        cursor.moveToLast();
        int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
		/*
		 * while (cursor.moveToNext()) { id =
		 * cursor.getString(cursor.getColumnIndex(ID_FIELD));
		 *
		 * }
		 */
     /*   cursor.close();
        dp.close();
        return id;

    }

    public int check_appointment(String time, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + TIME_FIELD + ", " + DATE_FIELD + " FROM "
                + APPOINTMENT_TABLE + " where " + TIME_FIELD + " = ? and "
                + DATE_FIELD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { time, date });
        int i = cursor.getCount();
        return i;

    }

    public int check_time_date(String t) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + TIME_DATE_FIELD + " FROM "
                + APPOINTMENT_TABLE + " where " + TIME_DATE_FIELD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{t});
        int i = cursor.getCount();
        return i;
    }

    public int check_timr_date_2(String t, String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + TIME_DATE_FIELD + " FROM "
                + APPOINTMENT_TABLE + " where " + TIME_DATE_FIELD + " = ? AND "
                + A_ID_FIELD + " != ?";
        Cursor cursor = db.rawQuery(query, new String[] { t, id });
        int i = cursor.getCount();
        return i;

    }

    public int count_catagory() {
        String query = "SELECT * FROM " + CATAGORY_TABLE;
        SQLiteDatabase dp = this.getReadableDatabase();
        Cursor cursor = dp.rawQuery(query, null);

        return cursor.getCount();
    }



    public void DeleteApp(String i) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(APPOINTMENT_TABLE, A_ID_FIELD + "=?", new String[] { i });
        db.close();
    }
*/
}