package com.karpunets.cinemaposter.element;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.karpunets.cinemaposter.R;
import com.karpunets.cinemaposter.pojo.Poster;
import com.squareup.picasso.Picasso;

public class PosterActivity extends Activity {

    public static  final String nameActivity = "Poster";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        Intent intent = getIntent();
        Poster poster = intent.getParcelableExtra("poster");

        Log.d(LoginActivity.MY_TAB, poster.getImdbID());
        ((ActionBarFragment) getFragmentManager().findFragmentById(R.id.bar)).setName(nameActivity);

        ((TextView)findViewById(R.id.title)).setText(poster.getTitle());
        Picasso.with(this).load(poster.getPoster()).fit().into((ImageView)findViewById(R.id.img));
        ((TextView)findViewById(R.id.year)).setText("Year: " + poster.getYear());
        ((TextView)findViewById(R.id.country)).setText("Country: " + poster.getCountry());
        ((TextView)findViewById(R.id.director)).setText("Director: " + poster.getDirector());
        ((TextView)findViewById(R.id.writer)).setText("Writer: " + poster.getWriter());
        ((TextView)findViewById(R.id.type)).setText("Type: " + poster.getType());
        ((TextView)findViewById(R.id.genre)).setText("Genre: " + poster.getGenre());
        ((TextView)findViewById(R.id.actors)).setText("Actors: " + poster.getActors());
        ((TextView)findViewById(R.id.awards)).setText("Awards: " + poster.getAwards());
        ((TextView)findViewById(R.id.rating)).setText("Rating: " + poster.getImdbRating());
        ((TextView)findViewById(R.id.language)).setText("Language: " + poster.getLanguage());
        ((TextView)findViewById(R.id.released)).setText("Released: " + poster.getReleased());
        ((TextView)findViewById(R.id.runtime)).setText("Runtime: " + poster.getRuntime());
        ((TextView)findViewById(R.id.plot)).setText(poster.getPlot());


    }
}
