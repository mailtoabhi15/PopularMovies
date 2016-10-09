package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements EventCallback {

    //Dixit: added in lesson-5.40(2 Pane Ui)-Handling List Item Click
    static final String LIST_MOVIES_INDEX = "movies_index";

    private MyMovie movieList;
    ArrayList<MoviesExtra> moviesExtraList;

    private View rootView ;
//    private MovieTrailer[] mtrailerList;
//    private MovieReview[] mreviewList;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("moviesExtra")) {
                moviesExtraList = savedInstanceState.getParcelableArrayList("moviesExtra");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(moviesExtraList!=null) {
            outState.putParcelableArrayList("moviesExtra", moviesExtraList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Dixit-imp:start: added in lesson-5.40(2 Pane Ui)-Handling List Item Click
        //Reading the saved bundle arguments i.e clicked uri/item, if the activity was killed/started
        Bundle args = getArguments();
        if (args != null) {
            movieList = args.getParcelable(LIST_MOVIES_INDEX);
        }
        //Dixit:end

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Now we will handle the Intent we sent from MainActivityFragment here
//        Intent intent = getActivity().getIntent();
        if (movieList != null) {

//            final MyMovie movieList = intent.getParcelableExtra("movielist");

            ((TextView) rootView.findViewById(R.id.textView_title)).setText(movieList.title);


            TextView overView = (TextView) rootView.findViewById(R.id.textView_overview);
            overView.setText(movieList.overview);


            ((TextView) rootView.findViewById(R.id.textView_date)).setText("Release Date: " + movieList.releaseDate);

            ((TextView) rootView.findViewById(R.id.textView_avgvote)).setText("Avg. Vote: " + movieList.voteAvg);


            ImageView imgView = (ImageView) rootView.findViewById(R.id.detail_imageview);

            String base_uri = "https://image.tmdb.org/t/p/";
            String poster_size = "w185";
            final String posterUri = base_uri + poster_size + "/" + movieList.posterPath;

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
                    .into(imgBackdrpView);

            //set fav
            final CheckBox favBox = (CheckBox) rootView.findViewById(R.id.chkbox_favourite);
            final SharedPreferences mfavPref = getActivity().getSharedPreferences("fav_movie", Context.MODE_PRIVATE);

            Map<String, ?> keys = mfavPref.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                if (entry.getValue().toString() != null) {
                    Log.d("mfavPref values", entry.getKey() + ": " + entry.getValue().toString());
                    if (entry.getKey().equals(movieList.id)) {
                        favBox.setChecked(true);
                        favBox.setText(R.string.remove_fav);
                    }
                }
            }

            favBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SharedPreferences.Editor prefEditor = mfavPref.edit();

                    if (isChecked) {
                        if (!mfavPref.contains(movieList.id)) {

                            Gson gson = new Gson();
                            String jsonFav = gson.toJson(movieList);

                            prefEditor.putString(movieList.id, jsonFav);
                            prefEditor.apply();
                            favBox.setText(R.string.remove_fav);
                        }
                    } else {
                        prefEditor.remove(movieList.id);
                        prefEditor.apply();
                        favBox.setText(R.string.favourite);
                    }
                }
            });

            if(moviesExtraList == null) {
                FetchMoviesExtraTask moviesExtra = new FetchMoviesExtraTask(getActivity(),this);
                moviesExtra.execute(movieList.id);
            }
            else{
                showTrailers(moviesExtraList);

                showReviews(moviesExtraList);
            }
//            //Add Trailers list
//            addTrailerView(rootView, movieList.id);
//
//            //Add Review List
//            addReviewView(rootView, movieList.id);


        }
        return rootView;
    }

    @Override
    public void onEventReady(ArrayList<MoviesExtra> result) {

        moviesExtraList = result;

        showTrailers(moviesExtraList);

        showReviews(moviesExtraList);

    }

    private void showTrailers(ArrayList<MoviesExtra> moviesExtraList) {

        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

        final String YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/";

        for (final MoviesExtra moviesExtra : moviesExtraList) {
            if (moviesExtra.getTrailer_source() != null && (getContext() != null)) {

                View trailerView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, null);

                trailerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //play youtube yVideo
                        Uri youtubeUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                                .appendPath(moviesExtra.getTrailer_source())
                                .build();

                        Intent watchVideoIntent = new Intent(Intent.ACTION_VIEW, youtubeUri);
                        startActivity(watchVideoIntent);

                    }
                });

                TextView trailerTitle = (TextView) trailerView.findViewById(R.id.trailer_text_title);
                trailerTitle.setText(moviesExtra.getTrailer_title());

                ImageView videoThumbnailView = (ImageView) trailerView.findViewById(R.id.trailer_image);
                String thumbnailUrl = YOUTUBE_THUMBNAIL + moviesExtra.getTrailer_source() + "/0.jpg";

                Picasso.with(getContext())
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.sample_0)
                        .error(android.R.drawable.ic_media_play)
                        .into(videoThumbnailView);


                LinearLayout trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailer_layout);
                trailerLayout.addView(trailerView);

            }
        }
    }

    private void showReviews(ArrayList<MoviesExtra> moviesExtraList) {

        for (final MoviesExtra moviesExtra : moviesExtraList) {
            if (moviesExtra.getReview_content() != null && (getContext() != null)) {
                View reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, null);

                TextView reviewAuthor = (TextView) reviewView.findViewById(R.id.review_text_author);
                reviewAuthor.setText(moviesExtra.getReview_author());

                TextView reviewContent = (TextView) reviewView.findViewById(R.id.review_text_content);
                reviewContent.setText(moviesExtra.getReview_content());

                LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.review_layout);
                reviewLayout.addView(reviewView);
            }
        }

    }


//    private void addTrailerView(View rootView, String movieId) {
//        final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
//
//        final String YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/";
//
//        if (mtrailerList == null) {
//            FetchTrailerTask trailerTask = new FetchTrailerTask();
//
//            trailerTask.execute(movieId);
//
//            //Dixit-Imp: This is a Blocking call , Needs to be revisited with/for better design
//            try {
//                mtrailerList = trailerTask.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        if (mtrailerList!=null) {
//
//            for (final MovieTrailer trailerItem : mtrailerList) {
//
//                View trailerView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, null);
//
//                trailerView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //play youtube yVideo
//                        Uri youtubeUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
//                                .appendPath(trailerItem.trailer_source)
//                                .build();
//
//                        Intent watchVideoIntent = new Intent(Intent.ACTION_VIEW, youtubeUri);
//                        startActivity(watchVideoIntent);
//
//                    }
//                });
//
//                TextView trailerTitle = (TextView) trailerView.findViewById(R.id.trailer_text_title);
//                trailerTitle.setText(trailerItem.trailer_title);
//
//                ImageView videoThumbnailView = (ImageView) trailerView.findViewById(R.id.trailer_image);
//                String thumbnailUrl = YOUTUBE_THUMBNAIL + trailerItem.trailer_source + "/0.jpg";
//
//                Picasso.with(getContext())
//                        .load(thumbnailUrl)
//                        .placeholder(R.drawable.sample_0)
//                        .error(android.R.drawable.ic_media_play)
//                        .into(videoThumbnailView);
//
//
//                LinearLayout trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailer_layout);
//                trailerLayout.addView(trailerView);
//            }
//        }
//        else {
//            //Dixit: Need to show some Dummy Data for NO TRAILERS Available
//            return;
//        }
//    }
//
//    private void addReviewView(View rootView, String movieId) {
//
//
//        if (mreviewList == null) {
//            FetchReviewTask reviewTask = new FetchReviewTask();
//
//            reviewTask.execute(movieId);
//
//            //Dixit-Imp: This iks a Blocking call , Needs to be revisited with/for better design
//            try {
//                mreviewList = reviewTask.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (mreviewList != null) {
//
//            for (final MovieReview reviewItem : mreviewList) {
//
//                View reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, null);
//
//    //            reviewView.setOnClickListener(new View.OnClickListener() {
//    //                @Override
//    //                public void onClick(View v) {
//    //                    //open url
//    //                }
//    //            });
//
//                TextView reviewAuthor = (TextView) reviewView.findViewById(R.id.review_text_author);
//                reviewAuthor.setText(reviewItem.review_author);
//
//                TextView reviewContent = (TextView) reviewView.findViewById(R.id.review_text_content);
//                reviewContent.setText(reviewItem.review_content);
//
//                LinearLayout reviewLayout = (LinearLayout) rootView.findViewById(R.id.review_layout);
//                reviewLayout.addView(reviewView);
//            }
//        }
//        else{
//            //Dixit: Need to show some Dummy Data for NO REVIEWS Available
//            return;
//        }
//    }

}
