package com.hermax_lab.www.planninggymsuedoise.Classes;



import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Guillaume on 5/10/16.
 */
@IgnoreExtraProperties
public class Staff {
    int staff_issue_type;
    String staff_adress;
    int staff_business;
    String staff_city;
    String staff_comment;
    String staff_email;
    int staff_id;
    boolean staff_is_country;
    boolean staff_is_global;
    boolean staff_is_teacher;
    boolean staff_is_zone;
    boolean staff_is_host;
    String staff_mobile;
    String staff_name;
    String staff_phone;
    String staff_photo_large_url;
    String staff_photo_url;
    String staff_job;
    ArrayList<Integer> staff_intensites;
    ArrayList<Week_Data> week_data;
    public Staff(){}

    public int getStaff_issue_type() {
        return staff_issue_type;
    }

    public void setStaff_issue_type(int staff_issue_type) {
        this.staff_issue_type = staff_issue_type;
    }

    public String getStaff_comment() {
        return staff_comment;
    }

    public void setStaff_comment(String staff_comment) {
        this.staff_comment = staff_comment;
    }

    public ArrayList<Integer> getStaff_intensites() {
        return staff_intensites;
    }

    public String getStaff_job() {
        if(isStaff_is_teacher()) staff_job="INTERVENANT";
        else if (isStaff_is_host()) staff_job="HÔTE";
        return staff_job;
    }

    public String getStaff_adress() {
        return staff_adress;
    }

    public void setStaff_adress(String staff_adress) {
        this.staff_adress = staff_adress;
    }

    public int getStaff_business() {
        return staff_business;
    }

    public void setStaff_business(int staff_business) {
        this.staff_business = staff_business;
    }

    public String getStaff_city() {
        return staff_city;
    }

    public void setStaff_city(String staff_city) {
        this.staff_city = staff_city;
    }

    public String getStaff_email() {
        return staff_email;
    }

    public void setStaff_email(String staff_email) {
        this.staff_email = staff_email;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public boolean isStaff_is_country() {
        return staff_is_country;
    }

    public void setStaff_is_country(boolean staff_is_country) {
        this.staff_is_country = staff_is_country;
    }

    public boolean isStaff_is_global() {
        return staff_is_global;
    }

    public void setStaff_is_global(boolean staff_is_global) {
        this.staff_is_global = staff_is_global;
    }

    public boolean isStaff_is_teacher() {
        return staff_is_teacher;
    }

    public void setStaff_is_teacher(boolean staff_is_teacher) {
        this.staff_is_teacher = staff_is_teacher;
    }
    public boolean isStaff_is_host() {
        return staff_is_host;
    }

    public void setStaff_is_host(boolean staff_is_host) {
        this.staff_is_host = staff_is_host;
    }

    public boolean isStaff_is_zone() {
        return staff_is_zone;
    }

    public void setStaff_is_zone(boolean staff_is_zone) {
        this.staff_is_zone = staff_is_zone;
    }

    public String getStaff_mobile() {
        return staff_mobile;
    }

    public void setStaff_mobile(String staff_mobile) {
        this.staff_mobile = staff_mobile;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }

    public String getStaff_photo_large_url() {
        return staff_photo_large_url;
    }

    public void setStaff_photo_large_url(String staff_photo_large_url) {
        this.staff_photo_large_url = staff_photo_large_url;
    }

    public String getStaff_photo_url() {
        return staff_photo_url;
    }

    public void setStaff_photo_url(String staff_photo_url) {
        this.staff_photo_url = staff_photo_url;
    }
}
