package com.blueplanet.smartcookieteacher.models;

/**
 * Created by prashantj on 3/27/2018.
 */

public class GoogelMapSponsor {


    public String SPONSOR_ID = "";
    public String SPONSOR_NAME = "";
    public String SPONSOR_ADDRESS = "";
    public String SPONSOR_CITY = "";
    public String SPONSOR_COUNTRY = "";
    public String SPONSOR_LAT = "";
    public String SPONSOR_LONG = "";
    public String SPONSOR_DISTANCE = "";
    public String SPONSOR_CATEGORY = "";
    public String SPONSOR_IMG = "";

    public String shopName = "";
    public String shopPhoneNo = "";
    public String shopMaxDiscount = "";

    public GoogelMapSponsor(String SPONSOR_ID, String SPONSOR_NAME, String SPONSOR_ADDRESS, String SPONSOR_CITY, String SPONSOR_COUNTRY, String SPONSOR_LAT, String SPONSOR_LONG, String SPONSOR_DISTANCE, String SPONSOR_CATEGORY, String SPONSOR_IMG, String shopName, String shopPhoneNo, String shopMaxDiscount) {
        this.SPONSOR_ID = SPONSOR_ID;
        this.SPONSOR_NAME = SPONSOR_NAME;
        this.SPONSOR_ADDRESS = SPONSOR_ADDRESS;
        this.SPONSOR_CITY = SPONSOR_CITY;
        this.SPONSOR_COUNTRY = SPONSOR_COUNTRY;
        this.SPONSOR_LAT = SPONSOR_LAT;
        this.SPONSOR_LONG = SPONSOR_LONG;
        this.SPONSOR_DISTANCE = SPONSOR_DISTANCE;
        this.SPONSOR_CATEGORY = SPONSOR_CATEGORY;
        this.SPONSOR_IMG = SPONSOR_IMG;
        this.shopName = shopName;
        this.shopPhoneNo = shopPhoneNo;
        this.shopMaxDiscount = shopMaxDiscount;
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

    public String getShopName() {
        return shopName;
    }

    public String getShopPhoneNo() {
        return shopPhoneNo;
    }

    public String getShopMaxDiscount() {
        return shopMaxDiscount;
    }
}
