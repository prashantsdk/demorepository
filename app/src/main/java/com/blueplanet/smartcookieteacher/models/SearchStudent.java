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
    private String _stdFatherName = null;
    private String _stdSchoolName = null;
    private String _stdClass = null;
    private String _stdAddress = null;
    private String _stdGender = null;
    private String _stdDOB = null;
    private String _stdAge = null;
    private String _stdCity = null;
    private String _stdEmail = null;

    private String _stdDate = null;
    private String _stdDiv = null;
    private String _stdHobbies = null;
    private String _stdCountry = null;
    private String _stdClassTeacherName = null;
    private int _inputId = -1;
    private int _totalStudentCount = -1;
    private String _stdsubcode = null;
    private String _stdsubname = null;


    public SearchStudent(String studentname,String searchSchoolId,String searchPrn,String searchimg,String branch,String depart,
                         String stdFatherName, String stdSchoolName, String stdClass, String stdAddress, String _tdGender,
                                  String stdDOB, String stdAge, String stdCity, String stdEmail, String stdDate,
                                  String stdDiv, String stdHobbies, String stdCountry, String stdClassTeacherName, int inputId ,
                                  int totalStudentCount){

        _studentname=studentname;
        _searchSchoolId=searchSchoolId;
        _searchPrn=searchPrn;
        _searchimg=searchimg;
        _searchbran=branch;
        _searchdepart=depart;

        _stdFatherName = stdFatherName;
        _stdSchoolName = stdSchoolName;
        _stdClass = stdClass;
        _stdAddress = stdAddress;
        _stdGender = _tdGender;
        _stdDOB = stdDOB;
        _stdAge = stdAge;
        _stdCity = stdCity;
        _stdEmail = stdEmail;
        _stdDate = stdDate;
        _stdDiv = stdDiv;
        _stdHobbies = stdHobbies;
        _stdCountry = stdCountry;
        _stdClassTeacherName = stdClassTeacherName;
        _inputId = inputId;
        _totalStudentCount = totalStudentCount;
    }

    public SearchStudent(String searchname, String searchSchoolId, String searchPrn, String searchImg, String searchbbranch, String searchdepart) {
        _studentname=searchname;
        _searchSchoolId=searchSchoolId;
        _searchPrn=searchPrn;
        _searchimg=searchImg;
        _searchbran=searchbbranch;
        _searchdepart=searchdepart;
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

    public String get_stdFatherName() {
        return _stdFatherName;
    }

    public String get_stdSchoolName() {
        return _stdSchoolName;
    }

    public String get_stdClass() {
        return _stdClass;
    }

    public String get_stdAddress() {
        return _stdAddress;
    }

    public String get_stdGender() {
        return _stdGender;
    }

    public String get_stdDOB() {
        return _stdDOB;
    }

    public String get_stdAge() {
        return _stdAge;
    }

    public String get_stdCity() {
        return _stdCity;
    }

    public String get_stdEmail() {
        return _stdEmail;
    }

    public String get_stdDate() {
        return _stdDate;
    }

    public String get_stdDiv() {
        return _stdDiv;
    }

    public String get_stdHobbies() {
        return _stdHobbies;
    }

    public String get_stdCountry() {
        return _stdCountry;
    }

    public String get_stdClassTeacherName() {
        return _stdClassTeacherName;
    }

    public int get_inputId() {
        return _inputId;
    }

    public int get_totalStudentCount() {
        return _totalStudentCount;
    }

    public String get_stdsubcode() {
        return _stdsubcode;
    }

    public String get_stdsubname() {
        return _stdsubname;
    }
}
