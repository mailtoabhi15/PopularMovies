package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.content.SharedPreferences;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Now we will handle the Intent we sent from MainActivityFragment here
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movielist")) {

            MyMovie movieList = intent.getParcelableExtra("movielist");

            ((TextView) rootView.findViewById(R.id.textView_title)).setText(movieList.title);


            TextView overView = (TextView) rootView.findViewById(R.id.textView_overview);
            overView.setText(movieList.overview);


            ((TextView) rootView.findViewById(R.id.textView_date)).setText("Release Date: " + movieList.releaseDate);

            ((TextView) rootView.findViewById(R.id.textView_avgvote)).setText("Avg. Vote: " + movieList.voteAvg);


            ImageView imgView = (ImageView) rootView.findViewById(R.id.detail_imageview);

            String base_uri = "https://image.tmdb.org/t/p/";
            String poster_size = "w185";
            String posterUri = base_uri + poster_size + "/" + movieList.posterPath;

            Picasso.with(getContext())
                    .load(posterUri)
                    .placeholder(R.drawable.sample_0)
                    .error(R.drawable.sample_7)
                            // .noFade()
//                    .resize(55,55)
//                    .centerCrop()
                            //.fit()
                    .into(imgView);

            ImageView imgBackdrpView = (ImageView) rootView.findViewById(R.id.backdrop_imageview);

            String backdrop_size = "w500";
            String backdropUri = base_uri + backdrop_size + "/" + movieList.backdropPath;

            Picasso.with(getContext())
                    .load(backdropUri)
                    .placeholder(R.drawable.sample_0)
                    .error(R.drawable.sample_7)
                    // .noFade()
                    //.fit()
                    .into(imgBackdrpView);

            //Set Trailer list
            {
                View trailerView = inflater.from(getContext()).inflate(R.layout.trailer_item, container, false);

                trailerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //play youtube yVideo
                    }
                });

                TextView trailerTitle = (TextView) trailerView.findViewById(R.id.trailer_text_title);
                trailerTitle.setText("Trailer text");

                LinearLayout trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailer_layout);
                trailerLayout.addView(trailerView);
            }

            //Set Review List
            {
                View reviewView = inflater.from(getContext()).inflate(R.layout.review_item, container, false);

                reviewView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //play youtube yVideo
                    }
                });

                TextView reviewAuthor = (TextView) reviewView.findViewById(R.id.review_text_author);
                reviewAuthor.setText("Review Author");

                TextView reviewContent = (TextView) reviewView.findViewById(R.id.review_text_content);
                reviewContent.setText("Review Content");

                LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.review_layout);
                reviewLayout.addView(reviewView);
            }

        }
        return rootView;
    }




}
