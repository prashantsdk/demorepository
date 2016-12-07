package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 14-07-2016.
 */
public class AdminThankqPoint {


    private String  _name = null;
    private String _get_reason = null;
    private String _point = null;
    private String _date = null;

    public AdminThankqPoint(String name,String get_reason,String point,String date){

        _name=name;
        _get_reason=get_reason;
        _point=point;
        _date=date;
    }

    public String get_get_reason() {
        return _get_reason;
    }

    public String get_point() {
        return _point;
    }

    public String get_name() {
        return _name;
    }

    public String get_date() {
        return _date;
    }
}
