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
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;

import com.karpunets.cinemaposter.MyContentProvider;
import com.karpunets.cinemaposter.PosterAdapter;
import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.service.CinemaPosterService;

public class SearchActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {

    public static  final String nameActivity = "Search";

    public static final String BROADCAST_ACTION = "com.karpunets.cinemaposter.servicebackbroadcast.search";

    private static final Uri CONTACT_URI_CACHE = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_CACHE);

    private PosterAdapter adapter;
    private ListFragment listFragment;
    private EditText editText;

    private static int page = 1;
    private static String search = "";

    private boolean canAdd;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = (EditText) findViewById(R.id.searchEditText);
        editText.setOnClickListener(this);
        findViewById(R.id.sentQuery).setOnClickListener(this);

        listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list);
        listFragment.setEmptyText("");
        listFragment.setListAdapter(null);
        listFragment.getListView().setOnScrollListener(this);

        ((ActionBarFragment) getFragmentManager().findFragmentById(R.id.bar)).setName(nameActivity);

        cleanCache();
    }

    private void cleanCache() {
        getContentResolver().delete(Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_CACHE), null, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sentQuery:
                listFragment.setListShown(false);
                update = true;
                String text = editText.getText().toString();
                if (!search.equals(text)) {
                    page = 1;
                    search = text;
                    cleanCache();
                }
                addNewPoster();
                break;

        }

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(CinemaPosterService.PARAM_RESULT, CinemaPosterService.EXECUTED_ON_FAILURE)) {
                case CinemaPosterService.EXECUTED_SUCCESSFULLY:
                    addPosters();
                    break;
                case CinemaPosterService.EXECUTED_NOT_SUCCESSFULLY:
                    listFragment.setEmptyText("Not found");
                    listFragment.setListAdapter(null);
                    break;
                case CinemaPosterService.EXECUTED_ON_FAILURE:
                    listFragment.setEmptyText("No connection");
                    listFragment.setListAdapter(null);
                    break;
            }
            unregisterReceiver(broadcastReceiver);

        }
    };

    private void addPosters() {

        Cursor cursor = getContentResolver().query(CONTACT_URI_CACHE, null, null, null, null);

        if (adapter == null) {
            adapter = new PosterAdapter(this, cursor);
        } else
            adapter.notifyDataSetChanged(cursor);
        if (update) {
            update = false;
            listFragment.setListShown(true);
            listFragment.setListAdapter(adapter);
        }
        canAdd = true;
    }

    private void addNewPoster() {
        Uri CONTACT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_SERVER + "/" + editText.getText() + "/" + page++);
        getContentResolver().query(CONTACT_URI, null, null, null, null);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        cleanCache();
        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount - 2 && canAdd) {
            canAdd = false;
            addNewPoster();
        }
    }
}
