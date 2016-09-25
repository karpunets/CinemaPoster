package com.karpunets.cinemaposter.element;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karpunets.cinemaposter.R;

/**
 * Created by Arthur on 18.09.2016.
 */
public class ActionBarFragment extends Fragment implements View.OnClickListener {

    private TextView nameTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.action_bar, null);

        nameTextView = (TextView)v.findViewById(R.id.name);

        v.findViewById(R.id.homeBtn).setOnClickListener(this);
        v.findViewById(R.id.outBtn).setOnClickListener(this);
        v.findViewById(R.id.searchBtn).setOnClickListener(this);
        v.findViewById(R.id.starBtn).setOnClickListener(this);

        return v;
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.homeBtn:
                intent = new Intent(getActivity(), MainActivity.class);
                break;
            case R.id.outBtn:
                intent = new Intent(getActivity(), LoginActivity.class);
                break;
            case R.id.searchBtn:
                intent = new Intent(getActivity(), SearchActivity.class);
                break;
            case R.id.starBtn:
                intent = new Intent(getActivity(), FavouriteActivity.class);
                break;
            default:
                intent = new Intent(getActivity(), LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
