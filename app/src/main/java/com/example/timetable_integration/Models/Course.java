package com.example.timetable_integration.Models;

public class Course {

    String course_name,course_type,course_place,course_time,course_duration,course_colour;

    public Course() {
    }

    public Course(String course_name, String course_type, String course_place, String course_time, String course_duration, String course_colour) {
        this.course_name = course_name;
        this.course_type = course_type;
        this.course_place = course_place;
        this.course_time = course_time;
        this.course_duration = course_duration;
        this.course_colour = course_colour;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getCourse_place() {
        return course_place;
    }

    public void setCourse_place(String course_place) {
        this.course_place = course_place;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public String getCourse_duration() {
        return course_duration;
    }

    public void setCourse_duration(String course_duration) {
        this.course_duration = course_duration;
    }

    public String getCourse_colour() {
        return course_colour;
    }

    public void setCourse_colour(String course_colour) {
        this.course_colour = course_colour;
    }
}
