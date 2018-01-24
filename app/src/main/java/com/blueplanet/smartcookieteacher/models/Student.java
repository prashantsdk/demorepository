package com.blueplanet.smartcookieteacher.models;

import java.util.ArrayList;

/**
 * Created by 1311 on 26-11-2015.
 */
public class Student {

    private int _id = -1;
    private String _stdName = null;
    private String _stdFatherName = null;
    private String _stdSchoolName = null;
    private String _stdClass = null;
    private String _stdAddress = null;
    private String _stdGender = null;
    private String _stdDOB = null;
    private String _stdAge = null;
    private String _stdCity = null;
    private String _stdEmail = null;
    private String _stdPRN = null;

    private String _schoolId = null;
    private String _stdDate = null;
    private String _stdDiv = null;
    private String _stdHobbies = null;
    private String _stdCountry = null;
    private String _stdClassTeacherName = null;
    private String _stdImageUrl = null;
    private int _inputId = -1;
    private int _totalStudentCount = -1;
    private String _stdsubcode = null;
    private String _stdsubname = null;

    /**
     * constructor
     *
     * @param id
     * @param stdName
     * @param stdFatherName
     * @param stdSchoolName
     * @param stdClass
     * @param stdAddress
     * @param stdGender
     * @param stdDOB
     * @param stdAge
     * @param stdCity
     * @param stdEmail
     * @param stdPRN
     * @param schoolId
     * @param stdDate
     * @param stdDiv
     * @param stdHobbies
     * @param stdCountry
     * @param stdClassTeacherName
     * @param stdImageUrl
     */
    public Student(int id, String stdName, String stdFatherName, String stdSchoolName,
                   String stdClass, String stdAddress, String stdGender, String stdDOB,
                   String stdAge, String stdCity, String stdEmail, String stdPRN,
                   String schoolId, String stdDate, String stdDiv, String stdHobbies,
                   String stdCountry, String stdClassTeacherName, String stdImageUrl, int inputId, int totalStudentCount) {

        _id = id;
        _stdName = stdName;
        _stdFatherName = stdFatherName;
        _stdSchoolName = stdSchoolName;
        _stdClass = stdClass;
        _stdAddress = stdAddress;
        _stdGender = stdGender;
        _stdDOB = stdDOB;
        _stdAge = stdAge;
        _stdCity = stdCity;
        _stdEmail = stdEmail;
        _stdPRN = stdPRN;
        _schoolId = schoolId;
        _stdDate = stdDate;
        _stdDiv = stdDiv;
        _stdHobbies = stdHobbies;
        _stdCountry = stdCountry;
        _stdClassTeacherName = stdClassTeacherName;
        _stdImageUrl = stdImageUrl;
        _inputId = inputId;
        _totalStudentCount = totalStudentCount;
        //_stdsubcode=stdsubcode;
      //  _stdsubname=stdsubname;

    }




    /**
     * getters for student list data
     */

    public int get_id() {
        return _id;
    }

    public String get_stdName() {
        return _stdName;
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

    public String get_stdGender() {
        return _stdGender;
    }

    public String get_stdAddress() {
        return _stdAddress;
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

    public String get_stdPRN() {
        return _stdPRN;
    }

    public String get_schoolId() {
        return _schoolId;
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

    public String get_stdImageUrl() {
        return _stdImageUrl;
    }

    public int getInputId() {
        return _inputId;
    }

    public int getTotalCount() {
        return _totalStudentCount;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_stdsubcode() {
        return _stdsubcode;
    }

    public void set_stdsubcode(String _stdsubcode) {
        this._stdsubcode = _stdsubcode;
    }

    public String get_stdsubname() {
        return _stdsubname;
    }

    public void set_stdsubname(String _stdsubname) {
        this._stdsubname = _stdsubname;
    }
}
