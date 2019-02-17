package org.sopt.angeling.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DongHyun on 2016-01-13.
 */

public class DBManager {

    public static final String SIDO = "sido";
    public static final String GUNGU = "gungu";
    //public static final String DONG = "dong";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     *
     * Database creation sql statement
     */

    /*private static final String DATABASE_CREATE = "create table region (sido text, "
            + "gungu text, dong text primary key);";*/

    private static final String DATABASE_CREATE = "create table region (sido text, "
            + "gungu text);";

    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/data.db";

    private static final String DATABASE_NAME = "ResionSelectData.db";
    private static final String DATABASE_TABLE = "region";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public DBManager(Context ctx) {
        this.mCtx = ctx;
    }

    public DBManager open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /*public long createNote(String sidotxt, String gungutxt, String dongtxt) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SIDO, sidotxt);
        initialValues.put(GUNGU, gungutxt);
        initialValues.put(DONG, dongtxt);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }*/

    public long createNote(String sidotxt, String gungutxt) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SIDO, sidotxt);
        initialValues.put(GUNGU, gungutxt);
        //initialValues.put(DONG, dongtxt);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /*public boolean deleteNote(long rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }*/

    public Cursor fetchAllNotes() {
        //return mDb.query(DATABASE_TABLE, new String[] { SIDO, GUNGU, DONG }, null, null, null, null, null);
        return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public Cursor selectSIDO() {
        return mDb.rawQuery("select distinct sido from region", null);
    }

    public Cursor selectGUNGU(String sido) {
        return mDb.rawQuery("select distinct gungu from region where sido='" + sido + "'", null);
    }

    /*public Cursor selectDONG(String sido, String gungu) {
        return mDb.rawQuery("select distinct dong from region where sido='" + sido + "'" + " and gungu='" + gungu + "'", null);
    }*/

    /*public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { SIDO, GUNGU, DONG }, KEY_ROWID
                + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }*/

    /*public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }*/
}
