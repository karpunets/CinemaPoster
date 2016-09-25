package com.karpunets.cinemaposter.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.element.MainActivity;
import com.karpunets.cinemaposter.MyContentProvider;
import com.karpunets.cinemaposter.pojo.Poster;
import com.karpunets.cinemaposter.Processor;
import com.karpunets.cinemaposter.element.SearchActivity;
import com.karpunets.cinemaposter.pojo.Search;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CinemaPosterService extends Service {

    private final Context context;

    private final String BASE_URL = "http://www.omdbapi.com";

    public static final int EXECUTED_ON_FAILURE = 5;
    public static final int EXECUTED_NOT_SUCCESSFULLY = 4;
    public static final int EXECUTED_SUCCESSFULLY = 2;
    public static final String PARAM_RESULT = "answer";

    private final static String[] idPosters = {"tt1285016", "tt0816692", "tt1433562", "tt1922777", "tt3704050", "tt1258197", "tt0806203", "tt1130884", "tt2948790", "tt1675434", "tt4178092", "tt0443543", "tt0365929", "tt0209144", "tt0450385", "tt1401152", "tt0119174", "tt0488120", "tt5343494", "tt0408790", "tt0114814", "tt0167404", "tt0884328", "tt0884328", "tt0407887", "tt0137523", "tt1568346", "tt0425210", "tt0453467", "tt0387564", "tt0363988", "tt1726592", "tt0111161", "", "tt0120689", "tt0109830", "tt1375666", "tt0110357", "tt0482571", "tt0133093", "tt0120737", "tt2948356", "tt0112573", "tt0454921"};

    public static final String EXTRA_SEARCH = "search";
    public static final String EXTRA_PAGE = "page";

    private static final int addATime = 5;
    private int requiredAddPosters;
    private int index = 0;

    private APIService service;
    private String currentBroadcastAction;


    private Random random = new Random();

    public CinemaPosterService() {
        context = this;
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        index = 0;
        if ((flags&START_FLAG_RETRY) == START_FLAG_RETRY)
            Log.d(LoginActivity.MY_TAB, "Service restart");

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(APIService.class);


        String search = intent.getStringExtra(EXTRA_SEARCH);
        if (search == null) {
            currentBroadcastAction = MainActivity.BROADCAST_ACTION;
            requiredAddPosters = addATime;
            Call<Poster> call;
            for (int i = 0; i < requiredAddPosters; i++) {
                call = service.queryById(idPosters[random.nextInt(idPosters.length)]);
                call.enqueue(callback);
            }
        } else {
            currentBroadcastAction = SearchActivity.BROADCAST_ACTION;
            Call<Search> call = service.queryBySearch(search, intent.getIntExtra(EXTRA_PAGE, 1));
            call.enqueue(callbackSearch);
        }

        return START_REDELIVER_INTENT;
    }

    private Callback<Poster> callback = new Callback<Poster>() {
        @Override
        public void onResponse(Call<Poster> call, Response<Poster> response) {
            if (response.isSuccess()) {
                Poster poster = response.body();
                Processor.insert(poster, MyContentProvider.CONTACT_PATH_CACHE, context);
                finish();
            } else
                finish(EXECUTED_NOT_SUCCESSFULLY);
        }

        @Override
        public void onFailure(Call<Poster> call, Throwable t) {
            finish(EXECUTED_ON_FAILURE);
        }
    };

    private Callback<Search> callbackSearch = new Callback<Search>() {
        @Override
        public void onResponse(Call<Search> call, Response<Search> response) {
            if (response.isSuccess()) {
                Poster[] posters = response.body().getSearch();
                if (posters != null) {
                    requiredAddPosters = posters.length;
                    for (Poster poster: posters) {
                        Call<Poster> callPoster = service.queryById(poster.getImdbID());
                        callPoster.enqueue(callback);
                    }
                } else
                    finish();
            } else
                finish(EXECUTED_NOT_SUCCESSFULLY);
        }
        @Override
        public void onFailure(Call<Search> call, Throwable t) {
            Log.d(LoginActivity.MY_TAB, "onFailure");
            finish(EXECUTED_ON_FAILURE);
        }
    };

    private void finish() {
        if (++index == requiredAddPosters) {
            Intent intent = new Intent(currentBroadcastAction);
            intent.putExtra(PARAM_RESULT, EXECUTED_SUCCESSFULLY);
            sendBroadcast(intent);
        }
    }

    private void finish(int result) {
        Intent intent = new Intent(currentBroadcastAction);
        intent.putExtra(PARAM_RESULT, result);
        sendBroadcast(intent);
    }
}
