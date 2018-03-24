package com.blueplanet.smartcookieteacher.DatabaseManager;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to manage database creation and version management.
 *
 * @author dhanashree.ghayal
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartcookie.db";
    private static final int DATABASE_VERSION = 3;
   // private final String _tag = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // creates Scorm table:
        //create userInfo table:
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_USER);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }

        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_REWARD);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_STUDENTLIST);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_TEACHER);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_SUBJECT);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_ACTIVITYLIST);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_BLUELOG);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_GENERATE_COUP_LOG);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }

        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_BUY_COUP_LOG);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_TEACHER_POINT);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }

        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_TEACHER_POINT);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }

        try {
            database.execSQL(SmartTeacherDatabaseMasterTable.CREATE_RECENTLYGENERATEDCOUPON);
        } catch (Exception e) {
            // Log.log ( Log.INFO, "Exception while creating user table" );
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Method is called during an upgrade of the database,
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.USER);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.REWARD);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT);
        db.execSQL("DROP TABLE IF EXISTS " + SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON);

        // create new tables
        onCreate(db);

    }
}