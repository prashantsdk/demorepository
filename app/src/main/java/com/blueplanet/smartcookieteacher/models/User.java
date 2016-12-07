package com.blueplanet.smartcookieteacher.models;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;

/**
 * Created by 1311 on 20-02-2016.
 */
public class User {

    private String _userName = null;

    private String _password = null;
    private String _prn = null;

    private boolean _rememberMe = false;


    public User(){

    }
    public User(String userName, String password, boolean rememberMe,String prn) {
        _userName = userName;
        _password = password;
        _rememberMe = rememberMe;
        _prn=prn;

    }

    public boolean isRememberMe() {
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
}
