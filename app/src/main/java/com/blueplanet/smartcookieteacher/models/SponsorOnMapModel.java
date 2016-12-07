package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Avik1 on 20/02/2016.
 */
public class SponsorOnMapModel {

    private int id;
    private String SPONSOR_ID = "";
    private String SPONSOR_NAME = "";
    private String SPONSOR_ADDRESS = "";
    private String SPONSOR_CITY = "";
    private String SPONSOR_COUNTRY = "";
    private String SPONSOR_LAT = "";
    private String SPONSOR_LONG = "";
    private String SPONSOR_DISTANCE = "";
    private String SPONSOR_CATEGORY = "";
    private String SPONSOR_IMG = "";



    public SponsorOnMapModel(int id, String SPONSOR_NAME, String SPONSOR_ADDRESS, String SPONSOR_CITY, String SPONSOR_COUNTRY,
                             String SPONSOR_LAT, String SPONSOR_LONG, String SPONSOR_DISTANCE){

        this.id=id;
        this.SPONSOR_NAME=SPONSOR_NAME;
        this.SPONSOR_ADDRESS=SPONSOR_ADDRESS;
        this.SPONSOR_CITY=SPONSOR_CITY;
        this.SPONSOR_COUNTRY=SPONSOR_COUNTRY;
        this.SPONSOR_LAT=SPONSOR_LAT;
        this.SPONSOR_LONG=SPONSOR_LONG;
        this.SPONSOR_DISTANCE=SPONSOR_DISTANCE;

    }

    public SponsorOnMapModel(String SPONSOR_ID, String SPONSOR_NAME, String SPONSOR_ADDRESS, String SPONSOR_CITY, String SPONSOR_COUNTRY,
                             String SPONSOR_LAT, String SPONSOR_LONG, String SPONSOR_DISTANCE, String SPONSOR_CATEGORY, String SPONSOR_IMG){


        this.SPONSOR_ID=SPONSOR_ID;
        this.SPONSOR_NAME=SPONSOR_NAME;
        this.SPONSOR_ADDRESS=SPONSOR_ADDRESS;
        this.SPONSOR_CITY=SPONSOR_CITY;
        this.SPONSOR_COUNTRY=SPONSOR_COUNTRY;
        this.SPONSOR_LAT=SPONSOR_LAT;
        this.SPONSOR_LONG=SPONSOR_LONG;
        this.SPONSOR_DISTANCE=SPONSOR_DISTANCE;
        this.SPONSOR_CATEGORY=SPONSOR_CATEGORY;
        this.SPONSOR_IMG=SPONSOR_IMG;
    }


    public int getId() {
        return id;
    }

    public String getSPONSOR_ID() {
        return SPONSOR_ID;
    }

    public String getSPONSOR_NAME() {
        return SPONSOR_NAME;
    }

    public String getSPONSOR_ADDRESS() {
        return SPONSOR_ADDRESS;
    }

    public String getSPONSOR_CITY() {
        return SPONSOR_CITY;
    }

    public String getSPONSOR_COUNTRY() {
        return SPONSOR_COUNTRY;
    }

    public String getSPONSOR_LAT() {
        return SPONSOR_LAT;
    }

    public String getSPONSOR_LONG() {
        return SPONSOR_LONG;
    }

    public String getSPONSOR_DISTANCE() {
        return SPONSOR_DISTANCE;
    }

    public String getSPONSOR_CATEGORY() {
        return SPONSOR_CATEGORY;
    }

    public String getSPONSOR_IMG() {
        return SPONSOR_IMG;
    }
}
