package com.karpunets.cinemaposter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.pojo.Poster;

/**
 * Created by Arthur on 17.09.2016.
 */
public class Processor {


    public static void insert (Poster poster, String contactPath, Context context) {
        final Uri CONTACT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + contactPath);
        context.getContentResolver().insert(CONTACT_URI, convert(poster));
    }
    public static void insert (Poster[] posters, String contactPath, Context context) {
        for (Poster poster: posters) {
            insert(poster, contactPath, context);
        }
    }
    public static void delete (String contactPath, Context context) {
        final Uri CONTACT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + contactPath);
        context.getContentResolver().delete(CONTACT_URI, null, null);
    }

    public static Poster convert(Cursor cursor) {
        Poster poster = new Poster();
        poster.setTitle(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_TITLE)));
        poster.setYear(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_YEAR)));
        poster.setReleased(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_RELEASED)));
        poster.setRuntime(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_RUNTIME)));
        poster.setGenre(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_GENRE)));
        poster.setDirector(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_DIRECTOR)));
        poster.setWriter(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_WRITER)));
        poster.setActors(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_ACTORS)));
        poster.setPlot(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_PLOT)));
        poster.setLanguage(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_LANGUAGE)));
        poster.setCountry(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_COUNTRY)));
        poster.setAwards(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_AWARDS)));
        poster.setPoster(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_POSTER)));
        poster.setImdbRating(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_RATING)));
        poster.setImdbID(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_IMDB_ID)));
        poster.setType(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_TYPE)));
        return poster;
    }

    public static ContentValues convert(Poster poster) {
        ContentValues values = new ContentValues();
        values.put(MyContentProvider.COLUMN_TITLE, poster.getTitle());
        values.put(MyContentProvider.COLUMN_YEAR, poster.getYear());
        values.put(MyContentProvider.COLUMN_RELEASED, poster.getReleased());
        values.put(MyContentProvider.COLUMN_RUNTIME, poster.getRuntime());
        values.put(MyContentProvider.COLUMN_GENRE, poster.getGenre());
        values.put(MyContentProvider.COLUMN_DIRECTOR, poster.getDirector());
        values.put(MyContentProvider.COLUMN_WRITER, poster.getWriter());
        values.put(MyContentProvider.COLUMN_ACTORS, poster.getActors());
        values.put(MyContentProvider.COLUMN_PLOT, poster.getPlot());
        values.put(MyContentProvider.COLUMN_LANGUAGE, poster.getLanguage());
        values.put(MyContentProvider.COLUMN_COUNTRY, poster.getCountry());
        values.put(MyContentProvider.COLUMN_AWARDS, poster.getAwards());
        values.put(MyContentProvider.COLUMN_POSTER, poster.getPoster());
        values.put(MyContentProvider.COLUMN_RATING, poster.getImdbRating());
        values.put(MyContentProvider.COLUMN_IMDB_ID, poster.getImdbID());
        values.put(MyContentProvider.COLUMN_TYPE, poster.getType());
        return values;
    }


}
