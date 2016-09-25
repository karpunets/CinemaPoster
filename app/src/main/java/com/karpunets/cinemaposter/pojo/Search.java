package com.karpunets.cinemaposter.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arthur on 20.09.2016.
 */
public class Search {

    @SerializedName("Search")
    private Poster[] search;


    public Poster[] getSearch() {
        return search;
    }
    public void setSearch(Poster[] search) {
        this.search = search;
    }

}
