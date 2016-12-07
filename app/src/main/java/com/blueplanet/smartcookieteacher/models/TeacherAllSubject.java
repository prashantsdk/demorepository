package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 16-09-2016.
 */
public class TeacherAllSubject  {


    private int _tsubjectID = -1;
    private String _tSchoolId = null;
    private String _tSubjectCode = null;
    private String _tsubjectName = null;
    private String _tDivisionID = null;
    private String _tSemesterID = null;
    private String _tBranchID = null;
    private String _tDepartmentID = null;
    private String _tCoursLevel = null;

    /**
     * constructor
     *
     * @param tsubjectID
     * @param tSchoolId
     * @param tSubjectCode
     * @param tsubjectName
     * @param tDivisionID
     * @param tSemesterID
     * @param tBranchID
     * @param tDepartmentID
     * @param tCoursLevel
     */
    public TeacherAllSubject(int tsubjectID, String tSchoolId, String tSubjectCode, String tsubjectName,
                          String tDivisionID, String tSemesterID, String tBranchID, String tDepartmentID,
                          String tCoursLevel) {


        _tsubjectID = tsubjectID;
        _tSchoolId = tSchoolId;
        _tSubjectCode = tSubjectCode;
        _tsubjectName = tsubjectName;
        _tDivisionID = tDivisionID;
        _tSemesterID = tSemesterID;
        _tBranchID = tBranchID;
        _tDepartmentID = tDepartmentID;
        _tCoursLevel = tCoursLevel;


    }

    public int get_tsubjectID() {
        return _tsubjectID;
    }

    public String get_tSchoolId() {
        return _tSchoolId;
    }

    public String get_tSubjectCode() {
        return _tSubjectCode;
    }

    public String get_tsubjectName() {
        return _tsubjectName;
    }

    public String get_tDivisionID() {
        return _tDivisionID;
    }

    public String get_tSemesterID() {
        return _tSemesterID;
    }

    public String get_tBranchID() {
        return _tBranchID;
    }

    public String get_tDepartmentID() {
        return _tDepartmentID;
    }

    public String get_tCoursLevel() {
        return _tCoursLevel;
    }
}
