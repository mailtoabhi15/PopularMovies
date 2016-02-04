package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

        //Now we will handle the Intent we sent from MainActivityFragment here
        Intent intent = getActivity().getIntent();
        if(intent != null & intent.hasExtra(Intent.EXTRA_TEXT)){
            Integer pos = intent.getIntExtra(Intent.EXTRA_TEXT,0);
            ImageView imgView = (ImageView) rootView.findViewById(R.id.detail_imageview);
            imgView.setImageResource(pos);
        }
        return rootView;
    }
}
