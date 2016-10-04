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
import static com.udacitynanodegreeapps.android.popularmovies.R.string.favourite;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    //Dixit: added in lesson-5.40(2 Pane Ui)-Handling List Item Click
    static final String LIST_MOVIES_INDEX = "movies_index";

    private MyMovie movieList;

    public DetailActivityFragment() {
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState == null || !savedInstanceState.containsKey("moviedetails")){
//            movieList = new ArrayList<MyMovie>();
//        }
//        else{
//            movieList = savedInstanceState.getParcelableArrayList("moviedetails");
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelableArrayList("moviedetails", movieList);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Dixit-imp:start: added in lesson-5.40(2 Pane Ui)-Handling List Item Click
        //Reading the saved bundle arguments i.e clicked uri/item, if the activity was killed/started
        Bundle args = getArguments();
        if (args!= null) {
            movieList = args.getParcelable(LIST_MOVIES_INDEX);
        }
        //Dixit:end

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Now we will handle the Intent we sent from MainActivityFragment here
//        Intent intent = getActivity().getIntent();
        if (movieList !=null) {

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
                if (entry.getValue().toString() != null)
                {
                    Log.d("mfavPref values", entry.getKey() + ": " + entry.getValue().toString());
                    if (entry.getKey().equals(movieList.id))
                    {
                        favBox.setChecked(true);
                        favBox.setText(R.string.remove_fav);
                    }
                }
            }

            favBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SharedPreferences.Editor prefEditor = mfavPref.edit();

                    if(isChecked)
                    {
                        if (!mfavPref.contains(movieList.id)) {

                            Gson gson = new Gson();
                            String jsonFav = gson.toJson(movieList);

                            prefEditor.putString(movieList.id, jsonFav);
                            prefEditor.apply();
                            favBox.setText(R.string.remove_fav);
                        }
                    }
                    else
                    {
                        prefEditor.remove(movieList.id);
                        prefEditor.apply();
                        favBox.setText(R.string.favourite);
                    }
                }
            });

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

    final String YOUTUBE_THUMBNAIL = "http://img.youtube.com/vi/" ;

    FetchTrailerTask trailerTask = new FetchTrailerTask();

    trailerTask.execute(movieList.id);

    try {
        MovieTrailer[] trailerList = trailerTask.get();
        if(trailerList == null)
            return;

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

            ImageView videoThumbnailView = (ImageView) trailerView.findViewById(R.id.trailer_image);
            String thumbnailUrl = YOUTUBE_THUMBNAIL + trailerItem.trailer_source + "/0.jpg";

            Picasso.with(getContext())
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.sample_0)
                    .error(android.R.drawable.ic_media_play)
                    .into(videoThumbnailView);


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
            if(reviewList == null)
                return;

            for (final MovieReview reviewItem : reviewList) {

            View reviewView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, null);

//            reviewView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //open url
//                }
//            });

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
