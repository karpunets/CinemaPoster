package com.karpunets.cinemaposter.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.karpunets.cinemaposter.element.LoginActivity;

/**
 * Created by Arthur on 15.09.2016.
 */
public class ServiceHelper {

    private final Context context;
    private String search;
    private int page;


    public ServiceHelper (Context context) {
        this.context = context;
    }

    public ServiceHelper (Context context, String search, int page) {
        this.context = context;
        this.search = search;
        this.page = page;
    }

    public void startService() {

        Intent intent = new Intent(context, CinemaPosterService.class);
        if (search != null) {
            intent.putExtra(CinemaPosterService.EXTRA_SEARCH, search);
            intent.putExtra(CinemaPosterService.EXTRA_PAGE, page);
        }
        context.startService(intent);
    }

}
