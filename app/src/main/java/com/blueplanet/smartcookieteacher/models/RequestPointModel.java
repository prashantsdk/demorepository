package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 09-11-2016.
 */
public class RequestPointModel {


    private String _name= null;
    private String _point = null;
    private String _date = null;
    private String _reason = null;
    private  String _img=null;
    private  String _type=null;

    private  String _stuprn=null;
    private  String _reasonID=null;




    public RequestPointModel(String name,String point,String date,String reason,String img,String type ,String stuprn,String reasonID){

        _name=name;
        _point=point;
        _date=date;
        _reason=reason;
        _img=img;
        _type=type;
        _stuprn=stuprn;
        _reasonID=reasonID;

    }

    public String get_name() {
        return _name;
    }

    public String get_point() {
        return _point;
    }

    public String get_date() {
        return _date;
    }

    public String get_reason() {
        return _reason;
    }

    public String get_img() {
        return _img;
    }

    public String get_stuprn() {
        return _stuprn;
    }

    public String get_type() {
        return _type;
    }

    public String get_reasonID() {
        return _reasonID;
    }
}
