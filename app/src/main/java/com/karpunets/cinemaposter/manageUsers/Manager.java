package com.karpunets.cinemaposter.manageUsers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.karpunets.cinemaposter.MyContentProvider;
import com.karpunets.cinemaposter.element.LoginActivity;

import java.util.ArrayList;

/**
 * Created by Arthur on 13.09.2016.
 */
public class Manager {

    private static User currentUser;
    public static ArrayList<String> favourite;
    private DBUsers dbUsers;

    private Cursor users;


    public Manager(Context context){
        dbUsers = new DBUsers(context);
        dbUsers.open();
        users = dbUsers.getUsers();
    }

    public void setCurrentUser(int id, Context context) {
        currentUser = dbUsers.searchUser(id);
        favouriteUpdate(context);
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public void close(){
        dbUsers.close();
    }

    public Cursor getUsers() {
        return users;
    }

    public void deleteUser(int id) {
        dbUsers.deleteUser(id);
        users = dbUsers.getUsers();
    }

    public void addUser(String name){
        dbUsers.addUser(name);
        users = dbUsers.getUsers();
        users.moveToLast();
        currentUser = new User(users.getInt(users.getColumnIndex(DBUsers.COLUMN_ID)), users.getString(users.getColumnIndex(DBUsers.COLUMN_USER_NAME)));
        Log.d(LoginActivity.MY_TAB, currentUser.toString());
    }

    public boolean exist(String currentName) {
        String name;
        users.moveToFirst();
        if (users.getCount() == 0)
            return false;
        do {
            name = users.getString(users.getColumnIndex(DBUsers.COLUMN_USER_NAME));
            if (currentName.equals(name))
                return true;
        } while (users.moveToNext());
        return false;
    }

    public static boolean isFafourite(String imdbID) {
        if (Manager.favourite != null)
            for (String s: Manager.favourite) {
            if (s.equals(imdbID))
                return true;
            }
        return false;
    }

    public static void favouriteUpdate(Context context) {
        Bundle bundle = context.getContentResolver().call(Uri.parse("content://" + MyContentProvider.AUTHORITY), MyContentProvider.CHECK_ON_FAVOURITE, null, null);
        favourite = bundle.getStringArrayList("array");
    }

}
