package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ArrayAdapter<String> mGridImageAdapter;

    String[] tempImageList = {"http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",};

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mGridImageAdapter = new ImageAdapter(getActivity(),
                R.layout.grid_item_movies,
                R.id.grid_item_movies_imageview,
                tempImageList);


        GridView gridview = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridview.setAdapter(mGridImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(),"Movies Pop",Toast.LENGTH_SHORT).show();
                String imageUri = mGridImageAdapter.getItem(position);
                Intent detailAct = new Intent(getActivity(),DetailActivity.class);
                detailAct.putExtra(Intent.EXTRA_TEXT, imageUri);
                startActivity(detailAct);

            }
        });

        return rootView;
    }

    //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.
    public class FetchMovieTask extends AsyncTask<String,Void,String[]>
    {
//        final String BASE_URL = "http://image.tmdb.org/t/p";
//        final String SIZE = "w185";

        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT = "sort_by";
        final String APPID = "api_key";

        String sortAlgo = "popularity.desc";
        String apiKey = "7cc4c6e656a9febdd4f903137522c890";

        private String [] getMovieDataFromJson(String movieJsonStr, String sortAlgo)
                throws JSONException
        {
            JSONObject movieJson = new JSONObject(movieJsonStr);

            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {
            if(params.length==0)
            {
                return null;
            }

            //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.

            //Dixit: These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT, sortAlgo)
                        .appendQueryParameter(APPID, apiKey)
                        .build();

                URL url = new URL(movieUri.toString());
                Log.v(LOG_TAG, "URL: " + url);

                urlConnection =(HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader((inputStream)));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG,"Movie JSON String: " + movieJsonStr);


            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error",e);
                return null;
            }

            try{
                //Dixit: to parse(as required) response data from server we call below function
                return getMovieDataFromJson(movieJsonStr, sortAlgo);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return null;
        }
    }



}
