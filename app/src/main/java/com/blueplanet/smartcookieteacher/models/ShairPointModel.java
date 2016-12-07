package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 01-08-2016.
 */
public class ShairPointModel {
    private String _teacherid = null;
    private String _teacherName = null;
    private String _teacheremail = null;
    private String _teachermobile= null;
    private String _teacherbluePoint = null;

    /**
     * constructor
     *
     *
     */
    public ShairPointModel(String teacherid, String teacherName, String teacheremail,String teachermobile,String teacherbluePoint) {
        _teacherid = teacherid;
        _teacherName = teacherName;
        _teacheremail = teacheremail;
        _teachermobile = teachermobile;
        _teacherbluePoint = teacherbluePoint;

    }

    public String get_teacherid() {
        return _teacherid;
    }

    public String get_teacherName() {
        return _teacherName;
    }

    public String get_teacheremail() {
        return _teacheremail;
    }

    public String get_teachermobile() {
        return _teachermobile;
    }

    public String get_teacherbluePoint() {
        return _teacherbluePoint;
    }
}
