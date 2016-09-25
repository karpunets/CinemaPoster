package com.karpunets.cinemaposter.manageUsers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Arthur on 12.09.2016.
 */
public class User {

    private final String name;
    private final int id;

    public User(int id, String name) {
        this.name = name;
        this.id = id;
    }

    protected User(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }


    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }


}
