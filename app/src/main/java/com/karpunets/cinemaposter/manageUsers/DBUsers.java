package com.karpunets.cinemaposter.manageUsers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arthur on 13.09.2016.
 */
public class DBUsers {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "cinemaPosterDB";
    private static final String DB_TABLE = "users";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_NAME = "userName";

    private DBHelper helper;
    private final Context context;
    private SQLiteDatabase database;

    DBUsers (Context mCtx) {
        this.context = mCtx;
    }

    void open() {
        helper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        database = helper.getWritableDatabase();
    }

    Cursor getUsers() {
        return database.query(DB_TABLE, null, null, null, null, null, null);
    }

    void deleteUser(int id) {
        database.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    User searchUser(int id) {
        Cursor cursor =  database.query(DB_TABLE, null, COLUMN_ID + " = " + id , null, null, null, null);
        cursor.moveToFirst();
        return new User(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
    }

    void addUser(String name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        database.insert(DB_TABLE, null, cv);
    }

    void close() {
        if (helper != null)
            helper.close();
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB_TABLE + " ( " + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_USER_NAME + " text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
