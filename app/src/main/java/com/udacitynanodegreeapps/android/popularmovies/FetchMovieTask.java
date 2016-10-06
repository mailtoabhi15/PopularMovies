package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhishek.dixit on 9/17/2016.
 */
//Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.
public class FetchMovieTask extends AsyncTask<String, Void, MyMovie[]> {
//       BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";

    public static final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private ImageAdapter mGridImageAdapter;
    private final Context mContext;

    public FetchMovieTask(Context context, ImageAdapter imageAdapter) {
        this.mContext = context;
        this.mGridImageAdapter = imageAdapter;
    }

    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    final String SORT = "sort_by";
    final String APPID = "api_key";


    private MyMovie[] getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String ID = "id";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String TITLE = "original_title";
        final String BACKDROP_PATH = "backdrop_path";
        final String VOTE_AVG = "vote_average";


        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        MyMovie[] movieList = new MyMovie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObject = movieArray.getJSONObject(i);

            movieList[i] = new MyMovie(
                    movieObject.getString(ID),
                    movieObject.getString(TITLE),
                    movieObject.getString(OVERVIEW),
                    movieObject.getString(RELEASE_DATE),
                    movieObject.getString(POSTER_PATH),
                    movieObject.getString(BACKDROP_PATH),
                    movieObject.getDouble(VOTE_AVG));

        }

        return movieList;
    }

    @Override
    protected MyMovie[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.

        //Dixit: These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

//            StethoURLConnectionManager stethoManager;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {

            Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT, params[0])
                    .appendQueryParameter("vote_average.gte", "5")
                    .appendQueryParameter(APPID, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(movieUri.toString());
            Log.v(LOG_TAG, "URL: " + url);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
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

            Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            movieJsonStr = null;
        }

        try {
            //Dixit: to parse(as required) response data from server we call below function
            if (movieJsonStr != null)
                return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
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

    @Override
    protected void onPostExecute(MyMovie[] result) {
        if (result == null)
            Toast.makeText(mContext, "Please Check Network Connection", Toast.LENGTH_SHORT).show();
        else {
            mGridImageAdapter.clear();
            for (MyMovie movieList : result)
                mGridImageAdapter.addAll(movieList);
        }
    }
}