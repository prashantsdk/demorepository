package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;

import com.blueplanet.smartcookieteacher.models.Teacher;


import java.util.ArrayList;

/**
 * Created by 1311 on 06-03-2016.
 */
public class TeacherLoginTabel extends TableOperations implements IPersistence {

    public TeacherLoginTabel() {

    }



    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allTeacher = {
            SmartTeacherDatabaseMasterTable.Teacher.KEY_ID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TCOMPL_Name,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TNAME,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TMIDDLE_NAME,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TLAST_NAME,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TCURRENT_SCHOOL_NAME,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TSCHOOL_ID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TSTAFF_ID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TDEPARTMENT,

            SmartTeacherDatabaseMasterTable.Teacher.KEY_TEXPRIENCE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TSUBJECT,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_CLASS,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TQULIFICATION,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TADDRESS,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TCITY,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TDOB,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TAGE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_GENDER,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TCOUNTRY,

            SmartTeacherDatabaseMasterTable.Teacher.KEY_TEMAIL,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TINTERNAL_EMAIL,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TPASSWORD,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_DATE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_PC,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_PHONE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TLANDLINE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_BALANCE_POINT,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TUSED_POINT,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TSTATE,

            SmartTeacherDatabaseMasterTable.Teacher.KEY_BALANCE_BLUE_POINT,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_USED_BLUE_POINT,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_BATCH_ID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_ERROR_RECORDS,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_SEND_UNSEND_STATUS,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_TEMP_ADDRESS,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_VILLAGE,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_TALUKA,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_DISTRICT
            , SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_PINCODE,

            SmartTeacherDatabaseMasterTable.Teacher.KEY_DOT_OF_APPOINTMENT,
            SmartTeacherDatabaseMasterTable.Teacher.KEYAPPOINTMENT_TYPE_PID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_EMP_TYPE_PID,
            SmartTeacherDatabaseMasterTable.Teacher.KEY_COLLAGE_MNEMONIC};


    @Override
    public ContentValues prepare(Object obj) {
        Teacher teacherInfo = ( Teacher) obj;
        ContentValues values = new ContentValues ();

        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_ID, teacherInfo.getId() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TID, teacherInfo.get_tId() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TCOMPL_Name, teacherInfo.get_tCompleteName());
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TNAME, teacherInfo.get_tName() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TMIDDLE_NAME, teacherInfo.get_tMiddleName() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TLAST_NAME, teacherInfo.get_tLastName() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TCURRENT_SCHOOL_NAME, teacherInfo.get_tCurrent_School_Name() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TSCHOOL_ID, teacherInfo.get_tSchool_id() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TSTAFF_ID, teacherInfo.get_tSchool_staff_id() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TDEPARTMENT, teacherInfo.get_tDepartment() );

        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TEXPRIENCE, teacherInfo.get_tExperience() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TSUBJECT, teacherInfo.get_tSubject() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_CLASS, teacherInfo.get_tClass() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TQULIFICATION, teacherInfo.get_tQualification() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TADDRESS, teacherInfo.get_tAddress() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TCITY, teacherInfo.get_tCity() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TDOB, teacherInfo.get_tDOB() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TAGE, teacherInfo.get_tAge() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_GENDER, teacherInfo.get_tGender() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TCOUNTRY, teacherInfo.get_tCountry() );

        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TEMAIL, teacherInfo.get_tEmail() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TINTERNAL_EMAIL, teacherInfo.get_tInternal_email() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TPASSWORD, teacherInfo.get_tPassword() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_DATE, teacherInfo.get_tDate() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PC, teacherInfo.get_tPC() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PHONE, teacherInfo.get_tPhone() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TLANDLINE, teacherInfo.get_tLandline() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_BALANCE_POINT, teacherInfo.get_tBalance_point() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TUSED_POINT, teacherInfo.get_tUsed_point() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TSTATE, teacherInfo.get_tState() );

        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_BALANCE_BLUE_POINT, teacherInfo.get_tBalance_blue_pint() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_USED_BLUE_POINT, teacherInfo.get_tUsed_blue_pint() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_BATCH_ID, teacherInfo.get_tBatchId() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_ERROR_RECORDS, teacherInfo.get_terror_records() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_SEND_UNSEND_STATUS, teacherInfo.get_sendStatus() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_TEMP_ADDRESS, teacherInfo.get_ttemp_address() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_VILLAGE, teacherInfo.get_tpermanant_village() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_TALUKA, teacherInfo.get_tpermanent_taluka() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_DISTRICT, teacherInfo.get_tpermanent_district() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_PERMANENT_PINCODE, teacherInfo.get_tpermanent_pincode() );

        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_DOT_OF_APPOINTMENT, teacherInfo.get_tDate_of_appointment() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEYAPPOINTMENT_TYPE_PID, teacherInfo.get_tAppoTypeId() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_EMP_TYPE_PID, teacherInfo.get_tEmpTypeId() );
        values.put ( SmartTeacherDatabaseMasterTable.Teacher.KEY_COLLAGE_MNEMONIC, teacherInfo.get_tCollageMnemonic() );

        return values;
    }

    @Override
    public void save(Object object) {


        try {
            ContentValues conValues=prepare(object);
            insertRecord(SmartTeacherDatabaseMasterTable.Tables.TEACHER,null,conValues);
        }catch (Exception e){

        }
    }

    @Override
    public Object load(Object object) {

        return null;
    }

    @Override
    public Object getData() {

        Teacher teacher = null;
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.TEACHER, // a.table
                _allTeacher, // b. column names
                null,  // c. selections
                null, // d. selections
                // args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null ); // h. limit

        // 3. if we got results get the first one

        if ( cursor != null && cursor.getCount () > 0 ) {
            if ( cursor.moveToFirst () ) {
                do {

                    // boolean rememberMe = Boolean.parseBoolean(cursor.getString(2));
                  teacher =
                            new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                                    cursor.getString(7), cursor.getInt(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getInt(17), cursor.getString(18),
                                    cursor.getString(19), cursor.getString(20), cursor.getString(21), cursor.getString(22), cursor.getString(23), cursor.getString(24), cursor.getString(25), cursor.getInt(26), cursor.getInt(27), cursor.getInt(28), cursor.getInt(29), cursor.getString(30),cursor.getInt(31),
                                    cursor.getInt(32),cursor.getString(33),cursor.getString(34),cursor.getString(35),cursor.getString(36),cursor.getString(37),cursor.getString(38),cursor.getInt(39),cursor.getString(40),cursor.getString(41),cursor.getString(42),cursor.getString(43));

                } while ( cursor.moveToNext () );
            }
        }

        return teacher;
    }

    @Override
    public void delete(Object obj) {

        if ( obj != null ) {
            String userName = ( String ) obj;

            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.TEACHER,
                    SmartTeacherDatabaseMasterTable.Teacher.KEY_TID + " = ?" + " COLLATE NOCASE",
                    new String[] { String.valueOf ( userName ) } );
        } else {
            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.TEACHER, null, null );
        }

    }

    @Override
    public void update(Object object) {

    }
}
