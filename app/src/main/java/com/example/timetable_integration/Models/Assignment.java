package com.example.timetable_integration.Models;

import java.util.Date;

public class Assignment {

    String Code,Name,Platform,Status,tags;
    Date Deadline;


    public Assignment() {
    }

    public Assignment(String code, Date deadline, String name, String platform, String status, String tags) {
        Code = code;
        Deadline = deadline;
        Name = name;
        Platform = platform;
        Status = status;
        this.tags = tags;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        Deadline = deadline;
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

    public void setPlatform(String platform) {
        Platform = platform;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
