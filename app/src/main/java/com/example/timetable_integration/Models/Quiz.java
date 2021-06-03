package com.example.timetable_integration.Models;

import java.sql.Timestamp;
import java.util.Date;

public class Quiz {

    String Code,Duration,Name,Platform,tags;
    Date Time;

    public Quiz() {
    }

    public Quiz(String code, String duration, String name, String platform, String tags, Date time) {
        Code = code;
        Duration = duration;
        Name = name;
        Platform = platform;
        this.tags = tags;
        Time = time;
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

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String plateform) {
        Platform = plateform;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
