package com.blueplanet.smartcookieteacher.models;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;

/**
 * Created by 1311 on 20-02-2016.
 */
public class User {

    private String _userName = null;

    private String userEmailId = null;
    private String typeOne ="1";
    private String userMobileNo = null;
    private String typeTwo = "2";
    private String memberId = null;
    private String typeThree ="3";


    private String _password = null;
    private String _prn = null;
    private String _rememberMe = null;




    public User(){

    }


    public User(String userName, String password, String rememberMe, String prn) {
        _userName = userName;
        _password = password;
        _rememberMe = rememberMe;
        _prn=prn;

    }

    public User(String emailId,String password,String rememberMe,String prn,String typeOne,String typeTwo){

        userEmailId = emailId;
        _password = password;
        _rememberMe = rememberMe;
        _prn=prn;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;


    }

    public User(String mobileNo,String password,String rememberMe,String prn,String typeOne){

        userMobileNo = mobileNo;
        _password = password;
        _rememberMe = rememberMe;
        _prn=prn;
        this.typeOne = typeOne;
    }


    public User(String memberId,String password,String rememberMe,String prn,String typeOne,String typeTwo,String typeThree){

        this.memberId = memberId;
        _password = password;
        _rememberMe = rememberMe;
        _prn=prn;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
        this.typeThree = typeThree;
    }



    public String isRememberMe() {
        return _rememberMe;
    }

    public String getPassword1() {
        return _password;
    }

    public String getUserName() {
        return _userName;
    }

    public String get_prn() {
        return _prn;
    }



    public String getUserEmailId() {
        return userEmailId;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public String getMemberId() {
        return memberId;
    }
}
