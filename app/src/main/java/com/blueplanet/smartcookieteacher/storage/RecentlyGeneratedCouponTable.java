package com.blueplanet.smartcookieteacher.storage;

import android.content.ContentValues;
import android.database.Cursor;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.DatabaseManager.TableOperations;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;

import java.util.ArrayList;

/**
 * Created by prashantj on 3/5/2018.
 */

public class RecentlyGeneratedCouponTable extends TableOperations implements IPersistence {


    public RecentlyGeneratedCouponTable() {

    }


    private final String[] recenetlyGenerateAllCoupon = {
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_ID,
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_POINTS,
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_ISSUE_DATE,
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_VALIDITY_DATE,
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_BALANCE_POINTS,
            SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_USED_POINTS};



    @Override
    public ContentValues prepare(Object object) {
       GenerateCoupon generateCoupon = (GenerateCoupon) object;

        ContentValues values = new ContentValues ();
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_ID, generateCoupon.get_couID() );
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_POINTS, generateCoupon.get_couPoint() );
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_ISSUE_DATE, generateCoupon.get_couIssueDate() );
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_VALIDITY_DATE, generateCoupon.get_couValidityDate() );
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_BALANCE_POINTS, generateCoupon.get_couBalancePoint() );
        values.put ( SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_USED_POINTS, generateCoupon.getBalancePointType() );



        return values;
    }


    @Override
    public void save(Object object) {

        try {
            ContentValues contentValues = prepare ( object );
            insertRecord ( SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON, null, contentValues );
        } catch ( Exception e ) {

        }


    }

    @Override
    public Object load(Object object) {
        return null;
    }

    @Override
    public Object getData() {

        GenerateCoupon log = null;
        ArrayList<GenerateCoupon> list = new ArrayList<>();

        Cursor cursor = getRecords ( SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON, // a.table
                recenetlyGenerateAllCoupon, // b. column names
                null,  // c. selections
                null, // d. selections
                // args
                null, // e. group by
                null, // f. having
                recenetlyGenerateAllCoupon[0] +" DESC ", // g. order by
                 null); // h. limit

        // 3. if we got results get the first one

        if ( cursor != null && cursor.getCount () > 0 ) {
            if ( cursor.moveToFirst () ) {
                do {

                    // boolean rememberMe = Boolean.parseBoolean(cursor.getString(2));
                    log =
                            new GenerateCoupon( cursor.getString ( 0 ), cursor.getString ( 1 ),  cursor.getString ( 2 ), cursor.getString ( 3 ),cursor.getString ( 4),cursor.getString(5));
                    list.add(log);

                } while ( cursor.moveToNext () );
            }
        }

        return list;
    }

    @Override
    public void delete(Object object) {
        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON, null, null );

    }


    public void deleteOne(Object object) {
        String couponId[] = (String[]) object;
        deleteRecords ( SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON, SmartTeacherDatabaseMasterTable.RecentlyGenerateCoupon.COUPON_ID, couponId );

    }

    @Override
    public void update(Object object) {

    }


}
