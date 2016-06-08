package com.hermax_lab.www.planninggymsuedoise.Classes;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Guillaume on 5/13/16.
 */
@IgnoreExtraProperties
public class Cours {
    int class_bookable_capacity;
    int class_capacity;
    String class_date;
    int class_dayofweek;
    int class_duration;
    boolean class_hasbooking;
    int class_issue_code;
    boolean class_has_issue;
    int class_id;
    boolean class_is_free;
    boolean class_is_closed;
    boolean class_is_packed;
    int class_level;
    String class_level_icon_URL;
    String class_level_text;
    boolean class_pass_blue;
    boolean class_pass_green;
    boolean class_pass_white;
    boolean class_pass_yellow;
    String class_startdate;
    String class_starttime;
    int class_state;
    int class_type;
    int class_unit_value;
    int class_week;
    int classroom_id;
    float NotePonderation;
    int location_id;
    public Cours(){

    }

    public boolean isClass_has_issue() {
        return class_has_issue;
    }

    public int getClass_issue_code() {
        return class_issue_code;
    }

    public int getClass_state() {
        return class_state;
    }

    public void setClass_state(int class_state) {
        this.class_state = class_state;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getClass_bookable_capacity() {
        return class_bookable_capacity;
    }

    public void setClass_bookable_capacity(int class_bookable_capacity) {
        this.class_bookable_capacity = class_bookable_capacity;
    }

    public int getClass_capacity() {
        return class_capacity;
    }

    public void setClass_capacity(int class_capacity) {
        this.class_capacity = class_capacity;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public int getClass_dayofweek() {
        return class_dayofweek;
    }

    public void setClass_dayofweek(int class_dayofweek) {
        this.class_dayofweek = class_dayofweek;
    }

    public int getClass_duration() {
        return class_duration;
    }

    public void setClass_duration(int class_duration) {
        this.class_duration = class_duration;
    }

    public boolean isClass_hasbooking() {
        return class_hasbooking;
    }

    public void setClass_hasbooking(boolean class_hasbooking) {
        this.class_hasbooking = class_hasbooking;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public boolean isClass_is_free() {
        return class_is_free;
    }

    public void setClass_is_free(boolean class_is_free) {
        this.class_is_free = class_is_free;
    }

    public boolean isClass_is_closed() {
        return class_is_closed;
    }

    public void setClass_is_closed(boolean class_is_closed) {
        this.class_is_closed = class_is_closed;
    }

    public boolean isClass_is_packed() {
        return class_is_packed;
    }

    public void setClass_is_packed(boolean class_is_packed) {
        this.class_is_packed = class_is_packed;
    }

    public int getClass_level() {
        return class_level;
    }

    public void setClass_level(int class_level) {
        this.class_level = class_level;
    }

    public String getClass_level_icon_URL() {
        return class_level_icon_URL;
    }

    public void setClass_level_icon_URL(String class_level_icon_URL) {
        this.class_level_icon_URL = class_level_icon_URL;
    }

    public String getClass_level_text() {
        return class_level_text;
    }

    public void setClass_level_text(String class_level_text) {
        this.class_level_text = class_level_text;
    }

    public boolean isClass_pass_blue() {
        return class_pass_blue;
    }

    public void setClass_pass_blue(boolean class_pass_blue) {
        this.class_pass_blue = class_pass_blue;
    }

    public boolean isClass_pass_green() {
        return class_pass_green;
    }

    public void setClass_pass_green(boolean class_pass_green) {
        this.class_pass_green = class_pass_green;
    }

    public boolean isClass_pass_white() {
        return class_pass_white;
    }

    public void setClass_pass_white(boolean class_pass_white) {
        this.class_pass_white = class_pass_white;
    }

    public boolean isClass_pass_yellow() {
        return class_pass_yellow;
    }

    public void setClass_pass_yellow(boolean class_pass_yellow) {
        this.class_pass_yellow = class_pass_yellow;
    }

    public String getClass_startdate() {
        return class_startdate;
    }

    public void setClass_startdate(String class_startdate) {
        this.class_startdate = class_startdate;
    }

    public String getClass_starttime() {
        return class_starttime;
    }

    public void setClass_starttime(String class_starttime) {
        this.class_starttime = class_starttime;
    }

    public int getClass_type() {
        return class_type;
    }

    public void setClass_type(int class_type) {
        this.class_type = class_type;
    }

    public int getClass_unit_value() {
        return class_unit_value;
    }

    public void setClass_unit_value(int class_unit_value) {
        this.class_unit_value = class_unit_value;
    }

    public int getClass_week() {
        return class_week;
    }

    public void setClass_week(int class_week) {
        this.class_week = class_week;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public float getNotePonderation() {
        return NotePonderation;
    }

    public void setNotePonderation(float notePonderation) {
        NotePonderation = notePonderation;
    }


}
