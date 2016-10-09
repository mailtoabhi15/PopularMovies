package com.udacitynanodegreeapps.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by abhishek.dixit on 10/7/2016.
 */

public class FetchMoviesExtraTask extends AsyncTask<String, Void, ArrayList<MoviesExtra>> {


    public static final String LOG_TAG = FetchMoviesExtraTask.class.getSimpleName();
    private EventCallback eventCallback;


    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie?";

    final String APPID = "api_key";
    final String APEEND_TO_RESPONSE = "append_to_response";
    final String EXTRA_PARAMS = "trailers,reviews";

    public FetchMoviesExtraTask(EventCallback eventCallback){
        this.eventCallback = eventCallback;

    }

    private ArrayList<MoviesExtra> getMoviesExtraDataFromJson(String moviesExtraJsonStr)
            throws JSONException {

        final String TRAILERS = "trailers";
        final String YOUTUBE_ARRAY = "youtube";
        final String TRAILER_TITLE = "name";
        final String TRAILER_SOURCE = "source";

        final String REVIEWS = "reviews";
        final String REVIEWS_RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        ArrayList<MoviesExtra> moviesExtraList = new ArrayList<>();

        JSONObject moviesExtraJson = new JSONObject(moviesExtraJsonStr);

        JSONObject trailerJsonObject = moviesExtraJson.getJSONObject(TRAILERS);

        JSONArray trailerArray = trailerJsonObject.getJSONArray(YOUTUBE_ARRAY);

        JSONObject reviewJsonObject = moviesExtraJson.getJSONObject(REVIEWS);

        JSONArray reviewArray = reviewJsonObject.getJSONArray(REVIEWS_RESULTS);

        int loop =0;

        if(trailerArray.length() > reviewArray.length())
        {
            loop = trailerArray.length();
        }
        else{
            loop = reviewArray.length();
        }

        for (int i = 0; i < loop; i++) {

            MoviesExtra moviesExtra = new MoviesExtra();

            if (!trailerArray.isNull(i)) {
                JSONObject trailerObject = trailerArray.getJSONObject(i);

                if (!trailerObject.isNull(TRAILER_TITLE)) {
                    moviesExtra.setTrailer_title(trailerObject.getString(TRAILER_TITLE));
                    moviesExtra.setTrailer_source(trailerObject.getString(TRAILER_SOURCE));
                }
            }
            if (!reviewArray.isNull(i)) {
                JSONObject reviewObject = reviewArray.getJSONObject(i);

                if (!reviewObject.isNull(CONTENT)) {
                    moviesExtra.setReview_author(reviewObject.getString(AUTHOR));
                    moviesExtra.setReview_content(reviewObject.getString(CONTENT));
                    moviesExtra.setReview_url(reviewObject.getString(URL));
                }
            }
            moviesExtraList.add(moviesExtra);

        }

        return moviesExtraList;
    }


    @Override
    protected ArrayList<MoviesExtra> doInBackground(String... params) {

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
        String trailerJsonStr = null;

        try {

            Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(APPID, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                    .appendQueryParameter(APEEND_TO_RESPONSE, EXTRA_PARAMS)
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
                trailerJsonStr = null;
            }
            trailerJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movie JSON String: " + trailerJsonStr);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            trailerJsonStr = null;
        }

        try {
            //Dixit: to parse(as required) response data from server we call below function
            if (trailerJsonStr != null)
                return getMoviesExtraDataFromJson(trailerJsonStr);
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
    protected void onPostExecute(ArrayList<MoviesExtra> moviesExtras) {
        super.onPostExecute(moviesExtras);
        if(!moviesExtras.isEmpty() ){
            eventCallback.onEventReady(moviesExtras);
        }
    }
}