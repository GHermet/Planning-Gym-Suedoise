package com.hermax_lab.www.planninggymsuedoise.Enums;

/**
 * Created by Guillaume on 5/10/16.
 */
public enum GSAPI {


    private String stringValue;
    GSAPI(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
