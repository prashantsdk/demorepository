package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;

import java.util.ArrayList;

/**
 * Created by 1311 on 17-04-2016.
 */
public class TeacherSubjectTabel extends TableOperations implements IPersistence {

    private final String _TAG = this.getClass().getSimpleName();

    public TeacherSubjectTabel () {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allTeacherSub = {
            SmartTeacherDatabaseMasterTable.Subject.SUBJECT_ID,
            SmartTeacherDatabaseMasterTable.Subject.SCHOOL_ID,
            SmartTeacherDatabaseMasterTable.Subject.SUBJECT_CODE,
            SmartTeacherDatabaseMasterTable.Subject.SUBJECT_NAME,
            SmartTeacherDatabaseMasterTable.Subject.DIVISION_ID,
            SmartTeacherDatabaseMasterTable.Subject.SEMESTER_ID,
            SmartTeacherDatabaseMasterTable.Subject.BRANCHES_ID,
            SmartTeacherDatabaseMasterTable.Subject.DEPARTMENT_ID,
            SmartTeacherDatabaseMasterTable.Subject.COURSE_LEVEL};

    @Override
    public ContentValues prepare(Object obj) {

        TeacherSubject subject = ( TeacherSubject) obj;
        ContentValues values = new ContentValues ();
        values.put ( SmartTeacherDatabaseMasterTable.Subject.SUBJECT_ID, subject.get_tsubjectID() );
        values.put ( SmartTeacherDatabaseMasterTable.Subject.SCHOOL_ID, subject.get_tSchoolId());
        values.put ( SmartTeacherDatabaseMasterTable.Subject.SUBJECT_CODE, subject.get_tSubjectCode());
        values.put (SmartTeacherDatabaseMasterTable.Subject.SUBJECT_NAME, subject.get_tsubjectName());

        values.put ( SmartTeacherDatabaseMasterTable.Subject.DIVISION_ID, subject.get_tDivisionID() );
        values.put ( SmartTeacherDatabaseMasterTable.Subject.SEMESTER_ID, subject.get_tSemesterID() );
        values.put ( SmartTeacherDatabaseMasterTable.Subject.BRANCHES_ID, subject.get_tBranchID() );
        values.put ( SmartTeacherDatabaseMasterTable.Subject.DEPARTMENT_ID, subject.get_tDepartmentID() );
        values.put ( SmartTeacherDatabaseMasterTable.Subject.COURSE_LEVEL, subject.get_tCoursLevel() );

        return values;
    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.SUBJECT, null, contentValues );
        } catch ( Exception e ) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        TeacherSubject subj = null;
        ArrayList<TeacherSubject> list = new ArrayList<>();
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.SUBJECT, // a.table
                _allTeacherSub, // b. column names
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
                    subj =
                            new TeacherSubject( cursor.getInt(0), cursor.getString ( 1 ),  cursor.getString ( 2 ), cursor.getString ( 3 ), cursor.getString ( 4 ),
                                    cursor.getString ( 5 ),cursor.getString ( 6 ),cursor.getString ( 7 ),cursor.getString ( 8 )    );
                    list.add(subj);
                    Log.i(_TAG,"subList db"+list);
                } while ( cursor.moveToNext () );
            }
        }

        return list;
    }

    @Override
    public void delete(Object obj) {
        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.SUBJECT, null, null );
    }

    @Override
    public void update(Object object) {

    }
}
