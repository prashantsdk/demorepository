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

    public void set_subname(String _subname) {
        this._subname = _subname;
    }

    public String get_subcode() {
        return _subcode;
    }

    public void set_subcode(String _subcode) {
        this._subcode = _subcode;
    }
}
