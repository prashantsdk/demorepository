package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 07-01-2016.
 */
public class RewardPointLog {

    private String _points = null;
    private String _stuDate = null;
    private String _point_date = null;
    private String _reason = null;



    private String _comment = null;

    public RewardPointLog(String points,String stName,String pointDate,String reason,String comment){

        _points=points;
        _stuDate=stName;
        _point_date=pointDate;
        _reason=reason;
        _comment=comment;
    }

    public String get_points() {
        return _points;
    }

    public String get_stuDate() {
        return _stuDate;
    }

    public String get_point_date() {
        return _point_date;
    }

    public String get_reason() {
        return _reason;
    }
    public String get_comment() {
        return _comment;
    }

}
