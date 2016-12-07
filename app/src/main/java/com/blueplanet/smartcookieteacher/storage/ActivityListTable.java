package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.TeacherActivity;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-04-2016.
 */
public class ActivityListTable extends TableOperations implements IPersistence {

    public ActivityListTable() {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allActivityList = {
            SmartTeacherDatabaseMasterTable.ActivityList.SC_ID,
            SmartTeacherDatabaseMasterTable.ActivityList.SC_LIST,
            SmartTeacherDatabaseMasterTable.ActivityList.ACTIVITY_TYPE,
    };

    @Override
    public ContentValues prepare(Object obj) {

        TeacherActivity activity = (TeacherActivity) obj;
        ContentValues values = new ContentValues();
        values.put(SmartTeacherDatabaseMasterTable.ActivityList.SC_ID, activity.getActivityId());
        values.put(SmartTeacherDatabaseMasterTable.ActivityList.SC_LIST, activity.getActivityName());
        values.put(SmartTeacherDatabaseMasterTable.ActivityList.ACTIVITY_TYPE, activity.getActivityType());


        return values;
    }

    @Override
    public void save(Object object) {
        try {
            ContentValues contentValues = prepare(object);
            insertRecord(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST, null, contentValues);
        } catch (Exception e) {

        }
    }

    @Override
    public Object load(Object object) {

        String activityType = (String) object;
        TeacherActivity activity = null;
        ArrayList<TeacherActivity> list = new ArrayList<>();
        Cursor cursor = getRecords(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST, // a.table
                _allActivityList, // b. column names
                SmartTeacherDatabaseMasterTable.ActivityList.ACTIVITY_TYPE + " = ? ",  // c. selections
                new String[]{String.valueOf(activityType)}, // d. selections
                // args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // 3. if we got results get the first one

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    // boolean rememberMe = Boolean.parseBoolean(cursor.getString(2));
                    activity =
                            new TeacherActivity(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    list.add(activity);

                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    @Override
    public Object getData() {
        TeacherActivity activity = null;
        ArrayList<TeacherActivity> list = new ArrayList<>();
        Cursor cursor = getRecords(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST, // a.table
                _allActivityList, // b. column names
                null,  // c. selections
                null, // d. selections
                // args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        // 3. if we got results get the first one

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    // boolean rememberMe = Boolean.parseBoolean(cursor.getString(2));
                    activity =
                            new TeacherActivity(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    list.add(activity);

                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    @Override
    public void delete(Object obj) {
        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST, null, null );
    }

    @Override
    public void update(Object object) {

    }


}
