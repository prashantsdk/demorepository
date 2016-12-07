package com.blueplanet.smartcookieteacher.DatabaseManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Base class for all respective tables in persistance storage<br>
 * It consists of all generalized methods for basic database operation.
 */
public abstract class TableOperations {
    private static SQLDatabaseManager _instance;

    public TableOperations () {
        _instance = SQLDatabaseManager.getInstance ();
    }

    /**
     * This function add record to the database,
     * @param tableName Table name in which insert operation need to be perform.
     * @param nullColunmHack Column hack.
     * @param contentValues Object to be inserted to database.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long
            insertRecord ( String tableName, String nullColunmHack, ContentValues contentValues ) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = _instance._openDatabase ();
            result = db.insertOrThrow ( tableName, null, contentValues );
        } catch ( Exception e ) {
        } finally {
            contentValues = null;
            _instance._closeDatabase ();
        }
        return result;
    }

    /**
     * This function add record to the database,
     * @param tableName Table name in which delete operation need to be performed.
     * @param whereClause The WHERE clause to apply when deleting. Passing null will delete all rows.
     * @param whereArgs where clause args.
     * @return Number of records modified, -1 if error occured.
     */
    public long deleteRecords ( String tableName, String whereClause, String[] whereArgs ) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = _instance._openDatabase ();
            result = db.delete ( tableName, whereClause, whereArgs );
        } catch ( Exception e ) {
        } finally {
            _instance._closeDatabase ();
        }
        return result;
    }

    /**
     * This function retrieves records from database table according to column names passed and     *
     * @param table Table name on which query has to be fired.
     * @param columns Array of columns name to return. Passing null will return all columns.
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE
     *            itself). Passing null will return all rows for the given table.
     * @param selectionArgs
     * @param groupBy A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY
     *            itself). Passing null will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor, if row grouping is being used,
     *            formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row
     *            groups to be included, and is required when row grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself).
     *            Passing null will use the default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no
     *            LIMIT clause.
     * @return Cursor for retrieved records.
     */
    public Cursor getRecords ( String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy, String limit ) {
        Cursor mCursor = null;
        SQLiteDatabase db = null;
        try {
            db = _instance._openDatabase ();
            mCursor =
                    db.query ( table, columns, selection, selectionArgs, groupBy, having, orderBy,
                            limit );
        } catch ( Exception e ) {
        } finally {
            _instance._closeDatabase ();
        }
        return mCursor;
    }

    /**
     * This method performs mapping between key/value pair for respective table and returns contentValue object
     * @param object of respective model class
     * @return ContentValues
     */
    public abstract ContentValues prepare ( Object object );

    /**
     * this function updates a record in database
     * @param tableName the table to update in
     * @param contentValues a map from column names to new column values. null is a valid value that will be translated
     *            to NULL
     * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows.
     * @param whereArgs You may include ?s in the where clause, which will be replaced by the values from whereArgs. The
     *            values will be bound as Strings.
     * @return number of rows updated
     */
    public long updateRecords ( String tableName, ContentValues contentValues, String whereClause,
            String[] whereArgs ) {
        long result = -1;
        SQLiteDatabase db = null;
        try {
            db = _instance._openDatabase ();
            result = db.update ( tableName, contentValues, whereClause, whereArgs );
        } catch ( Exception e ) {
        } finally {
            contentValues = null;
            _instance._closeDatabase ();
        }
        return result;
    }
}
