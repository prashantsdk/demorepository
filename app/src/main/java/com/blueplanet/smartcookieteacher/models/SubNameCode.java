package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 05-07-2016.
 */
public class SubNameCode {

    private String _subname = null;
    private String _subcode = null;



    public SubNameCode(String subname,String subcode){

        _subname=subname;
        _subcode=subcode;

    }

    public String get_subname() {
        return _subname;
    }

    public String get_subcode() {
        return _subcode;
    }

}
