package com.hermax_lab.www.planninggymsuedoise.Enums;

/**
 * Created by Guillaume on 5/10/16.
 */
public enum GSAPI {

    REF("https://ws.gymsuedoise.com/api/V2/"),
    KEY("XE2uG449BC");


    private String stringValue;
    GSAPI(String toString) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
