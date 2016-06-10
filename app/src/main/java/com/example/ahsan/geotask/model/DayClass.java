package com.example.ahsan.geotask.model;

/**
 * Created by ahsan on 6/9/16.
 */
public class DayClass {
    public int friday, saturday, sunday, monday, tuesday, wednesday, thursday;

    public DayClass(int saturday, int sunday, int monday, int tuesday, int wednesday, int thursday, int
            friday) {
        this.saturday = saturday;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
    }

    public DayClass() {
    }

    public void setFriday(int friday) {

        this.friday = friday;
    }

    public int getFriday() {
        return friday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSaturday() {
        return saturday;

    }

    public void setSunday(int sunday) {
        this.sunday = sunday;

    }

    public int getSunday() {
        return sunday;
    }

    public void setMonday(int monday) {
        this.monday = monday;

    }

    public int getMonday() {
        return monday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;

    }

    public int getTuesday() {
        return tuesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;

    }

    public int getWednesday() {
        return wednesday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;

    }

    public int getThursday() {
        return thursday;
    }
}
