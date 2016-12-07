package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Avik1 on 27/08/2016.
 */
public class CustomPlaceModel {



    private static String placename="";
    private static String lat="";
    private static String lon="";



    public void CustomPlaceModel(String placename,String lat,String lon){

        this.placename=placename;
        this.lat=lat;
        this.lon=lon;
    }


    public static String getPlacename() {
        return placename;
    }

    public static void setPlacename(String placename) {
        CustomPlaceModel.placename = placename;
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        CustomPlaceModel.lat = lat;
    }

    public static String getLon() {
        return lon;
    }

    public static void setLon(String lon) {
        CustomPlaceModel.lon = lon;
    }
}
