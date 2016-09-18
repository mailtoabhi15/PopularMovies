package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

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

            //Add Trailers list
            addTrailerView(rootView,movieList);

            //Add Review List
            addReviewView(rootView, movieList);


        }
        return rootView;
    }

private void addTrailerView(View rootView,MyMovie movieList)
{
    final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    FetchTrailerTask trailerTask = new FetchTrailerTask();

    trailerTask.execute(movieList.id);

    try {
        MovieTrailer[] trailerList = trailerTask.get();

        for(final MovieTrailer trailerItem : trailerList) {

            View trailerView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item,null);

            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //play youtube yVideo
                    Uri youtubeUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                            .appendPath(trailerItem.trailer_source)
                            .build();

                    Intent watchVideoIntent = new Intent(Intent.ACTION_VIEW,youtubeUri);
                    startActivity(watchVideoIntent);

                }
            });

            TextView trailerTitle = (TextView) trailerView.findViewById(R.id.trailer_text_title);
            trailerTitle.setText(trailerItem.trailer_title);

            LinearLayout trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailer_layout);
            trailerLayout.addView(trailerView);
        }

    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }

}

    private void addReviewView(View rootView, MyMovie movieList)
    {

        FetchReviewTask reviewTask = new FetchReviewTask();

        reviewTask.execute(movieList.id);

        try {
            MovieReview[] reviewList = reviewTask.get();

            for (final MovieReview reviewItem : reviewList) {

            View reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, null);

            reviewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open url
                }
            });

            TextView reviewAuthor = (TextView) reviewView.findViewById(R.id.review_text_author);
            reviewAuthor.setText(reviewItem.review_author);

            TextView reviewContent = (TextView) reviewView.findViewById(R.id.review_text_content);
            reviewContent.setText(reviewItem.review_content);

            LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.review_layout);
            reviewLayout.addView(reviewView);
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
