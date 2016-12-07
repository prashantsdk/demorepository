package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;

import java.util.ArrayList;

/**
 * Created by 1311 on 22-04-2016.
 */
public class GenerateCoupLogTable extends TableOperations implements IPersistence {


    public GenerateCoupLogTable() {
    }

    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allGenPoint = {
            SmartTeacherDatabaseMasterTable.GenCoupon.GEN_POINT,
            SmartTeacherDatabaseMasterTable.GenCoupon.GEN_ID,
            SmartTeacherDatabaseMasterTable.GenCoupon.GEN_STATUS,
            SmartTeacherDatabaseMasterTable.GenCoupon.GEN_ISSHUE,
            SmartTeacherDatabaseMasterTable.GenCoupon.GEN_VALIDITY};

    @Override
    public ContentValues prepare(Object obj) {

        GenerateCouponLog genLog = (GenerateCouponLog) obj;
        ContentValues values = new ContentValues();
        values.put(SmartTeacherDatabaseMasterTable.GenCoupon.GEN_POINT, genLog.get_couponPoint());
        values.put(SmartTeacherDatabaseMasterTable.GenCoupon.GEN_ID, genLog.get_gencoupon_id());
        values.put(SmartTeacherDatabaseMasterTable.GenCoupon.GEN_STATUS, genLog.get_gencoupon_status());
        values.put(SmartTeacherDatabaseMasterTable.GenCoupon.GEN_ISSHUE, genLog.get_gencoupon_issue_date());
        values.put(SmartTeacherDatabaseMasterTable.GenCoupon.GEN_VALIDITY, genLog.get_generate_validity_date());

        return values;
    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare(obj);
            insertRecord(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG, null, contentValues);
        } catch (Exception e) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        GenerateCouponLog genLog = null;
        ArrayList<GenerateCouponLog> list = new ArrayList<>();
        Cursor cursor = getRecords(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG, // a.table
                _allGenPoint, // b. column names
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
                    genLog =
                            new GenerateCouponLog(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                    list.add(genLog);

                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    @Override
    public void delete(Object obj) {
        deleteRecords(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG, null, null);

    }

    @Override
    public void update(Object object) {

    }
}
