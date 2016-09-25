package com.karpunets.cinemaposter.element;

import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AbsListView;

import com.karpunets.cinemaposter.PosterAdapter;
import com.karpunets.cinemaposter.MyContentProvider;
import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.service.CinemaPosterService;


public class MainActivity extends Activity implements AbsListView.OnScrollListener{

    public static  final String nameActivity = "Main";

    public static final String BROADCAST_ACTION = "com.karpunets.cinemaposter.servicebackbroadcast";

    private ListFragment listFragment;
    private PosterAdapter adapter;

    private static final Uri CONTACT_URI_CACHE = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_CACHE);
    private static final Uri CONTACT_URI_SERVER = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_SERVER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContentResolver().delete(Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_CACHE), null, null);

        listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list);
        listFragment.getListView().setOnScrollListener(this);
        ((ActionBarFragment) getFragmentManager().findFragmentById(R.id.bar)).setName(nameActivity);

        addNewPoster();
    }

    private void addNewPoster() {

        getContentResolver().query(CONTACT_URI_SERVER, null, null, null, null);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(CinemaPosterService.PARAM_RESULT, CinemaPosterService.EXECUTED_ON_FAILURE)) {
                case CinemaPosterService.EXECUTED_ON_FAILURE:
                    listFragment.setEmptyText("No connection");
                    listFragment.setListAdapter(null);
                    break;
                case CinemaPosterService.EXECUTED_SUCCESSFULLY:
                    canAdd = true;
                    addPosters();
                    break;
            }
            unregisterReceiver(broadcastReceiver);

        }
    };

    private void addPosters() {

        Cursor cursor = getContentResolver().query(CONTACT_URI_CACHE, null, null, null, null);

        if (adapter == null) {
            adapter = new PosterAdapter(this, cursor);
            listFragment.setListAdapter(adapter);
        } else
            adapter.notifyDataSetChanged(cursor);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private boolean canAdd = false;
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount - 2 && canAdd) {
            canAdd = false;
            addNewPoster();
        }
    }

    @Override
    protected void onDestroy() {
        getContentResolver().delete(Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_CACHE), null, null);
        super.onDestroy();
    }
}
