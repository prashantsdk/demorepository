package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Sayali on 3/15/2017.
 */
public class DisplayTeacSubjectModel {

    private String _subname = null;
    private String _subcode = null;
    private String _subsemesterid = null;
    private String _subCoursename = null;

    public DisplayTeacSubjectModel(String subname,String subcode,String subsemesterid,String subCoursename){

        _subname=subname;
        _subcode=subcode;
        _subsemesterid=subsemesterid;
        _subCoursename=subCoursename;
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
}
