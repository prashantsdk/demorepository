package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 10-12-2015.
 */
public class Registration  {
    private String _tEmail = null;
    private String _tPass = null;

    public Registration(String tEmail,String tPass){

        _tEmail=tEmail;
        _tPass=tPass;

    }

    public String get_tEmail() {
        return _tEmail;
    }

    public String get_tPass() {
        return _tPass;
    }
}
