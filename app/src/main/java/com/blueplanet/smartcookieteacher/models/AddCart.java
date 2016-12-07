package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 16-03-2016.
 */
public class AddCart {
    private String _couId = null;
    private String _coupoints = null;
    private String _coupValidity = null;
    private String _coupName = null;

    public String get_coupName() {
        return _coupName;
    }

    public String get_coupAddress() {
        return _coupAddress;
    }

    public String get_coupimage() {
        return _coupimage;
    }

    private String _coupAddress = null;
    private String _coupimage = null;

    public AddCart( String couId,String coupoints,String coupValidity,String coupName,String coupAddress,String coupimage){

        _couId=couId;
        _coupoints=coupoints;
        _coupValidity=coupValidity;
        _coupName=coupName;
        _coupAddress=coupAddress;
        _coupimage=coupimage;

    }

    public String get_couId() {
        return _couId;
    }

    public String get_coupoints() {
        return _coupoints;
    }

    public String get_coupValidity() {
        return _coupValidity;
    }
}
