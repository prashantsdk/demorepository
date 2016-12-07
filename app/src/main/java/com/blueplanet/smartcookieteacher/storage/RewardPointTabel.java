package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.User;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by 1311 on 25-03-2016.
 */
public class RewardPointTabel extends TableOperations implements IPersistence {


    public RewardPointTabel () {
    }
    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allRewardPoint = {
            SmartTeacherDatabaseMasterTable.Reward.REWARD_POINT,
            SmartTeacherDatabaseMasterTable.Reward.REWARD_STUD_NAME,
            SmartTeacherDatabaseMasterTable.Reward.REWARD_DATE,
            SmartTeacherDatabaseMasterTable.Reward.REWARD_REASON };

    @Override
    public ContentValues prepare(Object obj) {

        RewardPointLog rewardInfo = ( RewardPointLog) obj;
        ContentValues values = new ContentValues ();
        values.put ( SmartTeacherDatabaseMasterTable.Reward.REWARD_POINT, rewardInfo.get_points() );
        values.put ( SmartTeacherDatabaseMasterTable.Reward.REWARD_STUD_NAME, rewardInfo.get_stuDate() );
        values.put ( SmartTeacherDatabaseMasterTable.Reward.REWARD_DATE, rewardInfo.get_point_date() );
        values.put ( SmartTeacherDatabaseMasterTable.Reward.REWARD_REASON, rewardInfo.get_reason() );

        return values;
    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.REWARD, null, contentValues );
        } catch ( Exception e ) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        RewardPointLog rewardObj = null;
        ArrayList<RewardPointLog> list = new ArrayList<>();
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.REWARD, // a.table
                _allRewardPoint, // b. column names
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
                    rewardObj =
                            new RewardPointLog( cursor.getString ( 0 ), cursor.getString ( 1 ),  cursor.getString ( 2 ), cursor.getString ( 3 ));
                    list.add(rewardObj);

                } while ( cursor.moveToNext () );
            }
        }

        return list;
    }

    @Override
    public void delete(Object obj) {
        if ( obj != null ) {
            String userName = ( String ) obj;

            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.REWARD,
                    SmartTeacherDatabaseMasterTable.Reward.REWARD_STUD_NAME + " = ?" + " COLLATE NOCASE",
                    new String[] { String.valueOf ( userName ) } );
        } else {
            deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.REWARD, null, null );
        }
    }

    @Override
    public void update(Object object) {

    }
}
