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

/**
 * Created by abhishek.dixit on 9/17/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, MovieTrailer[]> {


    public static final String LOG_TAG = FetchTrailerTask.class.getSimpleName();


    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie?";

    final String APPID = "api_key";
    final String APEEND_TO_RESPONSE = "append_to_response";
    final String EXTRA_PARAMS = "trailers";


    private MovieTrailer[] getTrailerDataFromJson(String trailerJsonStr)
            throws JSONException {

        final String TRAILERS = "trailers";
        final String YOUTUBE_ARRAY = "youtube";
        final String TRAILER_TITLE = "name";
        final String TRAILER_SOURCE = "source";


        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONObject trailerJsonObject = trailerJson.getJSONObject(TRAILERS);

        JSONArray trailerArray = trailerJsonObject.getJSONArray(YOUTUBE_ARRAY);

        MovieTrailer[] trailerList = new MovieTrailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {

            JSONObject trailerObject = trailerArray.getJSONObject(i);

            trailerList[i] = new MovieTrailer(
                    trailerObject.getString(TRAILER_TITLE),
                    trailerObject.getString(TRAILER_SOURCE));

        }

        return trailerList;
    }


    @Override
    protected MovieTrailer[] doInBackground(String... params) {

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
                return getTrailerDataFromJson(trailerJsonStr);
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


}
