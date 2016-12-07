package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Avik1 on 20/02/2016.
 */
public class SchoolOnMapModel {

    private int id;
    private String SCHOOL_ID = "";
    private String SCHOOL_NAME = "";
    private String SCHOOL_ADDRESS = "";
    private String SCHOOL_CITY = "";
    private String SCHOOL_COUNTRY = "";
    private String SCHOOL_LAT = "";
    private String SCHOOL_LONG = "";
    private String SCHOOL_DISTANCE = "";
    private String SCHOOL_STUDENTS_COUNT = "";
    private String SCHOOL_IMG = "";



    public SchoolOnMapModel(int id, String SCHOOL_NAME, String SCHOOL_ADDRESS, String SCHOOL_CITY, String SCHOOL_COUNTRY,
                            String SCHOOL_LAT, String SCHOOL_LONG, String SCHOOL_DISTANCE){

        this.id=id;
        this.SCHOOL_NAME=SCHOOL_NAME;
        this.SCHOOL_ADDRESS=SCHOOL_ADDRESS;
        this.SCHOOL_CITY=SCHOOL_CITY;
        this.SCHOOL_COUNTRY=SCHOOL_COUNTRY;
        this.SCHOOL_LAT=SCHOOL_LAT;
        this.SCHOOL_LONG=SCHOOL_LONG;
        this.SCHOOL_DISTANCE=SCHOOL_DISTANCE;

    }

    public SchoolOnMapModel(String SCHOOL_ID, String SCHOOL_NAME, String SCHOOL_ADDRESS, String SCHOOL_CITY, String SCHOOL_COUNTRY,
                            String SCHOOL_LAT, String SCHOOL_LONG, String SCHOOL_DISTANCE, String SCHOOL_STUDENTS_COUNT, String SCHOOL_IMG){


        this.SCHOOL_ID=SCHOOL_ID;
        this.SCHOOL_NAME=SCHOOL_NAME;
        this.SCHOOL_ADDRESS=SCHOOL_ADDRESS;
        this.SCHOOL_CITY=SCHOOL_CITY;
        this.SCHOOL_COUNTRY=SCHOOL_COUNTRY;
        this.SCHOOL_LAT=SCHOOL_LAT;
        this.SCHOOL_LONG=SCHOOL_LONG;
        this.SCHOOL_DISTANCE=SCHOOL_DISTANCE;
        this.SCHOOL_STUDENTS_COUNT=SCHOOL_STUDENTS_COUNT;
        this.SCHOOL_IMG=SCHOOL_IMG;
    }


    public int getId() {
        return id;
    }

    public String getSCHOOL_ID() {
        return SCHOOL_ID;
    }

    public String getSCHOOL_NAME() {
        return SCHOOL_NAME;
    }

    public String getSCHOOL_ADDRESS() {
        return SCHOOL_ADDRESS;
    }

    public String getSCHOOL_CITY() {
        return SCHOOL_CITY;
    }

    public String getSCHOOL_COUNTRY() {
        return SCHOOL_COUNTRY;
    }

    public String getSCHOOL_LAT() {
        return SCHOOL_LAT;
    }

    public String getSCHOOL_LONG() {
        return SCHOOL_LONG;
    }

    public String getSCHOOL_DISTANCE() {
        return SCHOOL_DISTANCE;
    }

    public String getSCHOOL_STUDENTS_COUNT() {
        return SCHOOL_STUDENTS_COUNT;
    }

    public String getSCHOOL_IMG() {
        return SCHOOL_IMG;
    }
}
