package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Sayali on 12/1/2017.
 */
public class SearchStudent
{

    private String _studentname = null;
    private String _searchSchoolId = null;
    private String _searchPrn = null;
    private String _searchimg = null;
    private String _searchbran = null;
    private String _searchdepart = null;


    public SearchStudent(String studentname,String searchSchoolId,String searchPrn,String searchimg,String branch,String depart){

        _studentname=studentname;
        _searchSchoolId=searchSchoolId;
        _searchPrn=searchPrn;
        _searchimg=searchimg;
        _searchbran=branch;
        _searchdepart=depart;

    }

    public String get_studentname() {
        return _studentname;
    }

    public void set_studentname(String _studentname) {
        this._studentname = _studentname;
    }

    public String get_searchSchoolId() {
        return _searchSchoolId;
    }

    public void set_searchSchoolId(String _searchSchoolId) {
        this._searchSchoolId = _searchSchoolId;
    }

    public String get_searchPrn() {
        return _searchPrn;
    }

    public void set_searchPrn(String _searchPrn) {
        this._searchPrn = _searchPrn;
    }

    public String get_searchimg() {
        return _searchimg;
    }

    public void set_searchimg(String _searchimg) {
        this._searchimg = _searchimg;
    }

    public String get_searchbran() {
        return _searchbran;
    }

    public String get_searchdepart() {
        return _searchdepart;
    }
}
