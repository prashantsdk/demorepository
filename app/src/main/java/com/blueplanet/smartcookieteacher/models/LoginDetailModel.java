package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 21-09-2016.
 */
public class LoginDetailModel {

    private String _modelName = null;

    private String _ipAdd = null;
    private String _version = null;
    public LoginDetailModel() {


    }

    public String get_modelName() {
        return _modelName;
    }

    public void set_modelName(String _modelName) {
        this._modelName = _modelName;
    }

    public String get_ipAdd() {
        return _ipAdd;
    }

    public void set_ipAdd(String _ipAdd) {
        this._ipAdd = _ipAdd;
    }

    public String get_version() {
        return _version;
    }

    public void set_version(String _version) {
        this._version = _version;
    }
}


