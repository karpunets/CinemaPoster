package com.karpunets.cinemaposter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.manageUsers.Manager;
import com.karpunets.cinemaposter.service.ServiceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 15.09.2016.
 */
public final class MyContentProvider extends ContentProvider {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private static final String DB_NAME = "posters";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE_FAVORITE = "favourite";
    private static final String DB_TABLE_CACHE = "cache";
    private static final String DB_TABLE_USERS_FAVORITE = "usersFavorite";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_RELEASED = "released";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_WRITER = "writer";
    public static final String COLUMN_ACTORS = "actors";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_LANGUAGE = "language";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_AWARDS = "awards";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_RATING = "imdbRating";
    public static final String COLUMN_IMDB_ID = "imdbID";
    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_ID_FAVORITE = "idFavourite";
    public static final String COLUMN_ID_USERS = "idUsers";

    private static final String[] COLUMN_ALL = {COLUMN_TITLE, COLUMN_YEAR, COLUMN_RELEASED, COLUMN_RUNTIME, COLUMN_GENRE, COLUMN_DIRECTOR, COLUMN_WRITER, COLUMN_ACTORS, COLUMN_PLOT, COLUMN_LANGUAGE, COLUMN_COUNTRY, COLUMN_AWARDS, COLUMN_POSTER, COLUMN_RATING, COLUMN_IMDB_ID, COLUMN_TYPE};

    public static final String AUTHORITY = "com.cinemaposter.providers.Posters";

    public static final String CONTACT_PATH_CACHE = DB_TABLE_CACHE;
    public static final String CONTACT_PATH_FAVORITE = DB_TABLE_FAVORITE;
    public static final String CONTACT_PATH_SERVER = "server";


    private static final int URI_CACHE = 11;
    private static final int URI_FAVORITE = 21;
    private static final int URI_FAVORITE_ID = 22;
    private static final int URI_SERVER = 31;
    private static final int URI_SERVER_SEARCH = 32;

    public static final String CHECK_ON_FAVOURITE = "checkOnFavourite";


    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_CACHE, URI_CACHE);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_FAVORITE, URI_FAVORITE);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_FAVORITE + "/*", URI_FAVORITE_ID);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_SERVER, URI_SERVER);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH_SERVER + "/*/#", URI_SERVER_SEARCH);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CACHE:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTACT_PATH_CACHE;
            case URI_FAVORITE:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTACT_PATH_FAVORITE;
            case URI_FAVORITE_ID:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTACT_PATH_FAVORITE;
            case URI_SERVER:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTACT_PATH_SERVER;
            case URI_SERVER_SEARCH:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTACT_PATH_SERVER;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String currentTable;

        switch (uriMatcher.match(uri)) {
            case URI_CACHE:
                currentTable = DB_TABLE_CACHE;
                break;
            case URI_FAVORITE:
                currentTable = DB_TABLE_FAVORITE;
                selection = "";
                Cursor cursorUser = db.query(DB_TABLE_USERS_FAVORITE, projection, COLUMN_ID_USERS + " = '" + Manager.getCurrentUser().getId() + "'", selectionArgs, null, null, sortOrder);
                cursorUser.moveToFirst();
                if (cursorUser.getCount() != 0)
                    while (true) {
                        selection += COLUMN_IMDB_ID + " = '" +cursorUser.getString(cursorUser.getColumnIndex(COLUMN_ID_FAVORITE)) + "'";
                        if (cursorUser.moveToNext())
                            selection += " OR ";
                        else
                            break;
                    }
                else
                    selection = COLUMN_IMDB_ID + " = '0'";
                break;
            case URI_SERVER:
                new ServiceHelper(getContext()).startService();
                return null;
            case URI_SERVER_SEARCH:
                List<String> listSegments = uri.getPathSegments();
                new ServiceHelper(getContext(), listSegments.get(listSegments.size() - 2), Integer.parseInt(listSegments.get(listSegments.size() - 1))).startService();
                return null;
            default:
                return null;
        }

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(currentTable, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case URI_CACHE:
                db.insert(DB_TABLE_CACHE, null, contentValues);
                break;
            case URI_FAVORITE:
                if (db.query(DB_TABLE_FAVORITE, null, COLUMN_IMDB_ID + " = '" + contentValues.get(COLUMN_IMDB_ID) + "'", null, null, null, null).getCount() == 0) {
                    db.insert(DB_TABLE_FAVORITE, null, contentValues);
                }
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID_USERS, Manager.getCurrentUser().getId());
                cv.put(COLUMN_ID_FAVORITE, (String) contentValues.get(COLUMN_IMDB_ID));
                db.insert(DB_TABLE_USERS_FAVORITE, null, cv);
                break;
            default:
                return null;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        String currentTable;
        switch (uriMatcher.match(uri)) {
            case URI_CACHE:
                currentTable = DB_TABLE_CACHE;
                break;
            case URI_FAVORITE:
                currentTable = DB_TABLE_FAVORITE;
                break;
            case URI_FAVORITE_ID:
                currentTable = DB_TABLE_FAVORITE;
                String id = uri.getLastPathSegment();
                selection = COLUMN_IMDB_ID + " = '" + id + "'";
                db.delete(DB_TABLE_USERS_FAVORITE, COLUMN_ID_FAVORITE + "= '" + id + "'", null);
                break;
            default:
                return -1;
        }
        int amountDeletePosters = db.delete(currentTable, selection, selectionArgs);
        return amountDeletePosters;

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if (method.equals(CHECK_ON_FAVOURITE)) {
            db = dbHelper.getWritableDatabase();

            Cursor cursor = db.query(DB_TABLE_USERS_FAVORITE, null, COLUMN_ID_USERS + " = '" + Manager.getCurrentUser().getId() + "'", null, null, null, null);

            ArrayList<String> result = new ArrayList<>();
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    result.add(cursor.getString(cursor.getColumnIndex(COLUMN_ID_FAVORITE)));
                } while (cursor.moveToNext());
            }

            Bundle b = new Bundle();
            b.putStringArrayList("array", result);
            return b;
        }
        return null;
    }



    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            StringBuffer createColumn = new StringBuffer();
            for (int i = 0; i < COLUMN_ALL.length; i++)
                createColumn.append(", " + COLUMN_ALL[i] + " text");
            db.execSQL("CREATE TABLE " + DB_TABLE_CACHE + " ( " + COLUMN_ID + " integer primary key autoincrement" + createColumn + ");");
            db.execSQL("CREATE TABLE " + DB_TABLE_FAVORITE + " ( " + COLUMN_ID + " integer primary key autoincrement" + createColumn + ");");
            db.execSQL("CREATE TABLE " + DB_TABLE_USERS_FAVORITE + " ( " + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_ID_USERS + " integer, " + COLUMN_ID_FAVORITE + " text);");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
