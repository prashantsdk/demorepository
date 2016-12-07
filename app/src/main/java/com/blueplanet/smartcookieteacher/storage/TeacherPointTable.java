package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.User;

/**
 * Created by 1311 on 28-04-2016.
 */
public class TeacherPointTable extends TableOperations implements IPersistence {


    public TeacherPointTable () {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allTeacherPoint = {
            SmartTeacherDatabaseMasterTable.TeacherPoint.GREEN,
            SmartTeacherDatabaseMasterTable.TeacherPoint.WATER,
            SmartTeacherDatabaseMasterTable.TeacherPoint.BLUE,
            SmartTeacherDatabaseMasterTable.TeacherPoint.BROWN };


    @Override
    public ContentValues prepare ( Object obj ) {
        TeacherDashbordPoint point = ( TeacherDashbordPoint) obj;
        ContentValues values = new ContentValues ();

        values.put ( SmartTeacherDatabaseMasterTable.TeacherPoint.GREEN, point.get_greenpoint() );
        values.put ( SmartTeacherDatabaseMasterTable.TeacherPoint.WATER, point.get_waterpoint() );
        values.put ( SmartTeacherDatabaseMasterTable.TeacherPoint.BLUE, point.get_bluepoint() );
        values.put ( SmartTeacherDatabaseMasterTable.TeacherPoint.BROWN, point.get_brownpoint() );

        return values;


    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT, null, contentValues );
        } catch ( Exception e ) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        TeacherDashbordPoint teaPoint = null;
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT, // a.table
                _allTeacherPoint, // b. column names
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
                    teaPoint =
                            new TeacherDashbordPoint( cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),cursor.getInt(3) );
                } while ( cursor.moveToNext () );
            }
        }

        return teaPoint;
    }

    @Override
    public void delete(Object obj) {


            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT, null, null );
        }



    @Override
    public void update(Object object) {

    }


}
