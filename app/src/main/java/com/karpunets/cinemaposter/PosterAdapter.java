package com.karpunets.cinemaposter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.karpunets.cinemaposter.element.LoginActivity;
import com.karpunets.cinemaposter.element.PosterActivity;
import com.karpunets.cinemaposter.manageUsers.Manager;
import com.karpunets.cinemaposter.pojo.Poster;
import com.squareup.picasso.Picasso;

/**
 * Created by Arthur on 20.09.2016.
 */
public class PosterAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context context;
    private Cursor cursor;
    private final LayoutInflater lInflater;


    public PosterAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void notifyDataSetChanged(Cursor cursor) {
        this.cursor = cursor;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return new Object();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View item, ViewGroup viewGroup) {
        if (item == null) {
            item = lInflater.inflate(R.layout.item_poster, viewGroup, false);
        }
        cursor.moveToPosition(position);

        Poster poster = Processor.convert(cursor);

        item.setTag(poster);
        item.setOnClickListener(this);
        ImageView img = (ImageView) item.findViewById(R.id.img);
        Picasso.with(context).load(poster.getPoster()).fit().into(img);

        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(poster.getTitle());
        TextView genre = (TextView) item.findViewById(R.id.genre);
        genre.setText("Genre: " + poster.getGenre());
        TextView year = (TextView) item.findViewById(R.id.year);
        year.setText("Year: " + poster.getYear());
        TextView country = (TextView) item.findViewById(R.id.country);
        country.setText("Country: " + poster.getCountry());
        TextView runtime = (TextView) item.findViewById(R.id.runtime);
        runtime.setText("Runtime: " + poster.getRuntime());

        TextView rating = (TextView) item.findViewById(R.id.rating);
        if (poster.getImdbRating() != null)
            rating.setText(poster.getImdbRating().equals("N/A") ? "" : poster.getImdbRating());

        View popularBtn = item.findViewById(R.id.popularBtn);
        popularBtn.setTag(false);
        popularBtn.setOnClickListener(this);

        ImageButton star = (ImageButton) item.findViewById(R.id.star);
        if (Manager.isFafourite(poster.getImdbID()))
            star.setImageResource(R.mipmap.star_on);
        else
            star.setImageResource(R.mipmap.star_off);
        star.setTag(poster);


        return item;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popularBtn:
                ImageButton star = (ImageButton) view.findViewById(R.id.star);
                String imdbId = ((Poster)star.getTag()).getImdbID();

                int src;
                if (Manager.isFafourite(imdbId)) {
                    src = R.mipmap.star_off;
                    Processor.delete(MyContentProvider.CONTACT_PATH_FAVORITE + "/" + imdbId, context);
                } else {
                    Processor.insert((Poster)star.getTag(), MyContentProvider.CONTACT_PATH_FAVORITE, context);
                    src = R.mipmap.star_on;
                }
                Manager.favouriteUpdate(context);

                star.setImageResource(src);
                star.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_increase));
                break;
            default:
                Intent intent = new Intent(context, PosterActivity.class);
                intent.putExtra("poster", (Poster) view.getTag());
                context.startActivity(intent);
                break;
        }
    }
}
