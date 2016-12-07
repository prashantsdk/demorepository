package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.BlueLog;


import java.util.ArrayList;

/**
 * Created by 1311 on 21-04-2016.
 */
public class BluePointTable extends TableOperations implements IPersistence {
    public BluePointTable () {
    }
    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allBlueLogTabel = {
            SmartTeacherDatabaseMasterTable.BlueLog.STUD_POINT,
            SmartTeacherDatabaseMasterTable.BlueLog.STUD_DATE,
            SmartTeacherDatabaseMasterTable.BlueLog.STDREASON,
            SmartTeacherDatabaseMasterTable.BlueLog.STD_COMPNAME,
          };

    @Override
    public ContentValues prepare(Object obj) {

        BlueLog bluelog = ( BlueLog) obj;
        ContentValues values = new ContentValues ();
        values.put ( SmartTeacherDatabaseMasterTable.BlueLog.STUD_POINT, bluelog.get_point() );
        values.put ( SmartTeacherDatabaseMasterTable.BlueLog.STUD_DATE, bluelog.get_point_date() );
        values.put ( SmartTeacherDatabaseMasterTable.BlueLog.STDREASON, bluelog.get_reason() );
        values.put ( SmartTeacherDatabaseMasterTable.BlueLog.STD_COMPNAME, bluelog.get_compName() );



        return values;
    }
    @Override
    public void save(Object obj) {

        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG, null, contentValues );
        } catch ( Exception e ) {

        }

    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        BlueLog log = null;
        ArrayList<BlueLog> list = new ArrayList<>();
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG, // a.table
                _allBlueLogTabel, // b. column names
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
                    log =
                            new BlueLog( cursor.getString ( 0 ), cursor.getString ( 1 ),  cursor.getString ( 2 ), cursor.getString ( 3 ));
                    list.add(log);

                } while ( cursor.moveToNext () );
            }
        }

        return list;
    }

    @Override
    public void delete(Object object) {
        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG, null, null );
    }

    @Override
    public void update(Object object) {

    }


}
