package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.Student;

import java.util.ArrayList;

/**
 * Created by 1311 on 15-04-2016.
 */
public class StudentTabel extends TableOperations implements IPersistence {


    public StudentTabel() {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allStudent = {
            SmartTeacherDatabaseMasterTable.StudentList.ID,
            SmartTeacherDatabaseMasterTable.StudentList.STUD_NAME,
            SmartTeacherDatabaseMasterTable.StudentList.STU_FATHERNAME,
            SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLNAME,
            SmartTeacherDatabaseMasterTable.StudentList.STU_CLASSNAME,
            SmartTeacherDatabaseMasterTable.StudentList.STU_ADDRESS,
            SmartTeacherDatabaseMasterTable.StudentList.STU_GENDER,
            SmartTeacherDatabaseMasterTable.StudentList.STU_DOB,
            SmartTeacherDatabaseMasterTable.StudentList.STU_AGE,
            SmartTeacherDatabaseMasterTable.StudentList.STU_CITY,
            SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOLEMAIL,
            SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLPRN,
            SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLID,
            SmartTeacherDatabaseMasterTable.StudentList.STUD_DATE,
            SmartTeacherDatabaseMasterTable.StudentList.STU_DIV,
            SmartTeacherDatabaseMasterTable.StudentList.STU_HOBBIES,
            SmartTeacherDatabaseMasterTable.StudentList.STU_COUNTRI,
            SmartTeacherDatabaseMasterTable.StudentList.STUD_CLASS_TEACHER_NAME,
            SmartTeacherDatabaseMasterTable.StudentList.STU_IMG,
            SmartTeacherDatabaseMasterTable.StudentList.STU_INPPUTID,
            SmartTeacherDatabaseMasterTable.StudentList.STU_TOTALCOUNT,
            };

    @Override
    public ContentValues prepare(Object obj) {
        Student studentInfo = (Student) obj;
        ContentValues values = new ContentValues();

        values.put(SmartTeacherDatabaseMasterTable.StudentList.ID, studentInfo.get_id());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STUD_NAME, studentInfo.get_stdName());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_FATHERNAME, studentInfo.get_stdFatherName());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLNAME, studentInfo.get_stdSchoolName());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_CLASSNAME, studentInfo.get_stdClass());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_ADDRESS, studentInfo.get_stdAddress());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_GENDER, studentInfo.get_stdGender());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_DOB, studentInfo.get_stdDOB());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_AGE, studentInfo.get_stdAge());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_CITY, studentInfo.get_stdCity());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOLEMAIL, studentInfo.get_stdEmail());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLPRN, studentInfo.get_stdPRN());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_SCHOOLID, studentInfo.get_schoolId());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STUD_DATE, studentInfo.get_stdDate());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_DIV, studentInfo.get_stdDiv());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_HOBBIES, studentInfo.get_stdHobbies());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_COUNTRI, studentInfo.get_stdCountry());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_TOTALCOUNT, studentInfo.getTotalCount());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_IMG, studentInfo.get_stdImageUrl());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STUD_CLASS_TEACHER_NAME, studentInfo.get_schoolId());
        values.put(SmartTeacherDatabaseMasterTable.StudentList.STU_INPPUTID, studentInfo.getInputId());

        return values;


    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare(obj);
            insertRecord(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST, null, contentValues);

        } catch (Exception e) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }


    @Override
    public Object getData() {
        Student stuobj = null;
        ArrayList<Student> list = new ArrayList<>();
        Cursor cursor = getRecords(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST, // a.table
                _allStudent, // b. column names
                null,  // c. selections
                null, // d. selections
                // args
                null, // e. group by
                null, // f. having
                SmartTeacherDatabaseMasterTable.StudentList.NUM, // g. order by
                null); // h. limit

        // 3. if we got results get the first one

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    // boolean rememberMe = Boolean.parseBoolean(cursor.getString(2));
                    stuobj =
                            new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11),
                                    cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17),
                                    cursor.getString(18), cursor.getInt(19), cursor.getInt(20),cursor.getString(21),cursor.getString(22));
                    list.add(stuobj);

                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    @Override
    public void delete(Object obj) {
        if (obj != null) {
            String userName = (String) obj;

            deleteRecords(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST,
                    SmartTeacherDatabaseMasterTable.StudentList.ID + " = ?" + " COLLATE NOCASE",
                    new String[]{String.valueOf(userName)});
        } else {
            deleteRecords(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST, null, null);
        }

    }

    @Override
    public void update(Object object) {

    }


}
