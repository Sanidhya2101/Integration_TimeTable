package com.example.timetable_integration.Models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Class_ {

    String Code,Duration,Name,tags,Platform;
    Date Time,Deadline;
    ArrayList<Slot> Slots;

    public Class_() {

    }

    public Class_(String code, String duration, String name, String tags, String platform, Date time, Date deadline, ArrayList<Slot> slots) {
        Code = code;
        Duration = duration;
        Name = name;
        this.tags = tags;
        Platform = platform;
        Time = time;
        Deadline = deadline;
        Slots = slots;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Timestamp deadline) {
        Deadline = deadline;
    }

    public ArrayList<Slot> getSlots() {
        return Slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        Slots = slots;
    }
}
