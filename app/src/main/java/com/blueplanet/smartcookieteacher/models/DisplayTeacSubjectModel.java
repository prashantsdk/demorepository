package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Sayali on 3/15/2017.
 */
public class DisplayTeacSubjectModel {

    private String _subname = null;
    private String _subcode = null;
    private String _subsemesterid = null;
    private String _subCoursename = null;
    private String _subyear = null;

    public DisplayTeacSubjectModel(String subname,String subcode,String subsemesterid,String subCoursename,String subyear){

        _subname=subname;
        _subcode=subcode;
        _subsemesterid=subsemesterid;
        _subCoursename=subCoursename;
        _subyear=subyear;
    }

    public String get_subname() {
        return _subname;
    }

    public String get_subcode() {
        return _subcode;
    }

    public String get_subCoursename() {
        return _subCoursename;
    }

    public String get_subsemesterid() {
        return _subsemesterid;
    }

    public String get_subyear() {
        return _subyear;
    }
}
