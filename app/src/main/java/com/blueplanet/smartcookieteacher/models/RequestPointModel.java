package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 09-11-2016.
 */
public class RequestPointModel {


    private String _name= null;
    private String _prn = null;
    private String _date = null;


    public RequestPointModel(String name,String prn,String date){

        _name=name;
        _prn=prn;
        _date=date;

    }

    public String get_name() {
        return _name;
    }

    public String get_prn() {
        return _prn;
    }

    public String get_date() {
        return _date;
    }
}
