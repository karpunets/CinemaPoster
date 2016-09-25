package com.karpunets.cinemaposter.service;

import com.karpunets.cinemaposter.pojo.Poster;
import com.karpunets.cinemaposter.pojo.Search;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Arthur on 17.09.2016.
 */
public interface APIService {

    @POST("/?")
    Call<Poster> queryById(@Query("i") String id);

    @POST("/?")
    Call<Search> queryBySearch(@Query("s") String search, @Query("page") int page);

}
