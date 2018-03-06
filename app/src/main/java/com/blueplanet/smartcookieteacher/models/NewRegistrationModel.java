package com.blueplanet.smartcookieteacher.models;

import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by Sayali on 1/8/2018.
 */
public class NewRegistrationModel {

    private String  _userIdname = null;
    private String _compname = null;
    private String _fname = null;
    private String _mname = null;
    private String _lname = null;
    private String _gender = null;
    private String _address = null;
    private String _city = null;
    private String _country = null;
    private String _state = null;
    private String _phone = null;
    private String _regpassward = null;
    private String _countryucode = null;
    private String _email = null;
    private String _imgpath = null;
    private String _imgname = null;
    private String _dob = null;

    private String _password = null;


    public NewRegistrationModel(String userIdname,String compname,String fname,String mname,String lname,String address,String city,String country,String state,String phone,
                                String regpassward,String countryucode,String email,String imgpath,String imgname/*, String dob, String password*/){


        _userIdname=userIdname;
        _compname=compname;
        _fname=fname;
        _mname=mname;
        _lname=lname;
        _address=address;
        _city=city;
        _country=country;
        _state=state;
        _phone=phone;
        _regpassward=regpassward;
        _countryucode=countryucode;
        _email=email;
        _imgpath=imgpath;
        _imgname=imgname;

    }

    public String get_userIdname() {
        return _userIdname;
    }

    public String get_compname() {
        return _compname;
    }

    public String get_fname() {
        return _fname;
    }

    public String get_lname() {
        return _lname;
    }

    public String get_address() {
        return _address;
    }

    public String get_city() {
        return _city;
    }

    public String get_country() {
        return _country;
    }

    public String get_state() {
        return _state;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_regpassward() {
        return _regpassward;
    }

    public String get_countryucode() {
        return _countryucode;
    }

    public String get_email() {
        return _email;
    }

    public String get_imgpath() {
        return _imgpath;
    }

    public String get_imgname() {
        return _imgname;
    }

    public String get_mname() {
        return _mname;
    }

    public String get_dob() {
        return _dob;
    }

    public String get_password() {

        return _regpassward;
    }

    public String get_gender() {
        return _gender;

    }
}
