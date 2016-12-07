package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 04-02-2016.
 */
public class Subjectwise_student {

    private String _id = null;
    private String _stdFullName = null;
    private String _stFName = null;
    private String _stdLName = null;
    private String _stdImage = null;

    public Subjectwise_student(String id, String stdFullName, String stFName, String stdLName, String stdImage) {

        _id = id;
        _stdFullName = stdFullName;
        _stFName = stFName;
        _stdLName = stdLName;
        _stdImage = stdImage;
    }

    public String get_id() {
        return _id;
    }

    public String get_stdFullName() {
        return _stdFullName;
    }

    public String get_stFName() {

        return _stFName;
    }

    public String get_stdLName() {
        return _stdLName;
    }

    public String get_stdImage() {

        return _stdImage;
    }
}
