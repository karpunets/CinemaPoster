package com.karpunets.cinemaposter.element;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.karpunets.cinemaposter.PosterAdapter;
import com.karpunets.cinemaposter.MyContentProvider;
import com.karpunets.cinemaposter.R;

public class FavouriteActivity extends Activity {

    public static  final String nameActivity = "Favourite";

    private static  final Uri CONTACT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.CONTACT_PATH_FAVORITE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);

        ListFragment listFragment = (ListFragment) getFragmentManager().findFragmentById(R.id.list);
        listFragment.setEmptyText("No data");
        ((ActionBarFragment) getFragmentManager().findFragmentById(R.id.bar)).setName(nameActivity);
        PosterAdapter adapter = new PosterAdapter(this, cursor);
        listFragment.setListAdapter(adapter);
    }
}
