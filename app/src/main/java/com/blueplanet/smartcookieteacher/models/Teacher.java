package com.blueplanet.smartcookieteacher.models;

/**
 * model class to represent teacher
 *
 * @author sayali
 */
public class Teacher {

    private int _id = -1;
    private String _tId = null;
    private String _tCompleteName = null;
    private String _tName = null;
    private String _tMiddleName = null;
    private String _tLastName = null;
    private String _tCurrent_School_Name = null;
    private String _tSchool_id = null;
    private int _tSchool_staff_id = -1;
    private String _tDepartment = null;
    private String _tExperience = null;
    private String _tSubject = null;
    private String _tClass = null;
    private String _tQualification = null;
    private String _tAddress = null;
    private String _tCity = null;
    private String _tDOB = null;
    private int _tAge = -1;
    private String _tGender = null;
    private String _tCountry = null;
    private String _tEmail = null;
    private String _tInternal_email = null;
    private String _tPassword = null;
    private String _tDate = null;
    private String _tPC = null;
    private int _tPhone = -1;
    private int _tLandline = -1;
    private int _tUsed_point = -1;
    private int _tBalance_point = -1;
    private int _tState = -1;
    private int _tBalance_blue_pint = -1;
    private int _tUsed_blue_pint = -1;
    private String _tBatchId = null;
    private String _terror_records = null;
    private String _tpermanent_district = null;
    private String _ttemp_address = null;
    private String _tpermanant_village = null;
    private String _tpermanent_taluka = null;
    private int _tpermanent_pincode = -1;
    private String _tDate_of_appointment = null;
    private String _tAppoTypeId = null;
    private String _tEmpTypeId = null;
    private String _tCollageMnemonic = null;
    private String _sendStatus;


    public Teacher(int id, String tId, String tCompleteName, String tName, String tMiddleName, String tLastName,
                   String tCurrent_School_Name, String tSchool_id, int tSchool_staff_id, String tDepartment,
                   String tExperience, String tSubject, String tClass, String tQualification, String tAddress,
                   String tCity, String tDOB, int tAge, String tGender, String tCountry, String tEmail,
                   String tInternal_email, String tPassword, String tDate, String tPC, int tPhone,
                   int tLandline, int tUsed_point, int tBalance_point, int tUsed_blue_pint, String tBatchId, int tState,
                   int tBalance_blue_pint,
                   String terror_records, String sendStatus, String ttemp_address, String tpermanant_village,
                   String tpermanent_taluka, String tpermanent_district, int tpermanent_pincode,
                   String tDate_of_appointment, String tAppoTypeId, String tEmpTypeId, String tCollageMnemonic
    ) {
        _id = id;
        _tId = tId;
        _tCompleteName = tCompleteName;
        _tName = tName;
        _tMiddleName = tMiddleName;
        _tLastName = tLastName;
        _tCurrent_School_Name = tCurrent_School_Name;
        _tSchool_id = tSchool_id;
        _tSchool_staff_id = tSchool_staff_id;
        _tDepartment = tDepartment;
        _tExperience = tExperience;
        _tSubject = tSubject;
        _tClass = tClass;
        _tQualification = tQualification;
        _tAddress = tAddress;
        _tClass = tClass;
        _tDOB = tDOB;
        _tCity = tCity;
        _tAge = tAge;
        _tGender = tGender;
        _tCountry = tCountry;
        _tEmail = tEmail;
        _tInternal_email = tInternal_email;
        _tPassword = tPassword;
        _tDate = tDate;
        _tPC = tPC;
        _tPhone = tPhone;
        _tLandline = tLandline;
        _tUsed_point = tUsed_point;
        _tBalance_blue_pint = tBalance_blue_pint;
        _tUsed_blue_pint = tUsed_blue_pint;
        _tBatchId = tBatchId;
        _terror_records = terror_records;
        _ttemp_address = ttemp_address;
        _tpermanant_village = tpermanant_village;
        _tpermanent_taluka = tpermanent_taluka;
        _tpermanent_district = tpermanent_district;
        _tpermanent_pincode = tpermanent_pincode;
        _tDate_of_appointment = tDate_of_appointment;
        _tAppoTypeId = tAppoTypeId;
        _tEmpTypeId = tEmpTypeId;
        _tCollageMnemonic = tCollageMnemonic;
        _sendStatus = sendStatus;
        _tState = tState;
        _tBalance_point = tBalance_point;

    }

    /**
     * getters
     *
     * @return
     */
    public int getId() {
        return _id;
    }

    public String get_tId() {
        return _tId;
    }

    public String get_tCompleteName() {
        return _tCompleteName;
    }

    public String get_tName() {
        return _tName;
    }

    public String get_tMiddleName() {
        return _tMiddleName;
    }

    public String get_tCurrent_School_Name() {
        return _tCurrent_School_Name;
    }

    public String get_tLastName() {
        return _tLastName;
    }

    public String get_tSchool_id() {
        return _tSchool_id;
    }

    public int get_tSchool_staff_id() {
        return _tSchool_staff_id;
    }

    public String get_tDepartment() {
        return _tDepartment;
    }

    public String get_tExperience() {
        return _tExperience;
    }

    public String get_tSubject() {
        return _tSubject;
    }

    public String get_tClass() {
        return _tClass;
    }

    public String get_tQualification() {
        return _tQualification;
    }

    public String get_tAddress() {
        return _tAddress;
    }

    public String get_tCity() {
        return _tCity;
    }

    public String get_tDOB() {
        return _tDOB;
    }

    public int get_tAge() {
        return _tAge;
    }

    public String get_tGender() {
        return _tGender;
    }

    public String get_tCountry() {
        return _tCountry;
    }

    public String get_tEmail() {
        return _tEmail;
    }

    public String get_tInternal_email() {
        return _tInternal_email;
    }

    public String get_tPassword() {
        return _tPassword;
    }

    public String get_tDate() {
        return _tDate;
    }

    public String get_tPC() {
        return _tPC;
    }

    public int get_tPhone() {
        return _tPhone;
    }

    public int get_tLandline() {
        return _tLandline;
    }

    public int get_tUsed_point() {
        return _tUsed_point;
    }

    public int get_tBalance_point() {
        return _tBalance_point;
    }

    public int get_tState() {
        return _tState;
    }

    public int get_tBalance_blue_pint() {
        return _tBalance_blue_pint;
    }

    public int get_tUsed_blue_pint() {
        return _tUsed_blue_pint;
    }

    public String get_tBatchId() {
        return _tBatchId;
    }

    public String get_terror_records() {
        return _terror_records;
    }

    public String get_tpermanent_district() {
        return _tpermanent_district;
    }

    public String get_ttemp_address() {
        return _ttemp_address;
    }

    public String get_tpermanant_village() {
        return _tpermanant_village;
    }

    public String get_tpermanent_taluka() {
        return _tpermanent_taluka;
    }


    public int get_tpermanent_pincode() {
        return _tpermanent_pincode;
    }

    public String get_tDate_of_appointment() {
        return _tDate_of_appointment;
    }

    public String get_tAppoTypeId() {
        return _tAppoTypeId;
    }

    public String get_tEmpTypeId() {
        return _tEmpTypeId;
    }

    public String get_tCollageMnemonic() {
        return _tCollageMnemonic;
    }

    public String get_sendStatus() {
        return _sendStatus;
    }

}
