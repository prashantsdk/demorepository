package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;

import java.util.ArrayList;

/**
 * Created by 1311 on 22-04-2016.
 */
public class BuyCoupLogTable extends TableOperations implements IPersistence {


    public BuyCoupLogTable() {
    }
    /**
     * keep this array final, because it is initialized at first, and its value should not change
     */
    private final String[] _allBuyLog = {
            SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_NAME,
            SmartTeacherDatabaseMasterTable.BuyCoupLog.BUYIMG,
            SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_POINTS_PER_PRODUCT,
            SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_VALIDITY,
            SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_COU_CODE };

    @Override
    public ContentValues prepare(Object obj) {

        Buy_Coupon_log buyLog = ( Buy_Coupon_log) obj;
        ContentValues values = new ContentValues ();
        values.put ( SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_NAME, buyLog.get_couponLogName() );
        values.put ( SmartTeacherDatabaseMasterTable.BuyCoupLog.BUYIMG, buyLog.get_couLogImage() );
        values.put ( SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_POINTS_PER_PRODUCT, buyLog.get_couLogPointsPerProduct() );
        values.put ( SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_VALIDITY, buyLog.get_couponLogValidity() );
        values.put ( SmartTeacherDatabaseMasterTable.BuyCoupLog.BUY_COU_CODE, buyLog.get_couponLogcode() );

        return values;
    }

    @Override
    public void save(Object obj) {
        try {
            ContentValues contentValues = prepare ( obj );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG, null, contentValues );
        } catch ( Exception e ) {

        }
    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {
        Buy_Coupon_log _buyLog = null;
        ArrayList<Buy_Coupon_log> list = new ArrayList<>();
        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG, // a.table
                _allBuyLog, // b. column names
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
                    _buyLog =
                            new Buy_Coupon_log( cursor.getString ( 0 ), cursor.getString ( 1 ),  cursor.getString ( 2 ), cursor.getString ( 3 ),cursor.getString ( 4 ));
                    list.add(_buyLog);

                } while ( cursor.moveToNext () );
            }
        }


        return list;
    }

    @Override
    public void delete(Object obj) {

        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG, null, null );

    }

    @Override
    public void update(Object object) {

    }
}
