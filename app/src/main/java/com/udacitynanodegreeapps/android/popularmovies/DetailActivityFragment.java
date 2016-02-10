package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        if(intent != null ){
            String title = intent.getStringExtra("title");
            ((TextView) rootView.findViewById(R.id.textView_title)).setText(title);

            String overview = intent.getStringExtra("overview");
            ((TextView) rootView.findViewById(R.id.textView_overview)).setText(overview);

            String release = intent.getStringExtra("release");
            ((TextView) rootView.findViewById(R.id.textView_date)).setText(release);

            Double vote = intent.getDoubleExtra("vote",0);
            ((TextView) rootView.findViewById(R.id.textView_avgvote)).setText(vote.toString());


            String posterPath = intent.getStringExtra("poster");
            ImageView imgView = (ImageView) rootView.findViewById(R.id.detail_imageview);

            String base_uri ="http://image.tmdb.org/t/p/";
            String size = "w185";
            String posterUri = base_uri + size + "/" + posterPath;

            Picasso.with(getContext())
                    .load(posterUri)
                    .placeholder(R.drawable.sample_0)
                    .error(R.drawable.sample_7)
                   // .noFade()
//                    .resize(55,55)
//                    .centerCrop()
                    .fit()
                    .into(imgView);
        }
        return rootView;
    }
}
