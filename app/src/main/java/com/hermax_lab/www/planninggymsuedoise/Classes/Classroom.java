package com.hermax_lab.www.planninggymsuedoise.Classes;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Guillaume on 5/13/16.
 */
@IgnoreExtraProperties
public class Classroom {

    String classroom_access;
    String classroom_address;
    int classroom_capacity;
    String classroom_city;
    String classroom_country_code;
    String classroom_description;
    boolean classroom_display;
    boolean classroom_feature_credit;
    boolean classroom_feature_locker;
    boolean classroom_feature_parking;
    boolean classroom_feature_shower;
    float classroom_gps_lat;
    float classroom_gps_lon;
    int classroom_id;
    String classroom_name;
    String classroom_photo_large_url;
    String classroom_photo_url;
    int classroom_type;
    String classroom_zip;
    int location_id;


    /*

    public ArrayList<Long> getLevels_list() {
        return levels_list;
    }

    public void setLevels_list(ArrayList<Long> levels_list) {
        this.levels_list = levels_list;
    }

    ArrayList<Long> levels_list;

    public ArrayList<String> getStaff_list() {
        return staff_list;
    }

    public void setStaff_list(ArrayList<String> staff_list) {
        this.staff_list = staff_list;
    }

    ArrayList<String> staff_list;

*/
    public Classroom(){

    }

    public boolean isClassroom_display() {
        return classroom_display;
    }

    public void setClassroom_display(boolean classroom_display) {
        this.classroom_display = classroom_display;
    }

    public String getClassroom_access() {
        return classroom_access;
    }

    public String getClassroom_address() {
        return classroom_address;
    }

    public int getClassroom_capacity() {
        return classroom_capacity;
    }

    public String getClassroom_city() {
        return classroom_city;
    }

    public String getClassroom_country_code() {
        return classroom_country_code;
    }

    public String getClassroom_description() {
        return classroom_description;
    }

    public boolean isClassroom_feature_credit() {
        return classroom_feature_credit;
    }

    public boolean isClassroom_feature_locker() {
        return classroom_feature_locker;
    }

    public boolean isClassroom_feature_parking() {
        return classroom_feature_parking;
    }

    public boolean isClassroom_feature_shower() {
        return classroom_feature_shower;
    }

    public float getClassroom_gps_lat() {
        return classroom_gps_lat;
    }

    public float getClassroom_gps_lon() {
        return classroom_gps_lon;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public String getClassroom_name() {
        return classroom_name;
    }

    public String getClassroom_photo_large_url() {
        return classroom_photo_large_url;
    }

    public String getClassroom_photo_url() {
        return classroom_photo_url;
    }

    public int getClassroom_type() {
        return classroom_type;
    }

    public String getClassroom_zip() {
        return classroom_zip;
    }

    public int getLocation_id() {
        return location_id;
    }




}
