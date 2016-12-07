package com.blueplanet.smartcookieteacher.DatabaseManager;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class implements functions to create,initialize and close database.
 * @author dhanashree.ghayal
 */
public class SQLDatabaseManager {

    private static SQLiteDatabase database;
    private AtomicInteger mOpenCounter = new AtomicInteger ();
    private static Context _context;
    private static DatabaseHelper dbHelper;
    private final String _tag = "SQLDatabaseManager";
    private static SQLDatabaseManager mInstance = null;

    /**
     * This is constructor initializes database
     */
    private SQLDatabaseManager () {

        Log.i(_tag, "SQLDatabaseManager constructor ");
    }

    /**
     * This function initializes database, performs following tasks,<br>
     * 1. Opens database. <br>
     * 2. Maintain its handler for further use.
     * @param ctx Context is required to open database
     */
    private void _initialize ( Context ctx ) {

        _context = ctx;
        try {
            dbHelper = new DatabaseHelper ( _context );
            _openDatabase ();
        } catch ( SQLException ex ) {
            Log.i ( _tag,
                    "SQLDatabaseManager-Initialize-SQLException Exception while opening database" );
        }
    }

    /**
     * This function opens database in write mode and maintain its handle globally for further use.
     * @return {@link SQLiteDatabase} object created while opening database in write mode
     * @throws SQLException
     */
    public SQLiteDatabase _openDatabase () throws SQLException {
        if ( mOpenCounter.incrementAndGet () == 1 ) {
            // Opening new database
            database = dbHelper.getWritableDatabase ();
            if ( !database.isReadOnly () ) {
                database.execSQL ( "PRAGMA foreign_keys = ON;" );
            }
        }
        return database;
    }

    /**
     * This function closes database and its helper
     */
    public void _closeDatabase () {
        if ( mOpenCounter.decrementAndGet () == 0 ) {
            if ( database != null ) {
                // Closing database
                database.close ();
                dbHelper.close ();
            }
        }
    }

    /**
     * This method is implemented to make SQLDatabaseManager class singleton.
     * @return SQLDatabaseManager object
     */
    public static SQLDatabaseManager getInstance () {
        synchronized ( SQLDatabaseManager.class ) {
            if ( mInstance == null ) {
                mInstance = new SQLDatabaseManager ();
            }
        }
        return mInstance;
    }

    public void setApplicationContext ( Context context ) {
        _context = context;
        _initialize ( _context );
    }

}