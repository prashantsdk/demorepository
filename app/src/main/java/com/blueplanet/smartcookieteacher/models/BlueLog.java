package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 12-02-2016.
 */
public class BlueLog {


    private String _point = null;
    private String _point_date = null;
    private String _reason = null;
    private String _compName = null;

    public BlueLog(String points,String point_date,String reason,String compName){

        _point=points;
        _point_date=point_date;
        _reason=reason;
        _compName=compName;
    }

    public String get_point() {
        return _point;
    }

    public String get_point_date() {
        return _point_date;
    }

    public String get_reason() {
        return _reason;
    }

    public String get_compName() {
        return _compName;
    }
}
