package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 17-02-2016.
 */
public class RegisModel {

    private String _tEmail = null;
    private String _tPass = null;
    public RegisModel(String tEmail, String tPass){

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
