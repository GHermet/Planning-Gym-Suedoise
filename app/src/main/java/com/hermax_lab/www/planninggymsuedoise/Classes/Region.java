package com.hermax_lab.www.planninggymsuedoise.Classes;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Guillaume on 6/6/16.
 */
@IgnoreExtraProperties
public class Region {

    String region_country_code;
    double region_gps_lat;
    double region_gps_lon;
    int region_id;
    int region_map_zoom;
    String region_name;

    public Region(){

    }

    public String getRegion_country_code() {
        return region_country_code;
    }

    public double getRegion_gps_lat() {
        return region_gps_lat;
    }

    public double getRegion_gps_lon() {
        return region_gps_lon;
    }

    public int getRegion_id() {
        return region_id;
    }

    public int getRegion_map_zoom() {
        return region_map_zoom;
    }

    public String getRegion_name() {
        return region_name;
    }
}
