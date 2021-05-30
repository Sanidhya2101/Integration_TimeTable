package com.example.timetable_integration.Models;

public class Slot {

    String Day,Time;

    public Slot() {
    }

    public Slot(String day, String time) {
        Day = day;
        Time = time;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
