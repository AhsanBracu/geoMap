package com.example.ahsan.geotask;

/**
 * Created by ahsan on 12/11/15.
 */
enum Day {
    SATURDAY(0), SUNDAY(0), MONDAY(0), TUESDAY(0), WEDNESDAY(0), THURSDAY(0),FRIDAY(0);
    int value;
    Day(int i) {
        value = i;
    }

    int getday() {
        return value;
    }
}

