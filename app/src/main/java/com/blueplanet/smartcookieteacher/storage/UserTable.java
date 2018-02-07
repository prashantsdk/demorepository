package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;
import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.User;

import org.apache.http.cookie.SM;

import java.util.ArrayList;

public class UserTable extends TableOperations implements IPersistence {

    public UserTable () {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allUsers = {
            SmartTeacherDatabaseMasterTable.User.USER_NAME,
            SmartTeacherDatabaseMasterTable.User.USER_PASSWORD,
            SmartTeacherDatabaseMasterTable.User.REMEMBER_ME,
            SmartTeacherDatabaseMasterTable.User.PRN_VALUE

           /* SmartTeacherDatabaseMasterTable.User.USER_EMAIL_ID,
            SmartTeacherDatabaseMasterTable.User.USER_MOBILE_NO,
            SmartTeacherDatabaseMasterTable.User.USER_MEMBER_ID
            */
            };

    @Override
    public ContentValues prepare ( Object obj ) {
        User userInfo = ( User) obj;
        ContentValues values = new ContentValues ();

        values.put ( SmartTeacherDatabaseMasterTable.User.USER_NAME, userInfo.getUserName () );
        values.put ( SmartTeacherDatabaseMasterTable.User.USER_PASSWORD, userInfo.getPassword1() );
        values.put ( SmartTeacherDatabaseMasterTable.User.REMEMBER_ME, userInfo.isRememberMe() );
        values.put(SmartTeacherDatabaseMasterTable.User.PRN_VALUE,userInfo.get_prn());

       /* values.put(SmartTeacherDatabaseMasterTable.User.USER_EMAIL_ID,userInfo.getUserEmailId());
        values.put(SmartTeacherDatabaseMasterTable.User.USER_MOBILE_NO,userInfo.getUserMobileNo());
        values.put(SmartTeacherDatabaseMasterTable.User.USER_MEMBER_ID,userInfo.getMemberId());
*/
        return values;


    }

    @Override
    public Object load ( Object obj ) {

        return null;
    }

    @Override
    public Object getData() {

        User userObj = null;
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.USER, // a.table
                _allUsers, // b. column names
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
                    userObj =
                            new User( cursor.getString ( 0 ), cursor.getString ( 1 ), cursor.getString(2) ,cursor.getString ( 3 ));
                } while ( cursor.moveToNext () );
            }
        }

        return userObj;
    }

    @Override
    public void save ( Object obj ) {
        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.USER, null, contentValues );
        } catch ( Exception e ) {

        }
    }

    @Override
    public void delete ( Object obj ) {

        if ( obj != null ) {
            String userName = ( String ) obj;

            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.USER,
                    SmartTeacherDatabaseMasterTable.User.USER_NAME + " = ?" + " COLLATE NOCASE",
                    new String[] { String.valueOf ( userName ) } );
        } else {
            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.USER, null, null );
        }

    }

    @Override
    public void update ( Object obj ) {


    }

}
